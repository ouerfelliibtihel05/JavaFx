package com.gestion.projet;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GestionPatientController {

    @FXML
    private Button buttonAdd;
    @FXML
    private Button buttonDelete;
    @FXML
    private Button buttonUpdate;
    @FXML
    private Button buttonGeneratePDF;
    @FXML
    private TableView<Patient> tablepatients;
    @FXML
    private TableColumn<Patient, String> colonnenom;
    @FXML
    private TableColumn<Patient, String> colonneprenom;
    @FXML
    private TableColumn<Patient, String> colonnetel;
    @FXML
    private TextField cin;
    @FXML
    private TextField nomfamille;
    @FXML
    private TextField prenom;
    @FXML
    private TextField tel;
    @FXML
    private RadioButton masculin;
    @FXML
    private RadioButton feminin;
    @FXML
    private TextField searchNom;
    @FXML
    private Button buttonSearch;

    private ToggleGroup genderGroup = new ToggleGroup();
    private Patient selectedPatient;

    @FXML
    public void initialize() {
        masculin.setToggleGroup(genderGroup);
        feminin.setToggleGroup(genderGroup);
        colonnenom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colonneprenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colonnetel.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        initializeListeners();
        loadPatientsFromDatabase();
    }

    private void initializeListeners() {
        buttonAdd.setOnAction(event -> handleAdd());
        buttonDelete.setOnAction(event -> handleDelete());
        buttonUpdate.setOnAction(event -> handleUpdate());
        buttonSearch.setOnAction(event -> handleSearch());
        buttonGeneratePDF.setOnAction(event -> handleGeneratePDF());

        tablepatients.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedPatient = newSelection;
                loadSelectedPatientInfo();
            }
        });
    }

    @FXML
    private void handleGeneratePDF() {
        Patient selectedPatient = tablepatients.getSelectionModel().getSelectedItem();
        if (selectedPatient != null) {
            PDFGenerator pdfGenerator = new PDFGenerator();
            pdfGenerator.generatePatientPDF(selectedPatient);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("PDF Generated");
            alert.setHeaderText(null);
            alert.setContentText("Le PDF a été généré avec succès.");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un patient pour générer un PDF.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleAdd() {
        if (validateInput()) {
            Patient nouveauPatient = new Patient(nomfamille.getText(), prenom.getText(), tel.getText());
            try {
                Connection conn = DatabaseConnection.getConnection();
                if (conn != null) {
                    String sql = "INSERT INTO patient (nomfamille, prenom, sexe, tel, cin) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement statement = conn.prepareStatement(sql);
                    statement.setString(1, nouveauPatient.getNom());
                    statement.setString(2, nouveauPatient.getPrenom());
                    statement.setString(3, masculin.isSelected() ? "Masculin" : "Féminin");
                    statement.setString(4, nouveauPatient.getTelephone());
                    statement.setString(5, cin.getText());
                    int rowsAffected = statement.executeUpdate();
                    if (rowsAffected > 0) {
                        tablepatients.getItems().add(nouveauPatient);
                    }
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Champs manquants");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs avant d'ajouter un nouveau patient.");
            alert.showAndWait();
        }
    }

    @FXML
    private void loadPatientsFromDatabase() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            if (conn != null) {
                String sql = "SELECT * FROM patient";
                PreparedStatement statement = conn.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    String nomFamille = resultSet.getString("nomfamille");
                    String prenom = resultSet.getString("prenom");
                    String sexe = resultSet.getString("sexe");
                    String tel = resultSet.getString("tel");
                    String cin = resultSet.getString("cin");
                    Patient patient = new Patient(nomFamille, prenom, tel);
                    patient.setSexe(sexe);
                    patient.setCin(cin);
                    tablepatients.getItems().add(patient);
                }
                resultSet.close();
                statement.close();
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadSelectedPatientInfo() {
        if (selectedPatient != null) {
            cin.setText(selectedPatient.getCin());
            nomfamille.setText(selectedPatient.getNom());
            prenom.setText(selectedPatient.getPrenom());
            tel.setText(selectedPatient.getTelephone());
            if ("Masculin".equals(selectedPatient.getSexe())) {
                masculin.setSelected(true);
            } else {
                feminin.setSelected(true);
            }
        }
    }

    @FXML
    private void handleUpdate() {
        if (selectedPatient != null) {
            try {
                Connection conn = DatabaseConnection.getConnection();
                if (conn != null) {
                    String sql = "UPDATE patient SET nomfamille = ?, prenom = ?, sexe = ?, tel = ? WHERE cin = ?";
                    PreparedStatement statement = conn.prepareStatement(sql);
                    statement.setString(1, nomfamille.getText());
                    statement.setString(2, prenom.getText());
                    statement.setString(3, masculin.isSelected() ? "Masculin" : "Féminin");
                    statement.setString(4, tel.getText());
                    statement.setString(5, cin.getText());
                    int rowsAffected = statement.executeUpdate();
                    if (rowsAffected > 0) {
                        selectedPatient.setNom(nomfamille.getText());
                        selectedPatient.setPrenom(prenom.getText());
                        selectedPatient.setTelephone(tel.getText());
                        selectedPatient.setSexe(masculin.isSelected() ? "Masculin" : "Féminin");
                        tablepatients.refresh();
                    }
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune sélection");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un patient pour mettre à jour.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleDelete() {
        Patient selectedPatient = tablepatients.getSelectionModel().getSelectedItem();
        if (selectedPatient != null) {
            try {
                Connection conn = DatabaseConnection.getConnection();
                if (conn != null) {
                    String sql = "DELETE FROM patient WHERE cin = ?";
                    PreparedStatement statement = conn.prepareStatement(sql);
                    statement.setString(1, selectedPatient.getCin());
                    int rowsAffected = statement.executeUpdate();
                    if (rowsAffected > 0) {
                        tablepatients.getItems().remove(selectedPatient);
                    }
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune sélection");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un patient pour supprimer.");
            alert.showAndWait();
        }
    }

    private boolean validateInput() {
        return !cin.getText().isEmpty() && !nomfamille.getText().isEmpty() &&
                !prenom.getText().isEmpty() && !tel.getText().isEmpty() &&
                (masculin.isSelected() || feminin.isSelected());
    }

    @FXML
    private void handleSearch() {
        String searchText = searchNom.getText().trim();
        tablepatients.getItems().clear();

        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null) {
                String sql;
                PreparedStatement statement;

                if (searchText.isEmpty()) {
                    sql = "SELECT * FROM patient";
                    statement = conn.prepareStatement(sql);
                } else {
                    sql = "SELECT * FROM patient WHERE nomfamille LIKE ?";
                    statement = conn.prepareStatement(sql);
                    statement.setString(1, "%" + searchText + "%");
                }

                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    Patient patient = new Patient(
                            resultSet.getString("nomfamille"),
                            resultSet.getString("prenom"),
                            resultSet.getString("tel")
                    );
                    patient.setSexe(resultSet.getString("sexe"));
                    patient.setCin(resultSet.getString("cin"));
                    tablepatients.getItems().add(patient);
                }

                resultSet.close();
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
