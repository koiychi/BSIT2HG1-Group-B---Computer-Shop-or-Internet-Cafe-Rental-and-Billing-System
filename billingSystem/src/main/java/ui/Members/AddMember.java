/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package ui.Members;

/**
 *
 * @author Cals
 */
public class AddMember extends javax.swing.JDialog {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(AddMember.class.getName());

    /**
     * Creates new form AddMember
     */
    public AddMember(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        setUndecorated(true);
        initComponents();
        setupLogic();
        applyTheme();
    }

    private final service.MembershipService membershipService = new service.MembershipService();
    private static final String LAST_HINT = "Enter Last Name...";
    private static final String FIRST_HINT = "Enter First Name...";
    private static final String USER_HINT = "Enter Username...";
    private static final String PASS_HINT = "Enter Password...";
    private javax.swing.JPasswordField passwordField;
    private boolean passwordHint = true;     // true while the hint text is showing
    private boolean added = false;
    private String addedUsername = null;

    public boolean isAdded() {
        return added;
    }

    public String getAddedUsername() {
        return addedUsername;
    }

    private void setupLogic() {
        hint(lastNField, LAST_HINT);
        hint(firstNField, FIRST_HINT);
        hint(usernameField, USER_HINT);

        // swap the password text box for a password field with the same look, so typing is hidden
        passwordField = new javax.swing.JPasswordField();
        passwordField.setBorder(firstNField1.getBorder());
        passwordField.setFont(firstNField1.getFont());
        final char hiddenChar = passwordField.getEchoChar();
        passwordField.setEchoChar((char) 0);      // the hint shows as normal text
        passwordField.setText(PASS_HINT);
        ((javax.swing.GroupLayout) CustomerNamePanel1.getLayout()).replace(firstNField1, passwordField);

        passwordField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (passwordHint) {
                    passwordField.setText("");
                    passwordField.setEchoChar(hiddenChar);   // from here on the typing is hidden
                    passwordHint = false;
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (passwordField.getPassword().length == 0) {
                    passwordField.setEchoChar((char) 0);
                    passwordField.setText(PASS_HINT);
                    passwordHint = true;
                }
            }
        });
    }
        // COLORR
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
        paintTree(body, BG);
        javax.swing.JPanel root = new javax.swing.JPanel(new java.awt.BorderLayout());
        root.setBackground(BG);
        root.add(header, java.awt.BorderLayout.NORTH);
        root.add(body, java.awt.BorderLayout.CENTER);
        setContentPane(root);

        // orange border around the whole window
        getRootPane().setBorder(javax.swing.BorderFactory.createLineBorder(ORANGE, 2));

        // the two boxes: slightly lighter card
        java.awt.Color card = new java.awt.Color(0x15151B);
        javax.swing.JPanel[] panels = {CustomerNamePanel, CustomerNamePanel1};
        for (javax.swing.JPanel p : panels) {
            p.setBackground(card);
            p.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(70, 70, 70), 1));
        }

        // light input fields
        javax.swing.JTextField[] fields = {lastNField, firstNField, usernameField, passwordField};
        for (javax.swing.JTextField f : fields) {
            f.setBackground(new java.awt.Color(0xF2F2F2));
            f.setForeground(java.awt.Color.BLACK);
            f.setCaretColor(java.awt.Color.BLACK);
            f.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                    javax.swing.BorderFactory.createLineBorder(new java.awt.Color(150, 150, 150), 1),
                    javax.swing.BorderFactory.createEmptyBorder(4, 6, 4, 6)));
        }

        // orange rounded button
        proceedBtn.setPreferredSize(null);   // let the layout use the real button height
        proceedBtn.setForeground(java.awt.Color.WHITE);
        proceedBtn.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        proceedBtn.setContentAreaFilled(false);
        proceedBtn.setOpaque(false);
        proceedBtn.setBorderPainted(false);
        proceedBtn.setFocusPainted(false);
        proceedBtn.setBorder(javax.swing.BorderFactory.createEmptyBorder(6, 10, 6, 10));
        proceedBtn.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        proceedBtn.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(java.awt.Graphics g, javax.swing.JComponent c) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
                        java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(ORANGE);
                g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 8, 8);
                g2.dispose();
                super.paint(g, c);
            }
        });

        pack();   // grow to fit everything, header included
    }

    // gives every panel and label the dark theme colors
    private void paintTree(java.awt.Container container, java.awt.Color bg) {
        for (java.awt.Component c : container.getComponents()) {
            if (c instanceof javax.swing.JPanel) {
                c.setBackground(bg);
            } else if (c instanceof javax.swing.JLabel) {
                c.setForeground(java.awt.Color.WHITE);
            }
            if (c instanceof java.awt.Container) {
                paintTree((java.awt.Container) c, bg);
            }
        }
    }

    // clears the hint text when the box is clicked, puts it back if left empty
    private void hint(final javax.swing.JTextField f, final String hintText) {
        f.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (f.getText().equals(hintText)) {
                    f.setText("");
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (f.getText().trim().isEmpty()) {
                    f.setText(hintText);
                }
            }
        });
    }
    

    // the typed value, or "" if the box still shows the hint
    private String valueOf(javax.swing.JTextField f, String hintText) {
        String t = f.getText().trim();
        return t.equals(hintText) ? "" : t;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        CustomerNamePanel = new javax.swing.JPanel();
        customerNameLbl = new javax.swing.JLabel();
        lastNLbl = new javax.swing.JLabel();
        lastNField = new javax.swing.JTextField();
        firstNLbl = new javax.swing.JLabel();
        firstNField = new javax.swing.JTextField();
        CustomerNamePanel1 = new javax.swing.JPanel();
        customerCredentialLbl = new javax.swing.JLabel();
        usernameLbl = new javax.swing.JLabel();
        usernameField = new javax.swing.JTextField();
        passwordLbl = new javax.swing.JLabel();
        firstNField1 = new javax.swing.JTextField();
        proceedBtn = new javax.swing.JButton();
        titleLbl = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        CustomerNamePanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(38, 42, 52)));

        customerNameLbl.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        customerNameLbl.setText("Customer Name");

        lastNLbl.setText("Last Name");

        lastNField.setText("Enter Last Name...");
        lastNField.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        lastNField.setPreferredSize(new java.awt.Dimension(73, 26));
        lastNField.addActionListener(this::lastNFieldActionPerformed);

        firstNLbl.setText("First Name");

        firstNField.setText("Enter First Name...");
        firstNField.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        firstNField.setPreferredSize(new java.awt.Dimension(73, 26));
        firstNField.addActionListener(this::firstNFieldActionPerformed);

        javax.swing.GroupLayout CustomerNamePanelLayout = new javax.swing.GroupLayout(CustomerNamePanel);
        CustomerNamePanel.setLayout(CustomerNamePanelLayout);
        CustomerNamePanelLayout.setHorizontalGroup(
            CustomerNamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CustomerNamePanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(CustomerNamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(customerNameLbl)
                    .addGroup(CustomerNamePanelLayout.createSequentialGroup()
                        .addGroup(CustomerNamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lastNField, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lastNLbl))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(CustomerNamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(firstNLbl)
                            .addComponent(firstNField, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)))))
        );
        CustomerNamePanelLayout.setVerticalGroup(
            CustomerNamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CustomerNamePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(customerNameLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(CustomerNamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lastNLbl)
                    .addComponent(firstNLbl))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(CustomerNamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(firstNField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lastNField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        CustomerNamePanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(38, 42, 52)));

        customerCredentialLbl.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        customerCredentialLbl.setText("Customer Credentials");

        usernameLbl.setText("Username");

        usernameField.setText("Enter Username...");
        usernameField.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        usernameField.setPreferredSize(new java.awt.Dimension(73, 26));
        usernameField.addActionListener(this::usernameFieldActionPerformed);

        passwordLbl.setText("Password");

        firstNField1.setText("Enter Password...");
        firstNField1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        firstNField1.setPreferredSize(new java.awt.Dimension(73, 26));
        firstNField1.addActionListener(this::firstNField1ActionPerformed);

        javax.swing.GroupLayout CustomerNamePanel1Layout = new javax.swing.GroupLayout(CustomerNamePanel1);
        CustomerNamePanel1.setLayout(CustomerNamePanel1Layout);
        CustomerNamePanel1Layout.setHorizontalGroup(
            CustomerNamePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CustomerNamePanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(CustomerNamePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(customerCredentialLbl)
                    .addGroup(CustomerNamePanel1Layout.createSequentialGroup()
                        .addGroup(CustomerNamePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(usernameField, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(usernameLbl))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(CustomerNamePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(passwordLbl)
                            .addComponent(firstNField1, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)))))
        );
        CustomerNamePanel1Layout.setVerticalGroup(
            CustomerNamePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CustomerNamePanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(customerCredentialLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(CustomerNamePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(usernameLbl)
                    .addComponent(passwordLbl))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(CustomerNamePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(firstNField1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(usernameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        proceedBtn.setBackground(new java.awt.Color(255, 102, 0));
        proceedBtn.setForeground(new java.awt.Color(255, 255, 255));
        proceedBtn.setText("+ Add and Top Up");
        proceedBtn.setPreferredSize(new java.awt.Dimension(75, 26));
        proceedBtn.addActionListener(this::proceedBtnActionPerformed);

        titleLbl.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        titleLbl.setText("Add Member");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(titleLbl)
                        .addContainerGap(243, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(proceedBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(CustomerNamePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(CustomerNamePanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titleLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(CustomerNamePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(CustomerNamePanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(proceedBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void proceedBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_proceedBtnActionPerformed
        String lastName = valueOf(lastNField, LAST_HINT);
        String firstName = valueOf(firstNField, FIRST_HINT);
        String username = valueOf(usernameField, USER_HINT);
        String password = passwordHint ? "" : new String(passwordField.getPassword());

        if (lastName.isEmpty() || firstName.isEmpty() || username.isEmpty() || password.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Please fill in all fields.",
                    "Missing Information", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!system.ShopManager.ensureShopOpen()) {
            return;   // the top up needs the shop to be open
        }
        // the member is saved with no credit; the starting balance comes from the top up
        if (!membershipService.addMemberCustomer(firstName, lastName, username, password, 0.0)) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Could not add member. Username may already exist.",
                    "Database Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        added = true;
        addedUsername = username;
        // the Top Up dialog opens here (next step)
        dispose();
        
        java.awt.Window owner = getOwner();
        java.awt.Frame frame = owner instanceof java.awt.Frame ? (java.awt.Frame) owner : null;
        int customerId = membershipService.getCustomerIdByUsername(username);
        logic.MemberCustomer member = membershipService.findMember(username);
        if (member == null || customerId <= 0) {
            javax.swing.JOptionPane.showMessageDialog(frame,
                    "The member was added, but could not be found for the top up. Use Top Up from the dashboard.",
                    "Top Up", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        ui.Shop.TopUp topUp = new ui.Shop.TopUp(frame, true);
        topUp.setAccount(username, membershipService.getCustomerFullName(customerId));
        topUp.setCurrentBalance(java.math.BigDecimal.valueOf(member.getCreditAmount())
                .setScale(2, java.math.RoundingMode.HALF_UP));
        topUp.setCommitter(paidBy -> {
            try {
                membershipService.addCredit(customerId, topUp.getTotal().doubleValue());
                return null;   // success
            } catch (java.sql.SQLException | IllegalArgumentException ex) {
                return ex.getMessage();
            }
        });
        topUp.setVisible(true);
        if (topUp.isPaid()) {
            ui.Shop.PaymentUtil.firePaymentSaved();   // refresh the dashboard and transactions
        }
    }//GEN-LAST:event_proceedBtnActionPerformed

    private void firstNFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_firstNFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_firstNFieldActionPerformed

    private void firstNField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_firstNField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_firstNField1ActionPerformed

    private void lastNFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lastNFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lastNFieldActionPerformed

    private void usernameFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_usernameFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_usernameFieldActionPerformed

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
                AddMember dialog = new AddMember(new javax.swing.JFrame(), true);
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
    private javax.swing.JPanel CustomerNamePanel;
    private javax.swing.JPanel CustomerNamePanel1;
    private javax.swing.JLabel customerCredentialLbl;
    private javax.swing.JLabel customerNameLbl;
    private javax.swing.JTextField firstNField;
    private javax.swing.JTextField firstNField1;
    private javax.swing.JLabel firstNLbl;
    private javax.swing.JTextField lastNField;
    private javax.swing.JLabel lastNLbl;
    private javax.swing.JLabel passwordLbl;
    private javax.swing.JButton proceedBtn;
    private javax.swing.JLabel titleLbl;
    private javax.swing.JTextField usernameField;
    private javax.swing.JLabel usernameLbl;
    // End of variables declaration//GEN-END:variables
}
