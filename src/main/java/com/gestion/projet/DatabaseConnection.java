package com.gestion.projet;
import  java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    public Connection databaseLink;
    private static String DB_Url = "jdbc:mysql://localhost:3306/projet_db?user=root&password=";
    public static Connection cnx = null;

    public static Connection getConnection(){
        try {
            cnx = DriverManager.getConnection(DB_Url);

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
        return  cnx;

    }
}
