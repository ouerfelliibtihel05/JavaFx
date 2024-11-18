package com.gestion.projet;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PersonnelController {

    @FXML
    private TextField cinField;
    @FXML
    private TextField nomField;
    @FXML
    private TextField prenomField;
    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField fonctionnaliteField;
    @FXML
    private Button btnRegister;

    @FXML
    public void initialize() {
        btnRegister.setOnAction(event -> handleRegister());
    }

    private void handleRegister() {
        String cin = cinField.getText();
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String login = loginField.getText();
        String password = passwordField.getText();
        String fonctionnalite = fonctionnaliteField.getText();

        if (validateInput(cin, nom, prenom, login, password, fonctionnalite)) {
            registerPersonnel(cin, nom, prenom, login, password, fonctionnalite);
        } else {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Veuillez remplir tous les champs.");
        }
    }

    private boolean validateInput(String cin, String nom, String prenom, String login, String password, String fonctionnalite) {
        return !cin.isEmpty() && !nom.isEmpty() && !prenom.isEmpty() && !login.isEmpty() && !password.isEmpty() && !fonctionnalite.isEmpty();
    }

    private void registerPersonnel(String cin, String nom, String prenom, String login, String password, String fonctionnalite) {
        String insertPersonnelSQL = "INSERT INTO personnel (cin, nom, prenom, login, password, fonctionnalite) VALUES (?, ?, ?, ?, ?, ?)";
        String insertLoginSQL = "INSERT INTO login (login, password) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null) {
                conn.setAutoCommit(false); // Start transaction

                try (PreparedStatement personnelStatement = conn.prepareStatement(insertPersonnelSQL);
                     PreparedStatement loginStatement = conn.prepareStatement(insertLoginSQL)) {

                    personnelStatement.setString(1, cin);
                    personnelStatement.setString(2, nom);
                    personnelStatement.setString(3, prenom);
                    personnelStatement.setString(4, login);
                    personnelStatement.setString(5, password); // Il est recommandé de hacher le mot de passe ici
                    personnelStatement.setString(6, fonctionnalite);

                    int personnelRowsAffected = personnelStatement.executeUpdate();

                    loginStatement.setString(1, login);
                    loginStatement.setString(2, password); // Il est recommandé de hacher le mot de passe ici

                    int loginRowsAffected = loginStatement.executeUpdate();

                    if (personnelRowsAffected > 0 && loginRowsAffected > 0) {
                        conn.commit(); // Commit transaction
                        showAlertAndRedirect();
                    } else {
                        conn.rollback(); // Rollback transaction
                        showAlert(Alert.AlertType.ERROR, "Registration Error", "Échec de l'enregistrement. Veuillez réessayer.");
                    }
                } catch (SQLException e) {
                    conn.rollback(); // Rollback transaction on error
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Database Error", "Une erreur est survenue. Veuillez réessayer.");
                } finally {
                    conn.setAutoCommit(true); // Reset auto-commit to true
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Database Connection Error", "Échec de la connexion à la base de données.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Une erreur est survenue. Veuillez réessayer.");
        }
    }

    private void showAlertAndRedirect() {
        // Create and show the alert
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText("Enregistrement réussi!");

        // Wait for the user to close the alert
        alert.showAndWait();

        // Redirect to the login view
        try {
            Stage stage = (Stage) btnRegister.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Redirection Error", "Erreur de redirection vers la page de connexion.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
