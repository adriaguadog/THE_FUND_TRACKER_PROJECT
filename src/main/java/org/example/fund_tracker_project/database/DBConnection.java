package org.example.fund_tracker_project.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    //pertenece a la clase
    private static Connection connection;


    private static void createConnection(){
        String URL = "jdbc:mysql://localhost:3306/fund_tracker";
        String user = "root";
        String password = "";
        try {
            connection= DriverManager.getConnection(URL, user, password);
        } catch (SQLException e) {
            System.out.println("error en la conexion a la base de datos");            }
    }

    //tiene que ser static para poder acceder
    //static porque voy a usar una variable estatica
    public static Connection getConnection() {
        if (connection == null) {
            createConnection();
        }
        return connection;
    }
}