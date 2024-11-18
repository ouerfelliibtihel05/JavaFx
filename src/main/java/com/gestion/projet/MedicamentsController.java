package com.gestion.projet;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MedicamentsController {

    @FXML
    private Button buttonAdd;
    @FXML
    private Button buttonDelete;
    @FXML
    private Button buttonUpdate;
    @FXML
    private Button buttonSearch;
    @FXML
    private TableView<Medicament> tablemedicaments;
    @FXML
    private TableColumn<Medicament, String> colonneRef;
    @FXML
    private TableColumn<Medicament, String> colonneLibelle;
    @FXML
    private TableColumn<Medicament, String> colonnePrix;
    @FXML
    private TextField ref;
    @FXML
    private TextField libelle;
    @FXML
    private TextField prix;
    @FXML
    private TextField searchLibelle;
    @FXML
    private Medicament selectedMedicament;

    @FXML
    public void initialize() {
        colonneRef.setCellValueFactory(new PropertyValueFactory<>("ref"));
        colonneLibelle.setCellValueFactory(new PropertyValueFactory<>("libelle"));
        colonnePrix.setCellValueFactory(new PropertyValueFactory<>("prix"));

        buttonAdd.setOnAction(event -> handleAdd());
        buttonDelete.setOnAction(event -> handleDelete());
        buttonUpdate.setOnAction(event -> handleUpdate());
        buttonSearch.setOnAction(event -> handleSearch());

        tablemedicaments.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedMedicament = newValue;
                ref.setText(newValue.getRef());
                libelle.setText(newValue.getLibelle());
                prix.setText(newValue.getPrix());
            }
        });
        loadMedicamentsFromDatabase();
    }

    @FXML
    private void handleAdd() {
        if (validateInput()) {
            Medicament nouveauMedicament = new Medicament(ref.getText(), libelle.getText(), prix.getText());

            try {
                Connection conn = DatabaseConnection.getConnection();
                if (conn != null) {
                    String sql = "INSERT INTO medicament (ref, libelle, prix) VALUES (?, ?, ?)";
                    PreparedStatement statement = conn.prepareStatement(sql);
                    statement.setString(1, nouveauMedicament.getRef());
                    statement.setString(2, nouveauMedicament.getLibelle());
                    statement.setString(3, nouveauMedicament.getPrix());

                    int rowsAffected = statement.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Nouveau Medicament inséré avec succès dans la base de données.");
                        tablemedicaments.getItems().add(nouveauMedicament);
                    } else {
                        System.out.println("Aucun Medicament inséré dans la base de données.");
                    }
                    conn.close();
                } else {
                    System.out.println("La connexion à la base de données a échoué.");
                }
            } catch (SQLException e) {
                System.err.println("Erreur lors de l'insertion du Medicament dans la base de données : " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Champs vides");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs.");
            alert.showAndWait();
        }
    }
    private boolean validateInput() {
        return !ref.getText().isEmpty() && !libelle.getText().isEmpty() && !prix.getText().isEmpty();
    }

    private void loadMedicamentsFromDatabase() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            if (conn != null) {
                String sql = "SELECT * FROM medicament";
                PreparedStatement statement = conn.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    String ref = resultSet.getString("ref");
                    String libelle = resultSet.getString("libelle");
                    String prix = resultSet.getString("prix");

                    Medicament medicament = new Medicament(ref, libelle, prix);
                    tablemedicaments.getItems().add(medicament);
                }

                resultSet.close();
                statement.close();
                conn.close();
            } else {
                System.out.println("La connexion à la base de données a échoué.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors du chargement des medicaments depuis la base de données : " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void handleDelete() {
        selectedMedicament = tablemedicaments.getSelectionModel().getSelectedItem();
        if (selectedMedicament != null) {
            try {
                Connection conn = DatabaseConnection.getConnection();
                if (conn != null) {
                    String sql = "DELETE FROM medicament WHERE ref = ?";
                    PreparedStatement statement = conn.prepareStatement(sql);
                    statement.setString(1, selectedMedicament.getRef());

                    int rowsAffected = statement.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Medicament supprimé avec succès de la base de données.");
                        tablemedicaments.getItems().remove(selectedMedicament);
                    } else {
                        System.out.println("Aucun medicament supprimé de la base de données.");
                    }
                    statement.close();
                    conn.close();
                } else {
                    System.out.println("La connexion à la base de données a échoué.");
                }
            } catch (SQLException e) {
                System.err.println("Erreur lors de la suppression du medicament de la base de données : " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune sélection");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un medicament à supprimer.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleUpdate() {
        if (selectedMedicament != null) {
            String newRef = ref.getText();
            String newLibelle = libelle.getText();
            String newPrix = prix.getText();

            if (validateInput()) {
                try {
                    Connection conn = DatabaseConnection.getConnection();
                    if (conn != null) {
                        String sql = "UPDATE medicament SET ref = ?, libelle = ?, prix = ? WHERE ref = ?";
                        PreparedStatement statement = conn.prepareStatement(sql);
                        statement.setString(1, newRef);
                        statement.setString(2, newLibelle);
                        statement.setString(3, newPrix);
                        statement.setString(4, selectedMedicament.getRef());

                        int rowsAffected = statement.executeUpdate();
                        if (rowsAffected > 0) {
                            System.out.println("Medicament mis à jour avec succès dans la base de données.");
                            selectedMedicament.setRef(newRef);
                            selectedMedicament.setLibelle(newLibelle);
                            selectedMedicament.setPrix(newPrix);
                            tablemedicaments.refresh();
                        } else {
                            System.out.println("Aucun medicament mis à jour dans la base de données.");
                        }
                        statement.close();
                        conn.close();
                    } else {
                        System.out.println("La connexion à la base de données a échoué.");
                    }
                } catch (SQLException e) {
                    System.err.println("Erreur lors de la mise à jour du medicament dans la base de données : " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Champs manquants");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez remplir tous les champs.");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune sélection");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un medicament à mettre à jour.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleSearch() {
        String searchText = searchLibelle.getText().trim();
        tablemedicaments.getItems().clear();
        if (!searchText.isEmpty()) {
            try {
                Connection conn = DatabaseConnection.getConnection();
                if (conn != null) {
                    String sql = "SELECT * FROM medicament WHERE libelle LIKE ?";
                    PreparedStatement statement = conn.prepareStatement(sql);
                    statement.setString(1, "%" + searchText + "%");
                    ResultSet resultSet = statement.executeQuery();

                    while (resultSet.next()) {
                        String ref = resultSet.getString("ref");
                        String libelle = resultSet.getString("libelle");
                        String prix = resultSet.getString("prix");

                        Medicament medicament = new Medicament(ref, libelle, prix);
                        tablemedicaments.getItems().add(medicament);
                    }

                    resultSet.close();
                    statement.close();
                    conn.close();
                } else {
                    System.out.println("La connexion à la base de données a échoué.");
                }
            } catch (SQLException e) {
                System.err.println("Erreur lors de la recherche des medicaments dans la base de données : " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            loadMedicamentsFromDatabase();
        }
    }

}



