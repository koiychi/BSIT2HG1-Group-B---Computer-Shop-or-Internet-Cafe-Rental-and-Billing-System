/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bsit2hg1groupb.billingSystem;

/**
 *
 * @author koi
 */
import com.formdev.flatlaf.FlatDarkLaf;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.Color;
import javax.swing.UIManager;

public class appUIManager {

    private static AdminLogin loginFrame;
    private static AdminAppWindow appWindow;

    public static void main(String[] args) {

        try {
            java.awt.GraphicsEnvironment ge = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment();

            String[] fontPaths = {
                "/fonts/GoogleSans-Regular.ttf",
                "/fonts/GoogleSans-Medium.ttf",
                "/fonts/GoogleSans-SemiBold.ttf",
                "/fonts/GoogleSans-Bold.ttf"
            };

            java.awt.Font appFont = null;

            for (String path : fontPaths) {
                try (java.io.InputStream is = AdminAppWindow.class.getResourceAsStream(path)) {
                    if (is != null) {
                        java.awt.Font font = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, is);
                        ge.registerFont(font);

                        if (path.contains("Regular")) {
                            appFont = font.deriveFont(java.awt.Font.PLAIN, 14f);
                        }
                        System.out.println("Registered font: " + font.getFontName());
                    } else {
                        System.err.println("Not found: " + path);
                    }
                } catch (Exception ex) {
                    System.err.println("Error loading " + path + ": " + ex.getMessage());
                }
            }

            if (appFont == null) {
                appFont = new java.awt.Font("Google Sans", java.awt.Font.PLAIN, 14);
            }

            javax.swing.UIManager.put("defaultFont", appFont);

            com.formdev.flatlaf.FlatDarkLaf.setup();

            javax.swing.UIManager.put("defaultFont", appFont);
            javax.swing.UIManager.put("Button.font", appFont);
            javax.swing.UIManager.put("Label.font", appFont);
            javax.swing.UIManager.put("TabbedPane.font", appFont);
            javax.swing.UIManager.put("Table.font", appFont);
            javax.swing.UIManager.put("TableHeader.font", appFont);
            javax.swing.UIManager.put("TextField.font", appFont);
            javax.swing.UIManager.put("PasswordField.font", appFont);
            javax.swing.UIManager.put("TextArea.font", appFont);
            javax.swing.UIManager.put("ComboBox.font", appFont);
            javax.swing.UIManager.put("CheckBox.font", appFont);
            javax.swing.UIManager.put("RadioButton.font", appFont);
            UIManager.put("TabbedPane.selectedForeground", Color.decode("#FF6600"));
            UIManager.put("TabbedPane.selectedBackground", Color.decode("#1E293B")); // was "TabbedPane.selected"
            UIManager.put("TabbedPane.background", Color.decode("#0F172A"));         // was "TabbedPane.tabAreaBackground"
            UIManager.put("TabbedPane.underlineColor", Color.decode("#FF6600"));
            UIManager.put("TabbedPane.inactiveUnderlineColor", Color.decode("#FF6600"));
            UIManager.put("TabbedPane.tabWidth", 0);
            UIManager.put("TabbedPane.tabAreaAlignment", "fill");   // stretch tab area to full width
            UIManager.put("TabbedPane.tabWidthMode", "equal");      // distribute width equally among tabs   
            UIManager.put("Panel.background", Color.decode("#0B0B0F"));

<<<<<<< Updated upstream:billingSystem/src/main/java/bsit2hg1groupb/billingSystem/UIManager.java
            com.formdev.flatlaf.FlatLaf.updateUI();

=======
//            com.formdev.flatlaf.FlatLaf.updateUI();
>>>>>>> Stashed changes:billingSystem/src/main/java/bsit2hg1groupb/billingSystem/appUIManager.java
        } catch (Exception e) {
            e.printStackTrace();
            com.formdev.flatlaf.FlatDarkLaf.setup();
        }

        EventQueue.invokeLater(() -> showLogin());
    }

    public static void showLogin() {
        if (appWindow != null) {
            appWindow.dispose();
            appWindow = null;
        }

        if (loginFrame == null) {
            loginFrame = new AdminLogin();
        }
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setVisible(true);
    }

    public static void showDashboard() {
        if (loginFrame != null) {
            loginFrame.dispose();
            loginFrame = null;
        }

        if (appWindow == null) {
            appWindow = new AdminAppWindow();
        }
        appWindow.setLocationRelativeTo(null);
        appWindow.setVisible(true);
    }

    public static void refreshTheme(JFrame frame) {
        if (frame != null) {
            SwingUtilities.updateComponentTreeUI(frame);
        }
    }
}
