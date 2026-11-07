/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

package com.mycompany.swing;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class MainDashboard extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(MainDashboard.class.getName());


    
    // Custom Table Cell Renderer for Color-Coding Low Stock and Expiration
class MedicineTableRenderer extends javax.swing.table.DefaultTableCellRenderer {
    @Override
    public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value, 
            boolean isSelected, boolean hasFocus, int row, int column) {
        
        java.awt.Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        if (c instanceof javax.swing.JComponent) {
            ((javax.swing.JComponent) c).setOpaque(true);
        }
        
        if (!isSelected) {
            try {
                // Let's print out what row values we are reading
                String medName = table.getValueAt(row, 1).toString();
                int stock = Integer.parseInt(table.getValueAt(row, 3).toString());
                String expiryStr = table.getValueAt(row, 4).toString();
                
                java.time.LocalDate expiryDate = java.time.LocalDate.parse(expiryStr);
                java.time.LocalDate today = java.time.LocalDate.now();

                if (expiryDate.isBefore(today)) {
                    c.setBackground(new java.awt.Color(255, 180, 180)); // Red
                } else if (stock <= 10) {
                    c.setBackground(new java.awt.Color(255, 255, 120)); // Bright Yellow for visibility
                } else {
                    c.setBackground(new java.awt.Color(220, 245, 220)); // Soft Green
                }
            } catch (Exception e) {
                // If something fails, make it bright orange so we know an error happened!
                c.setBackground(new java.awt.Color(255, 200, 150)); 
            }
        } else {
            c.setBackground(table.getSelectionBackground());
        }
        return c;
    }
}
    public MainDashboard() {
        initComponents();
        for (int i = 0; i < tableInventory.getColumnCount(); i++) {
            tableInventory.getColumnModel().getColumn(i).setCellRenderer(new MedicineTableRenderer());
    }
}
    
    public MainDashboard(String fullName, String userRole) {
        initComponents();
            for (int i = 0; i < tableInventory.getColumnCount(); i++) {
    tableInventory.getColumnModel().getColumn(i).setCellRenderer(new MedicineTableRenderer());
}
        setLocationRelativeTo(null);
        lblWelcome.setText("Welcome, " + fullName + " | Role: " + userRole.toUpperCase());
    

    filterComboBox.addActionListener(e -> {
        String selected = (String) filterComboBox.getSelectedItem();
        javax.swing.table.TableRowSorter sorter = (javax.swing.table.TableRowSorter) tableInventory.getRowSorter();
        
        if (sorter == null) {
            sorter = new javax.swing.table.TableRowSorter(tableInventory.getModel());
            tableInventory.setRowSorter(sorter);
        }
        
        if ("Show All".equals(selected)) {
            sorter.setRowFilter(null);
        } else if ("Low Stock Only".equals(selected)) {
            sorter.setRowFilter(new javax.swing.RowFilter() {
                @Override
                public boolean include(Entry entry) {
                    try {
                        int stock = Integer.parseInt(entry.getStringValue(3));
                        return stock <= 10;
                    } catch (Exception ex) {
                        return false;
                    }
                }
            });
        } else if ("Expired Only".equals(selected)) {
            sorter.setRowFilter(new javax.swing.RowFilter() {
                @Override
                public boolean include(Entry entry) {
                    try {
                        String expiryStr = entry.getStringValue(4);
                        java.time.LocalDate expiryDate = java.time.LocalDate.parse(expiryStr);
                        return expiryDate.isBefore(java.time.LocalDate.now());
                    } catch (Exception ex) {
                        return false;
                    }
                }
            });
        }
    });
        
       // Add action for Head Nurse to open the add form
       if (userRole.equalsIgnoreCase("headnurse")) {
            JButton btnAddMedicine = new JButton("Add New Medicine");
            
            JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            panelButtons.add(btnAddMedicine);
            add(panelButtons, BorderLayout.SOUTH);

            btnAddMedicine.addActionListener(e -> {
                // Create and display the dialog explicitly
                AddMedicineDialog dialog = new AddMedicineDialog(this);
                dialog.setVisible(true);
                
                if (dialog.isSaved()) {
                    loadInventoryData();
                }
            });
        }

        loadInventoryData();
    }

    private void loadInventoryData() {
        javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(
            new String[]{"ID", "Medicine Name", "Category", "Stock Quantity", "Expiration Date"}, 0
        );
        tableInventory.setModel(model);

        try (java.sql.Connection conn = DBconnection.getConnection()) {
            String sql = "SELECT m.medicine_id, m.medicine_name, c.category_name, m.stock_quantity, m.expiration_date " +
                         "FROM medicines m JOIN categories c ON m.category_id = c.category_id";
            java.sql.Statement stmt = conn.createStatement();
            java.sql.ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("medicine_id"),
                    rs.getString("medicine_name"),
                    rs.getString("category_name"),
                    rs.getInt("stock_quantity"),
                    rs.getDate("expiration_date")
                });
            }
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error loading inventory: " + e.getMessage());
        }
        for (int i = 0; i < tableInventory.getColumnCount(); i++) {
        tableInventory.getColumnModel().getColumn(i).setCellRenderer(new MedicineTableRenderer());
    }

}

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tableInventory = new javax.swing.JTable();
        lblWelcome = new javax.swing.JLabel();
        btnRefresh = new javax.swing.JButton();
        btnAddMedicine = new javax.swing.JButton();
        filterComboBox = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tableInventory.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tableInventory);

        lblWelcome.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblWelcome.setText("Welcome");

        btnRefresh.setText("Refresh Inventory");
        btnRefresh.addActionListener(this::btnRefreshActionPerformed);

        btnAddMedicine.setText("Add New Medicine");
        btnAddMedicine.addActionListener(this::btnAddMedicineActionPerformed);

        filterComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Show All", "Low Stock Only", "Expired Only" }));
        filterComboBox.addActionListener(this::filterComboBoxActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 98, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(86, 86, 86))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(filterComboBox, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnAddMedicine, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnRefresh, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(77, 77, 77)
                .addComponent(lblWelcome, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnRefresh)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAddMedicine)
                    .addComponent(lblWelcome))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addComponent(filterComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void btnAddMedicineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddMedicineActionPerformed
        AddMedicineDialog dialog = new AddMedicineDialog(this);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            loadInventoryData();
        }
    }//GEN-LAST:event_btnAddMedicineActionPerformed

    private void filterComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filterComboBoxActionPerformed
String selected = (String) filterComboBox.getSelectedItem();
javax.swing.table.TableRowSorter sorter = (javax.swing.table.TableRowSorter) tableInventory.getRowSorter();

if (sorter == null) {
    sorter = new javax.swing.table.TableRowSorter(tableInventory.getModel());
    tableInventory.setRowSorter(sorter);
}

if ("Show All".equals(selected)) {
    sorter.setRowFilter(null);
} else if ("Low Stock Only".equals(selected)) {
    sorter.setRowFilter(new javax.swing.RowFilter() {
        @Override
        public boolean include(Entry entry) {
            try {
                int stock = Integer.parseInt(entry.getStringValue(3));
                return stock <= 10;
            } catch (Exception ex) {
                return false;
            }
        }
    });
} else if ("Expired Only".equals(selected)) {
    sorter.setRowFilter(new javax.swing.RowFilter() {
        @Override
        public boolean include(Entry entry) {
            try {
                String expiryStr = entry.getStringValue(4);
                java.time.LocalDate expiryDate = java.time.LocalDate.parse(expiryStr);
                return expiryDate.isBefore(java.time.LocalDate.now());
            } catch (Exception ex) {
                return false;
            }
        }
    });
}        // TODO add your handling code here:
    }//GEN-LAST:event_filterComboBoxActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new MainDashboard().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddMedicine;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JComboBox<String> filterComboBox;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblWelcome;
    private javax.swing.JTable tableInventory;
    // End of variables declaration//GEN-END:variables
}
