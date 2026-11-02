/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package system;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import service.SessionService;





public class ActiveSessionsPanel extends javax.swing.JPanel {
         private final SessionService sessionService = new SessionService();
         private final javax.swing.Timer refreshTimer;

         


         public ActiveSessionsPanel() {
                  initComponents();
                  loadActiveSessions();
                  refreshTimer = new javax.swing.Timer(1, event -> loadActiveSessions());
                  refreshTimer.start();
         }

         @Override
         public void removeNotify() {
                  refreshTimer.stop();
                  super.removeNotify();
         }

         




         // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
         private void initComponents() {

                  tableScrollPane = new javax.swing.JScrollPane();
                  activeSessionsTable = new javax.swing.JTable();

                  setLayout(new java.awt.BorderLayout());

                  activeSessionsTable.setModel(new DefaultTableModel(
                           new Object[][] {},
                  new String[]{"Session", "Customer", "Station", "Started", "Ends", "Time Left", "Action"}) {
                           @Override
                           public boolean isCellEditable(int row, int column) {
                                    return false;
                           }
                  });
                  activeSessionsTable.addMouseListener(new java.awt.event.MouseAdapter() {
                           @Override
                           public void mouseClicked(java.awt.event.MouseEvent event) {
                                    int row = activeSessionsTable.rowAtPoint(event.getPoint());
                                    int column = activeSessionsTable.columnAtPoint(event.getPoint());
                                    if (row >= 0 && column == 6) {
                                             stopSession(row);
                                    }
                           }
                  });
                  tableScrollPane.setViewportView(activeSessionsTable);
                  add(tableScrollPane, java.awt.BorderLayout.CENTER);
         }// </editor-fold>//GEN-END:initComponents

         private void loadActiveSessions() {
                  try {
                           int endedSessions = sessionService.endExpiredSessions();
                           if (endedSessions > 0) {
                                    ShopManager.refreshDashboard();
                           }
                  } catch (SQLException ex) {
                           showDatabaseError("Could not end expired sessions: " + ex.getMessage());
                           return;
                  }

                  String sql = "SELECT s.SessionID, "
                           + "c.FirstName || ' ' || c.LastName AS CustomerName, "
                           + "s.StationID, s.StartTime, s.EndTime, s.DurationMinutes "
                           + "FROM SESSIONS s "
                           + "JOIN CUSTOMER c ON c.CustomerID = s.CustomerID "
                           + "WHERE s.EndTime > datetime('now', 'localtime') "
                           + "ORDER BY (julianday(s.EndTime) - julianday('now', 'localtime')) ASC";

                  try (Connection connection = DatabaseManager.getConnection();
                           PreparedStatement statement = connection.prepareStatement(sql);
                           ResultSet result = statement.executeQuery()) {
                           DefaultTableModel model = (DefaultTableModel) activeSessionsTable.getModel();
                           model.setRowCount(0);
                           while (result.next()) {
                                    model.addRow(new Object[]{
                                             result.getInt("SessionID"),
                                             result.getString("CustomerName"),
                                             result.getString("StationID"),
                                             result.getString("StartTime"),
                                             result.getString("EndTime"),
                                             getTimeLeft(result.getString("EndTime")),
                                             "Stop"
                                    });
                           }
                  } catch (SQLException ex) {
                           showDatabaseError("Could not load active sessions: " + ex.getMessage());
                  }
         }

         private String getTimeLeft(String endTime) {
                  try {
                           java.time.LocalDateTime end = java.time.LocalDateTime.parse(
                                    endTime, java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                           long seconds = java.time.Duration.between(
                                    java.time.LocalDateTime.now(), end).getSeconds();
                           long minutes = seconds / 60;
                           long remainingSeconds = seconds % 60;
                           return String.format("%02d:%02d", minutes, remainingSeconds);
                  } catch (java.time.format.DateTimeParseException ex) {
                           return "Unknown";
                  }
         }

         private void stopSession(int row) {
                  int sessionId = (Integer) activeSessionsTable.getValueAt(row, 0);
                  int answer = JOptionPane.showConfirmDialog(
                           this,
                           "Stop session " + sessionId + " now?\nThe prepaid amount will not be refunded.",
                           "Stop Session",
                           JOptionPane.YES_NO_OPTION);
                  if (answer != JOptionPane.YES_OPTION) {
                           return;
                  }
                  try {
                           sessionService.endSession(sessionId);
                           loadActiveSessions();
                  } catch (SQLException | IllegalArgumentException ex) {
                           showDatabaseError("Could not stop session: " + ex.getMessage());
                  }
         }

         private void showDatabaseError(String message) {
                  JOptionPane.showMessageDialog(this, message, "Database Error", JOptionPane.ERROR_MESSAGE);
         }


         // Variables declaration - do not modify//GEN-BEGIN:variables
         private javax.swing.JTable activeSessionsTable;
         private javax.swing.JScrollPane tableScrollPane;
         // End of variables declaration//GEN-END:variables
}
