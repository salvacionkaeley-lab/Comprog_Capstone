/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.swing;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class AddMedicineDialog extends JDialog {
    private JTextField txtName, txtStock, txtExpiry;
    private JComboBox<String> cmbCategory;
    private boolean isSaved = false;

    public AddMedicineDialog(JFrame parent) {
        super(parent, "Add New Medicine", true);
        
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new GridLayout(5, 2, 10, 10));

        // Ensure components are added
        add(new JLabel("  Medicine Name:"));
        txtName = new JTextField();
        add(txtName);

        add(new JLabel("  Category:"));
        cmbCategory = new JComboBox<>();
        loadCategories();
        add(cmbCategory);

        add(new JLabel("  Stock Quantity:"));
        txtStock = new JTextField();
        add(txtStock);

        add(new JLabel("  Expiration (YYYY-MM-DD):"));
        txtExpiry = new JTextField();
        add(txtExpiry);

        JButton btnSave = new JButton("Save Medicine");
        JButton btnCancel = new JButton("Cancel");
        add(btnSave);
        add(btnCancel);

        btnSave.addActionListener(e -> saveMedicine());
        btnCancel.addActionListener(e -> dispose());
    }

    private void loadCategories() {
        cmbCategory.removeAllItems(); // Clear any existing items first
        try (Connection conn = DBconnection.getConnection();
             Statement stmt = conn.createStatement();
             // DISTINCT prevents duplicates from showing up in the dropdown
             ResultSet rs = stmt.executeQuery("SELECT DISTINCT category_name FROM categories ORDER BY category_name ASC")) {
            
            while (rs.next()) {
                cmbCategory.addItem(rs.getString("category_name"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading categories: " + e.getMessage());
        }
    }

    private void saveMedicine() {
        String name = txtName.getText().trim();
        String category = (String) cmbCategory.getSelectedItem();
        String stockStr = txtStock.getText().trim();
        String expiry = txtExpiry.getText().trim();

        if (name.isEmpty() || stockStr.isEmpty() || expiry.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Connection conn = DBconnection.getConnection()) {
            // Get category_id based on selected category name
            PreparedStatement catStmt = conn.prepareStatement("SELECT category_id FROM categories WHERE category_name = ?");
            catStmt.setString(1, category);
            ResultSet rs = catStmt.executeQuery();
            
            if (rs.next()) {
                int categoryId = rs.getInt("category_id");

                PreparedStatement insertStmt = conn.prepareStatement(
                    "INSERT INTO medicines (medicine_name, category_id, stock_quantity, expiration_date) VALUES (?, ?, ?, ?)"
                );
                insertStmt.setString(1, name);
                insertStmt.setInt(2, categoryId);
                insertStmt.setInt(3, Integer.parseInt(stockStr));
                insertStmt.setString(4, expiry);
                insertStmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Medicine added successfully!");
                isSaved = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error: Selected category '" + category + "' was not found in the database.", "Category Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Stock quantity must be a valid number.", "Input Error", JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public boolean isSaved() {
        return isSaved;
    }
}
