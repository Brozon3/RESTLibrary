package com.example.restlibrary;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public static Connection getDatabaseConnection() throws SQLException {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.format("Error: %s\n%s", e.getCause(), e.getMessage());
        }
        return DriverManager.getConnection("jdbc:mariadb://localhost:3300/books", "root", "password");
    }
}