package com.gestion.projet;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
   @FXML
   private Label loginMessageLabel;
   @FXML
   private PasswordField password;
   @FXML
   private TextField name;
   @FXML
   private Button login;
   @FXML
   private Button register;

   @Override
   public void initialize(URL url, ResourceBundle resourceBundle) {
      login.setOnAction(event -> {
         if (!name.getText().isEmpty() && !password.getText().isEmpty()) {
            validateLogin();
         } else {
            showAlert("Input Required", "Please enter UserName and Password");
         }
      });

      register.setOnAction(event -> openRegisterView());
   }

   public void validateLogin() {
      Connection conn = DatabaseConnection.getConnection();
      String verifyLogin = "SELECT * FROM login WHERE login= '" + name.getText() + "' AND password = '" + password.getText() + "'";
      try {
         Statement statement = conn.createStatement();
         ResultSet queryResult = statement.executeQuery(verifyLogin);

         if (queryResult.next()) {
            loginMessageLabel.setText("Congratulations!");

            // Change the scene using Main's static method
            Main.changeScene("accueil.fxml");
         } else {
            showAlert("Invalid credentials", "The username or password is incorrect. Please try again.");
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   private void openRegisterView() {
      try {
         FXMLLoader loader = new FXMLLoader(getClass().getResource("register-view.fxml"));
         Parent registerViewParent = loader.load();
         Scene registerViewScene = new Scene(registerViewParent);
         Stage stage = new Stage();
         stage.setScene(registerViewScene);
         stage.show();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   private void showAlert(String title, String message) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle(title);
      alert.setHeaderText(null);
      alert.setContentText(message);
      alert.showAndWait();
   }
}
