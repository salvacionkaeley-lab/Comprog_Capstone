/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.swing;

/**
 *
 * @author kathe
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBconnection {
  private static final String URL = "jdbc:mysql://localhost:3306/medisee_db";
    private static final String USER = "root"; // Default XAMPP MySQL user
    private static final String PASS = "";     // Default XAMPP MySQL password is blank

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}