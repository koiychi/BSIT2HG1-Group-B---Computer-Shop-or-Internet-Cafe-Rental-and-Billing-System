/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package ui.Shop;

/**
 *
 * @author Cals
 */
public class SelectMemberTopUp extends javax.swing.JDialog {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(SelectMemberTopUp.class.getName());

    /**
     * Creates new form SelectMemberTopUp
     */
    private final java.util.List<logic.MemberCustomer> allMembers = new java.util.ArrayList<>();
    private final java.util.List<logic.MemberCustomer> shown = new java.util.ArrayList<>();
    private String selectedUsername = null;
    private final service.MembershipService membershipService = new service.MembershipService();
    public SelectMemberTopUp(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        setUndecorated(true);
        initComponents();
        setupLogic();
        setLocationRelativeTo(parent);
    }

    // the picked member's username, or null if the cashier cancelled
    public String getSelectedUsername() {
        return selectedUsername;
    }

    private void setupLogic() {
        setTitle("Select Member");
        setResizable(false);

        searchBar.setText("");   // clear the typed-in hint, use a real placeholder instead
        searchBar.putClientProperty("JTextField.placeholderText", "Search Member...");
        allMembers.addAll(membershipService.getMembers());

        // filter the combo box while typing
        searchBar.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override public void insertUpdate(javax.swing.event.DocumentEvent e) { applyFilter(); }
            @Override public void removeUpdate(javax.swing.event.DocumentEvent e) { applyFilter(); }
            @Override public void changedUpdate(javax.swing.event.DocumentEvent e) { applyFilter(); }
        });

        continueBtn.addActionListener(e -> confirmSelection());
        cancelBtn.addActionListener(e -> dispose());
        getRootPane().setDefaultButton(continueBtn);   // Enter = Continue
        getRootPane().registerKeyboardAction(e -> dispose(),
                javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0),
                javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);   // Esc = Cancel

        applyFilter();
        applyTheme();
    }

    private void applyFilter() {
        String key = searchBar.getText().trim().toLowerCase();
        shown.clear();
        javax.swing.DefaultComboBoxModel<String> model = new javax.swing.DefaultComboBoxModel<>();
        for (logic.MemberCustomer m : allMembers) {
            if (key.isEmpty() || m.getUsername().toLowerCase().contains(key)) {
                shown.add(m);
                model.addElement(m.getUsername() + "  -  PHP " + String.format("%.2f", m.getCreditAmount()));
            }
        }

        boolean any = !shown.isEmpty();
        if (!any) {
            model.addElement(allMembers.isEmpty() ? "No members in the database yet" : "No member matches your search");
        }
        memberBox.setModel(model);
        memberBox.setEnabled(any);
        continueBtn.setEnabled(any);
    }

    private void confirmSelection() {
        int i = memberBox.getSelectedIndex();
        if (i < 0 || i >= shown.size()) {
            return;
        }
        selectedUsername = shown.get(i).getUsername();
        dispose();
    }
    // COLOR!!
    private void applyTheme() {
        final java.awt.Color ORANGE = new java.awt.Color(0xFF6600);
        final java.awt.Color BG = new java.awt.Color(0x0B0B0F);

        // top bar with a close X and a thin white line under it
        javax.swing.JPanel header = new javax.swing.JPanel(new java.awt.BorderLayout());
        header.setBackground(BG);
        header.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, java.awt.Color.WHITE));
        javax.swing.JLabel closeLbl = new javax.swing.JLabel("X");
        closeLbl.setForeground(java.awt.Color.WHITE);
        closeLbl.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        closeLbl.setBorder(javax.swing.BorderFactory.createEmptyBorder(6, 10, 6, 12));
        closeLbl.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        closeLbl.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) { dispose(); }
        });
        header.add(closeLbl, java.awt.BorderLayout.EAST);

        // put the header on top of the existing content
        java.awt.Container body = getContentPane();
        body.setBackground(BG);
        paintTree(body, BG);
        javax.swing.JPanel root = new javax.swing.JPanel(new java.awt.BorderLayout());
        root.setBackground(BG);
        root.add(header, java.awt.BorderLayout.NORTH);
        root.add(body, java.awt.BorderLayout.CENTER);
        setContentPane(root);

        // orange border
        getRootPane().setBorder(javax.swing.BorderFactory.createLineBorder(ORANGE, 2));

        // the "Select the member to top up" card (same look as the total amount box in TopUp)
        jPanel2.setBackground(new java.awt.Color(0x15151B));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(70, 70, 70), 1));

        // light input fields
        searchBar.setBackground(new java.awt.Color(0xF2F2F2));
        searchBar.setForeground(java.awt.Color.BLACK);
        searchBar.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(new java.awt.Color(150, 150, 150), 1),
                javax.swing.BorderFactory.createEmptyBorder(4, 6, 4, 6)));
        memberBox.setBackground(new java.awt.Color(0xF2F2F2));
        memberBox.setForeground(java.awt.Color.BLACK);

        // orange rounded buttons
        styleOrangeButton(continueBtn, ORANGE);
        styleOrangeButton(cancelBtn, ORANGE);

        pack();   // grow the window to fit everything, header included
    }

    // orange rounded button (grey while disabled)
    private void styleOrangeButton(javax.swing.JButton button, java.awt.Color orange) {
        button.setForeground(java.awt.Color.WHITE);
        button.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 18, 8, 18));
        button.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        button.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(java.awt.Graphics g, javax.swing.JComponent c) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
                        java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(c.isEnabled() ? orange : new java.awt.Color(0x3A3A40));
                g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 8, 8);
                g2.dispose();
                super.paint(g, c);
            }
        });
    }

    // gives every panel, label, and radio button the dark theme colors
    private void paintTree(java.awt.Container container, java.awt.Color bg) {
        for (java.awt.Component c : container.getComponents()) {
            if (c instanceof javax.swing.JPanel) {
                c.setBackground(bg);
            } else if (c instanceof javax.swing.JLabel) {
                c.setForeground(java.awt.Color.WHITE);
            } else if (c instanceof javax.swing.JRadioButton) {
                c.setForeground(java.awt.Color.WHITE);
                c.setBackground(bg);
            }
            if (c instanceof java.awt.Container) {
                paintTree((java.awt.Container) c, bg);
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        searchLbl = new javax.swing.JLabel();
        searchBar = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        titleLbl = new javax.swing.JLabel();
        memberBox = new javax.swing.JComboBox<>();
        jPanel3 = new javax.swing.JPanel();
        continueBtn = new javax.swing.JButton();
        cancelBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        searchLbl.setText("Search:");

        searchBar.setText("Search Member....");
        searchBar.setAutoscrolls(false);
        searchBar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true));
        searchBar.addActionListener(this::searchBarActionPerformed);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(searchBar, javax.swing.GroupLayout.PREFERRED_SIZE, 365, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(searchLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(searchBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(11, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(38, 42, 52)));

        titleLbl.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        titleLbl.setText("Select the member to top up:");

        memberBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        memberBox.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true));
        memberBox.addActionListener(this::memberBoxActionPerformed);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(memberBox, javax.swing.GroupLayout.PREFERRED_SIZE, 341, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(titleLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titleLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(memberBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        continueBtn.setBackground(new java.awt.Color(255, 102, 0));
        continueBtn.setForeground(new java.awt.Color(255, 255, 255));
        continueBtn.setText("Continue");

        cancelBtn.setBackground(new java.awt.Color(255, 102, 0));
        cancelBtn.setForeground(new java.awt.Color(255, 255, 255));
        cancelBtn.setText("Cancel");
        cancelBtn.addActionListener(this::cancelBtnActionPerformed);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cancelBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(continueBtn))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(continueBtn)
                    .addComponent(cancelBtn))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 12, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void searchBarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchBarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchBarActionPerformed

    private void memberBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_memberBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_memberBoxActionPerformed

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cancelBtnActionPerformed

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

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                SelectMemberTopUp dialog = new SelectMemberTopUp(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelBtn;
    private javax.swing.JButton continueBtn;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JComboBox<String> memberBox;
    private javax.swing.JTextField searchBar;
    private javax.swing.JLabel searchLbl;
    private javax.swing.JLabel titleLbl;
    // End of variables declaration//GEN-END:variables
}
