package com.gestion.projet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PatientMedicamentController {

    @FXML
    private TableView<PatientMedicament> tablemedPat;

    @FXML
    private TableColumn<PatientMedicament, String> colonnePatCin;

    @FXML
    private TableColumn<PatientMedicament, String> colonneMedRef;

    @FXML
    private ComboBox<String> PatientComboBox;

    @FXML
    private ComboBox<String> medicamentComboBox;

    @FXML
    private Button buttonAdd;

    @FXML
    private Button buttonUpdate;

    @FXML
    private Button buttonDelete;

    private ObservableList<String> patientsList = FXCollections.observableArrayList();

    private ObservableList<String> medicamentsList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colonnePatCin.setCellValueFactory(new PropertyValueFactory<>("patientCin"));
        colonneMedRef.setCellValueFactory(new PropertyValueFactory<>("medicamentRef"));

        buttonAdd.setOnAction(event -> handleAdd());
        buttonUpdate.setOnAction(event -> handleUpdate());
        buttonDelete.setOnAction(event -> handleDelete());

        loadPatientsFromDatabase();
        loadMedicamentsFromDatabase();
        loadRelationsFromDatabase();

        tablemedPat.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Sélectionner les valeurs dans les ComboBox correspondantes
                PatientComboBox.getSelectionModel().select(newValue.getPatientCin());
                medicamentComboBox.getSelectionModel().select(newValue.getMedicamentRef());
            }
        });
    }

    @FXML
    private void handleAdd() {
        String patientCin = PatientComboBox.getSelectionModel().getSelectedItem();
        String medicamentRef = medicamentComboBox.getSelectionModel().getSelectedItem();

        if (patientCin != null && medicamentRef != null) {
            try {
                Connection conn = DatabaseConnection.getConnection();
                if (conn != null) {
                    String sql = "INSERT INTO PatientMedicament (patient_cin, medicament_ref) VALUES (?, ?)";
                    PreparedStatement statement = conn.prepareStatement(sql);
                    statement.setString(1, patientCin);
                    statement.setString(2, medicamentRef);

                    int rowsAffected = statement.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Relation patient-médicament insérée avec succès dans la base de données.");
                        // Insérer la nouvelle relation dans la TableView
                        PatientMedicament newRelation = new PatientMedicament(0, patientCin, medicamentRef);
                        tablemedPat.getItems().add(newRelation);

                        // Afficher une alerte de succès
                        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                        successAlert.setTitle("Succès");
                        successAlert.setHeaderText(null);
                        successAlert.setContentText("Relation patient-médicament ajoutée avec succès.");
                        successAlert.showAndWait();
                    } else {
                        System.out.println("Aucune relation patient-médicament insérée dans la base de données.");
                    }
                    statement.close();
                    conn.close();
                } else {
                    System.out.println("La connexion à la base de données a échoué.");
                }
            } catch (SQLException e) {
                System.err.println("Erreur lors de l'insertion de la relation patient-médicament dans la base de données : " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("Veuillez sélectionner un patient et un médicament.");
            // Afficher une alerte d'avertissement
            Alert warningAlert = new Alert(Alert.AlertType.WARNING);
            warningAlert.setTitle("Avertissement");
            warningAlert.setHeaderText(null);
            warningAlert.setContentText("Veuillez sélectionner un patient et un médicament.");
            warningAlert.showAndWait();
        }
    }

    @FXML
    private void handleUpdate() {
        PatientMedicament selectedRelation = tablemedPat.getSelectionModel().getSelectedItem();
        if (selectedRelation != null) {
            String patientCin = PatientComboBox.getSelectionModel().getSelectedItem();
            String medicamentRef = medicamentComboBox.getSelectionModel().getSelectedItem();

            if (patientCin != null && medicamentRef != null) {
                try {
                    Connection conn = DatabaseConnection.getConnection();
                    if (conn != null) {
                        String sql = "UPDATE PatientMedicament SET patient_cin = ?, medicament_ref = ? WHERE id = ?";
                        PreparedStatement statement = conn.prepareStatement(sql);
                        statement.setString(1, patientCin);
                        statement.setString(2, medicamentRef);
                        statement.setInt(3, selectedRelation.getId());

                        int rowsAffected = statement.executeUpdate();
                        if (rowsAffected > 0) {
                            System.out.println("Relation patient-médicament mise à jour avec succès dans la base de données.");
                            // Mettre à jour la TableView
                            selectedRelation.setPatientCin(patientCin);
                            selectedRelation.setMedicamentRef(medicamentRef);
                            tablemedPat.refresh();

                            // Afficher une alerte de succès
                            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                            successAlert.setTitle("Succès");
                            successAlert.setHeaderText(null);
                            successAlert.setContentText("Relation patient-médicament mise à jour avec succès.");
                            successAlert.showAndWait();
                        } else {
                            System.out.println("Aucune relation patient-médicament mise à jour dans la base de données.");
                        }
                        statement.close();
                        conn.close();
                    } else {
                        System.out.println("La connexion à la base de données a échoué.");
                    }
                } catch (SQLException e) {
                    System.err.println("Erreur lors de la mise à jour de la relation patient-médicament dans la base de données : " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                System.out.println("Veuillez sélectionner un patient et un médicament.");
                // Afficher une alerte d'avertissement
                Alert warningAlert = new Alert(Alert.AlertType.WARNING);
                warningAlert.setTitle("Avertissement");
                warningAlert.setHeaderText(null);
                warningAlert.setContentText("Veuillez sélectionner un patient et un médicament.");
                warningAlert.showAndWait();
            }
        } else {
            System.out.println("Veuillez sélectionner une relation patient-médicament à mettre à jour.");
            // Afficher une alerte d'avertissement
            Alert warningAlert = new Alert(Alert.AlertType.WARNING);
            warningAlert.setTitle("Avertissement");
            warningAlert.setHeaderText(null);
            warningAlert.setContentText("Veuillez sélectionner une relation patient-médicament à mettre à jour.");
            warningAlert.showAndWait();
        }
    }

    @FXML
    private void handleDelete() {
        PatientMedicament selectedRelation = tablemedPat.getSelectionModel().getSelectedItem();
        if (selectedRelation != null) {
            try {
                Connection conn = DatabaseConnection.getConnection();
                if (conn != null) {
                    String sql = "DELETE FROM PatientMedicament WHERE id = ?";
                    PreparedStatement statement = conn.prepareStatement(sql);
                    statement.setInt(1, selectedRelation.getId());

                    int rowsAffected = statement.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Relation patient-médicament supprimée avec succès de la base de données.");
                        tablemedPat.getItems().remove(selectedRelation);

                        // Afficher une alerte de succès
                        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                        successAlert.setTitle("Succès");
                        successAlert.setHeaderText(null);
                        successAlert.setContentText("Relation patient-médicament supprimée avec succès.");
                        successAlert.showAndWait();
                    } else {
                        System.out.println("Aucune relation patient-médicament supprimée de la base de données.");
                    }
                    statement.close();
                    conn.close();
                } else {
                    System.out.println("La connexion à la base de données a échoué.");
                }
            } catch (SQLException e) {
                System.err.println("Erreur lors de la suppression de la relation patient-médicament de la base de données : " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("Veuillez sélectionner une relation patient-médicament à supprimer.");
            // Afficher une alerte d'avertissement
            Alert warningAlert = new Alert(Alert.AlertType.WARNING);
            warningAlert.setTitle("Avertissement");
            warningAlert.setHeaderText(null);
            warningAlert.setContentText("Veuillez sélectionner une relation patient-médicament à supprimer.");
            warningAlert.showAndWait();
        }
    }

    private void loadPatientsFromDatabase() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            if (conn != null) {
                String sql = "SELECT cin FROM patient";
                PreparedStatement statement = conn.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery();

                patientsList.clear();

                while (resultSet.next()) {
                    String cin = resultSet.getString("cin");
                    patientsList.add(cin);
                }

                PatientComboBox.setItems(patientsList);

                resultSet.close();
                statement.close();
                conn.close();
            } else {
                System.out.println("La connexion à la base de données a échoué.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors du chargement des patients depuis la base de données : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadMedicamentsFromDatabase() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            if (conn != null) {
                String sql = "SELECT ref FROM medicament";
                PreparedStatement statement = conn.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery();

                medicamentsList.clear();

                while (resultSet.next()) {
                    String ref = resultSet.getString("ref");
                    medicamentsList.add(ref);
                }

                medicamentComboBox.setItems(medicamentsList);

                resultSet.close();
                statement.close();
                conn.close();
            } else {
                System.out.println("La connexion à la base de données a échoué.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors du chargement des médicaments depuis la base de données : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadRelationsFromDatabase() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            if (conn != null) {
                String sql = "SELECT id, patient_cin, medicament_ref FROM PatientMedicament";
                PreparedStatement statement = conn.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String patientCin = resultSet.getString("patient_cin");
                    String medicamentRef = resultSet.getString("medicament_ref");

                    PatientMedicament relation = new PatientMedicament(id, patientCin, medicamentRef);
                    tablemedPat.getItems().add(relation);
                }

                resultSet.close();
                statement.close();
                conn.close();
            } else {
                System.out.println("La connexion à la base de données a échoué.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors du chargement des relations patient-médicament depuis la base de données : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
