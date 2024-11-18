package com.gestion.projet;

import com.mysql.cj.x.protobuf.MysqlxDatatypes;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GestionDoctor {

    @FXML
    private Button btnAdd;
    private com.gestion.projet.Doctor selectedDoctor;



    @FXML
    private Button btnDelete;

    @FXML
    private Button btnEdit;

    @FXML
    private TableView<com.gestion.projet.Doctor> tableDoctors;

    @FXML
    private TableColumn<com.gestion.projet.Doctor, MysqlxDatatypes.Scalar.String> cn;

    @FXML
    private TableColumn<com.gestion.projet.Doctor, MysqlxDatatypes.Scalar.String> cpn;
    @FXML
    private TableColumn<com.gestion.projet.Doctor, MysqlxDatatypes.Scalar.String> ct;


    @FXML
    private TextField brandname;

    @FXML
    private TextField productname;
    @FXML
    private TextField tel;


    private FileChooser fileChooser;

    // Méthode pour gérer l'ajout d'un doctor
    private ObservableList<com.gestion.projet.Doctor> cachedDoctors = FXCollections.observableArrayList();


    @FXML
    private void handleAdd() {
        String brandName = brandname.getText();
        String productName = productname.getText();
        String phoneNumber = tel.getText();

        if (!brandName.isEmpty() && !productName.isEmpty() && !phoneNumber.isEmpty()) {
            com.gestion.projet.Doctor newDoctor = new com.gestion.projet.Doctor(brandName, productName, phoneNumber);
            tableDoctors.getItems().add(newDoctor);
            cachedDoctors.add(newDoctor);


            try {
                Connection conn = com.gestion.projet.DatabaseConnection.getConnection();
                if (conn != null) {
                    String sql = "INSERT INTO doctor  VALUES (?, ?, ?)";
                    PreparedStatement statement = conn.prepareStatement(sql);
                    statement.setString(1, brandName);
                    statement.setString(2, productName);
                    statement.setString(3, phoneNumber);

                    int rowsAffected = statement.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Nouveau docteur inséré avec succès dans la base de données.");
                    } else {
                        System.out.println("Aucun docteur inséré dans la base de données.");
                    }
                    conn.close();
                } else {
                    System.out.println("La connexion à la base de données a échoué.");
                }
            } catch (SQLException e) {
                System.err.println("Erreur lors de l'insertion du docteur dans la base de données : " + e.getMessage());
                e.printStackTrace();
                // Gérez l'erreur d'insertion dans la base de données
            }

            // Effacer les champs après l'ajout
            brandname.clear();
            productname.clear();
            tel.clear();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Champs manquants");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs et spécifier un numéro de téléphone.");
            alert.showAndWait();
        }
    }

    @FXML
    private void loadDoctorsFromDatabase() {
        try {
            Connection conn = com.gestion.projet.DatabaseConnection.getConnection();
            if (conn != null) {
                String sql = "SELECT * FROM doctor";
                PreparedStatement statement = conn.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery();

                ObservableList<com.gestion.projet.Doctor> doctorsList = FXCollections.observableArrayList();

                while (resultSet.next()) {
                    String brandName = resultSet.getString("brandName");
                    String productName = resultSet.getString("productName");
                    String phoneNumber = resultSet.getString("phoneNumber");
                    com.gestion.projet.Doctor doctor = new com.gestion.projet.Doctor(brandName, productName, phoneNumber);
                    doctorsList.add(doctor);
                }


                tableDoctors.setItems(doctorsList);
               // cachedDoctors.clear();
               // cachedDoctors.addAll(doctorsList);

                resultSet.close();
                statement.close();
                conn.close();
            } else {
                System.out.println("La connexion à la base de données a échoué.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors du chargement des docteurs depuis la base de données : " + e.getMessage());
            e.printStackTrace();
        }
    }


    @FXML
    private void handleDelete() {
        com.gestion.projet.Doctor selectedDoctor = tableDoctors.getSelectionModel().getSelectedItem();
        if (selectedDoctor != null) {
            try {
                Connection conn = com.gestion.projet.DatabaseConnection.getConnection();
                if (conn != null) {
                    String sql = "DELETE FROM doctor WHERE brandName = ? AND productName = ? AND phoneNumber = ?";
                    PreparedStatement statement = conn.prepareStatement(sql);
                    statement.setString(1, selectedDoctor.getBrandName());
                    statement.setString(2, selectedDoctor.getProductName());
                    statement.setString(3, selectedDoctor.getPhoneNumber());
                    int rowsAffected = statement.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Docteur supprimé avec succès de la base de données.");
                        loadDoctorsFromDatabase();
                    } else {
                        System.out.println("Aucun docteur supprimé de la base de données.");
                    }
                    statement.close();
                    conn.close();
                } else {
                    System.out.println("La connexion à la base de données a échoué.");
                }
            } catch (SQLException e) {
                System.err.println("Erreur lors de la suppression du docteur de la base de données : " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune sélection");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un docteur à supprimer.");
            alert.showAndWait();
        }
    }
    @FXML
    private void handleEdit() {
        if (selectedDoctor != null) {
            String newBrandName = brandname.getText();
            String newProductName = productname.getText();
            String newPhoneNumber = tel.getText();
            if (!newBrandName.isEmpty() && !newProductName.isEmpty() && !newPhoneNumber.isEmpty()) {
                try {
                    Connection conn = com.gestion.projet.DatabaseConnection.getConnection();
                    if (conn != null) {
                        String sql = "UPDATE doctor SET brandName = ?, productName = ?, phoneNumber = ? WHERE brandName = ? AND productName = ? AND phoneNumber = ?";
                        PreparedStatement statement = conn.prepareStatement(sql);
                        statement.setString(1, newBrandName);
                        statement.setString(2, newProductName);
                        statement.setString(3, newPhoneNumber);
                        statement.setString(4, selectedDoctor.getBrandName());
                        statement.setString(5, selectedDoctor.getProductName());
                        statement.setString(6, selectedDoctor.getPhoneNumber());
                        int rowsAffected = statement.executeUpdate();
                        if (rowsAffected > 0) {
                            System.out.println("Docteur mis à jour avec succès dans la base de données.");
                            loadDoctorsFromDatabase();
                        } else {
                            System.out.println("Aucun docteur mis à jour dans la base de données.");
                        }
                        statement.close();
                        conn.close();
                    } else {
                        System.out.println("La connexion à la base de données a échoué.");
                    }
                } catch (SQLException e) {
                    System.err.println("Erreur lors de la mise à jour du docteur dans la base de données : " + e.getMessage());
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
            alert.setContentText("Veuillez sélectionner un docteur à mettre à jour.");
            alert.showAndWait();
        }
    }

    // Initialisation du contrôleur
    @FXML
    public void initialize() {
        // Initialiser les colonnes de la TableView avec les données du modèle Doctor
//        cn.setCellValueFactory(new PropertyValueFactory<>("brandName"));
//        cpn.setCellValueFactory(new PropertyValueFactory<>("productName"));
//        ct.setCellValueFactory(new PropertyValueFactory<>("phoneNumber")); // Liaison de la colonne avec la propriété du modèle Doctor

        // Initialiser les écouteurs d'événements
        ajouterColonnes();
        loadDoctorsFromDatabase();
        initializeListeners();

    }

    // Initialiser les écouteurs d'événements pour les boutons
    @FXML
    private void initializeListeners()
    {
        btnAdd.setOnAction(event -> handleAdd());
        btnDelete.setOnAction(event ->handleDelete());
        btnEdit.setOnAction(event -> handleEdit());
        tableDoctors.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedDoctor = newValue;
                brandname.setText(newValue.getBrandName());
                productname.setText(newValue.getProductName());
                tel.setText(newValue.getPhoneNumber());
            }
        });
    }
    private void ajouterColonnes() {
        tableDoctors.getColumns().clear();
        TableColumn<com.gestion.projet.Doctor, String> name = new TableColumn<>("brandName");
        TableColumn<com.gestion.projet.Doctor, String> productname = new TableColumn<>("productName");
        TableColumn<com.gestion.projet.Doctor, String> telCol = new TableColumn<>("phoneNumber");

        name.setCellValueFactory(new PropertyValueFactory<>("brandName"));
        productname.setCellValueFactory(new PropertyValueFactory<>("productName"));
        telCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));


        tableDoctors.getColumns().addAll(name, productname, telCol);
    }
}
