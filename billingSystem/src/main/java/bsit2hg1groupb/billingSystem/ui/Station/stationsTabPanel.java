/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package bsit2hg1groupb.billingSystem.ui.Station;

/**
 *
 * @author koi
 */

import bsit2hg1groupb.billingSystem.system.Station;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.BoxLayout;

public class stationsTabPanel extends javax.swing.JPanel {
    private final List<Station> stations = new ArrayList<>();

    /**
     * Creates new form stationsTabPanel
     */
    
    
    private void refreshStationCards() {
        MidPanel.removeAll();
        MidPanel.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // REGULAR PC SECTION
        JLabel regularLabel = new JLabel("Regular PCs");
        regularLabel.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        regularLabel.setFont(
                regularLabel.getFont().deriveFont(java.awt.Font.BOLD, 20f)
        );
        regularLabel.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 10, 15)
        );

        JPanel regularPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        regularPanel.setBorder(
                BorderFactory.createEmptyBorder(0, 15, 20, 15)
        );

        // GAMING PC SECTION
        JLabel gamingLabel = new JLabel("Gaming PCs");
        gamingLabel.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        gamingLabel.setFont(
                gamingLabel.getFont().deriveFont(java.awt.Font.BOLD, 20f)
        );
        gamingLabel.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 10, 15)
        );

        JPanel gamingPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        gamingPanel.setBorder(
                BorderFactory.createEmptyBorder(0, 15, 15, 15)
        );

        
        for (Station station : stations) {

            JPanel card = new JPanel(new java.awt.GridBagLayout());

            card.setPreferredSize(new Dimension(180, 100));
            card.setMinimumSize(new Dimension(180, 100));
            card.setMaximumSize(new Dimension(180, 100));

            card.setBackground(
                    javax.swing.UIManager.getColor("Panel.background")
            );

            card.setBorder(
                    BorderFactory.createLineBorder(Color.LIGHT_GRAY)
            );

            // Load the PC icon
            java.net.URL iconURL
                    = getClass().getResource("/icons/pc.png");

            System.out.println("Icon URL: " + iconURL);

            if (iconURL == null) {
                System.out.println("ERROR: pc.png not found!");
            } else {
                ImageIcon pcImage = new ImageIcon("images/pc.png");

                Image scaledImage = pcImage.getImage().getScaledInstance(
                        40, 40, Image.SCALE_SMOOTH
                );

                JLabel pcIcon = new JLabel(new ImageIcon(scaledImage));

                card.add(pcIcon);
            }

            // PC ID
            JLabel idLabel = new JLabel(station.getStationID());

            idLabel.setFont(
                    idLabel.getFont().deriveFont(
                            java.awt.Font.BOLD, 16f
                    )
            );

            java.awt.GridBagConstraints gbcID
                    = new java.awt.GridBagConstraints();

            gbcID.gridx = 0;
            gbcID.gridy = 1;
            gbcID.insets = new java.awt.Insets(3, 0, 5, 0);

            card.add(idLabel, gbcID);

            // Place each card in the correct section
            if (station.getClass().getSimpleName().equals("GamingStation")) {
                gamingPanel.add(card);
            } else {
                regularPanel.add(card);
            }
        }


        // Regular PC heading aligned to the left
        JPanel regularHeadingPanel = new JPanel(new BorderLayout());
        regularHeadingPanel.add(regularLabel, BorderLayout.WEST);
        regularHeadingPanel.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);

        mainPanel.add(regularHeadingPanel);
        mainPanel.add(regularPanel);

// Gaming PC heading aligned to the left
        JPanel gamingHeadingPanel = new JPanel(new BorderLayout());
        gamingHeadingPanel.add(gamingLabel, BorderLayout.WEST);
        gamingHeadingPanel.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);

        mainPanel.add(gamingHeadingPanel);
        mainPanel.add(gamingPanel);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        );
        scrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        MidPanel.add(scrollPane, BorderLayout.CENTER);
        MidPanel.revalidate();
        MidPanel.repaint();
    }

private void initializeStations() {
    stations.clear();

    // Create 5 Regular PCs: PC-01 to PC-05
    for (int i = 1; i <= 5; i++) {
        String id = String.format("PC-%02d", i);

        Station station =
                new bsit2hg1groupb.billingSystem.system.RegularStation(
                        id, 15.0, true, true,
                        new ArrayList<>(), false
                );

        stations.add(station);
    }

    // Create 5 Gaming PCs: PC-06 to PC-10
    for (int i = 6; i <= 10; i++) {
        String id = String.format("PC-%02d", i);

        Station station =
                new bsit2hg1groupb.billingSystem.system.GamingStation(
                        id, 30.0, true, true,
                        new ArrayList<>()
                );

        stations.add(station);
    }

    refreshStationCards();
}

    

    public stationsTabPanel() {
        initComponents();
        initializeStations();
    }

    /**
     * This method is called from within the constructor to initialize the form.
        * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        headingPanel = new javax.swing.JPanel();
        leftPanel = new javax.swing.JPanel();
        text = new javax.swing.JLabel();
        rightPanel = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        MidPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();

        setLayout(new java.awt.BorderLayout());

        headingPanel.setLayout(new java.awt.GridLayout(1, 0));

        text.setFont(new java.awt.Font("Inter", 0, 24)); // NOI18N

        javax.swing.GroupLayout leftPanelLayout = new javax.swing.GroupLayout(leftPanel);
        leftPanel.setLayout(leftPanelLayout);
        leftPanelLayout.setHorizontalGroup(
            leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(leftPanelLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(text)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        leftPanelLayout.setVerticalGroup(
            leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(leftPanelLayout.createSequentialGroup()
                .addContainerGap(129, Short.MAX_VALUE)
                .addComponent(text))
        );

        headingPanel.add(leftPanel);

        jButton3.setText("+ Add Station");
        jButton3.addActionListener(this::jButton3ActionPerformed);

        javax.swing.GroupLayout rightPanelLayout = new javax.swing.GroupLayout(rightPanel);
        rightPanel.setLayout(rightPanelLayout);
        rightPanelLayout.setHorizontalGroup(
            rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, rightPanelLayout.createSequentialGroup()
                .addContainerGap(294, Short.MAX_VALUE)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );
        rightPanelLayout.setVerticalGroup(
            rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rightPanelLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(46, Short.MAX_VALUE))
        );

        headingPanel.add(rightPanel);

        add(headingPanel, java.awt.BorderLayout.NORTH);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/computer_30dp_FFFFFF_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N

        javax.swing.GroupLayout MidPanelLayout = new javax.swing.GroupLayout(MidPanel);
        MidPanel.setLayout(MidPanelLayout);
        MidPanelLayout.setHorizontalGroup(
            MidPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MidPanelLayout.createSequentialGroup()
                .addGap(143, 143, 143)
                .addComponent(jLabel1)
                .addContainerGap(767, Short.MAX_VALUE))
        );
        MidPanelLayout.setVerticalGroup(
            MidPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MidPanelLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel1)
                .addContainerGap(115, Short.MAX_VALUE))
        );

        add(MidPanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        AddStation dialog = new AddStation();

        dialog.setOnStationSaved(station -> {
            stations.add(station);
            refreshStationCards();

            System.out.println(
                    "Station added: " + station.getStationID()
            );
        });

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }//GEN-LAST:event_jButton3ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel MidPanel;
    private javax.swing.JPanel headingPanel;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel leftPanel;
    private javax.swing.JPanel rightPanel;
    private javax.swing.JLabel text;
    // End of variables declaration//GEN-END:variables
}
