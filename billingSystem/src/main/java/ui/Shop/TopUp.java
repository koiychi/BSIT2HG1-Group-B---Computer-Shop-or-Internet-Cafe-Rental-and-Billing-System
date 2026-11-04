/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package ui.Shop;

import java.math.BigDecimal;





public class TopUp extends javax.swing.JDialog {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(TopUp.class.getName());

    


public TopUp(java.awt.Frame parent, boolean modal) {
    super(parent, modal);
    setUndecorated(true);
    initComponents();
    setupLogic();
    setLocationRelativeTo(parent);
}
    private boolean paymentDone = false;
    private PaymentUtil.Committer committer;
    private BigDecimal currentBalance = BigDecimal.ZERO;

    public boolean isPaid() {
        return paymentDone;
    }

    public void setCommitter(PaymentUtil.Committer committer) {
        if (committer == null) {
            throw new IllegalArgumentException("Committer cannot be null.");
        }
        this.committer = committer;
    }

    public void setCurrentBalance(BigDecimal balance) {
        if (balance == null || balance.signum() < 0) {
            throw new IllegalArgumentException("Balance cannot be negative.");
        }
        currentBalance = balance;
    }

    public BigDecimal getTotal() {
        return total;
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        TotalAmountDue = new javax.swing.JPanel();
        totalTitleLbl = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        ModeOfPayment = new javax.swing.JPanel();
        payWithLbl = new javax.swing.JLabel();
        cashRb = new javax.swing.JRadioButton();
        creditRb = new javax.swing.JRadioButton();
        PayBill = new javax.swing.JPanel();
        titleLbl = new javax.swing.JLabel();
        detailsTitleLbl = new javax.swing.JLabel();
        usernameLbl = new javax.swing.JLabel();
        nameCustomerLbl = new javax.swing.JLabel();
        InputPayment = new javax.swing.JPanel();
        payField = new javax.swing.JTextField();
        changeLbl = new javax.swing.JLabel();
        ProceedPay = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        TotalAmountDue.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(38, 42, 52), 2, true));

        totalTitleLbl.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        totalTitleLbl.setText("Amount to Credit");

        jComboBox1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "50", "100", "200", "500", "1000" }));
        jComboBox1.addActionListener(this::jComboBox1ActionPerformed);

        javax.swing.GroupLayout TotalAmountDueLayout = new javax.swing.GroupLayout(TotalAmountDue);
        TotalAmountDue.setLayout(TotalAmountDueLayout);
        TotalAmountDueLayout.setHorizontalGroup(
            TotalAmountDueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TotalAmountDueLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(totalTitleLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(TotalAmountDueLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        TotalAmountDueLayout.setVerticalGroup(
            TotalAmountDueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TotalAmountDueLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(totalTitleLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox1)
                .addContainerGap())
        );

        payWithLbl.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        payWithLbl.setText("Pay With");

        cashRb.setText("Cash");
        cashRb.addActionListener(this::cashRbActionPerformed);

        creditRb.setText("Credit (PHP 1.00)");
        creditRb.addActionListener(this::creditRbActionPerformed);

        javax.swing.GroupLayout ModeOfPaymentLayout = new javax.swing.GroupLayout(ModeOfPayment);
        ModeOfPayment.setLayout(ModeOfPaymentLayout);
        ModeOfPaymentLayout.setHorizontalGroup(
            ModeOfPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ModeOfPaymentLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(payWithLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cashRb)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(creditRb, javax.swing.GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE)
                .addContainerGap())
        );
        ModeOfPaymentLayout.setVerticalGroup(
            ModeOfPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ModeOfPaymentLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ModeOfPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(payWithLbl)
                    .addComponent(cashRb)
                    .addComponent(creditRb))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        titleLbl.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        titleLbl.setText("Top Up Credit");

        detailsTitleLbl.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        detailsTitleLbl.setText("Infomation");

        usernameLbl.setText("@CalsLoveEni");

        nameCustomerLbl.setText("Carlo Sandoval");

        javax.swing.GroupLayout PayBillLayout = new javax.swing.GroupLayout(PayBill);
        PayBill.setLayout(PayBillLayout);
        PayBillLayout.setHorizontalGroup(
            PayBillLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PayBillLayout.createSequentialGroup()
                .addGroup(PayBillLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PayBillLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(PayBillLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(titleLbl)
                            .addComponent(detailsTitleLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(PayBillLayout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(PayBillLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(usernameLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(nameCustomerLbl, javax.swing.GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE))))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        PayBillLayout.setVerticalGroup(
            PayBillLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PayBillLayout.createSequentialGroup()
                .addComponent(titleLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(detailsTitleLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(usernameLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(nameCustomerLbl))
        );

        payField.setText("PHP 0.00");
        payField.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        payField.addActionListener(this::payFieldActionPerformed);

        changeLbl.setText("Change:");

        javax.swing.GroupLayout InputPaymentLayout = new javax.swing.GroupLayout(InputPayment);
        InputPayment.setLayout(InputPaymentLayout);
        InputPaymentLayout.setHorizontalGroup(
            InputPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(InputPaymentLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(InputPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(payField, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                    .addComponent(changeLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        InputPaymentLayout.setVerticalGroup(
            InputPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(InputPaymentLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(payField, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(changeLbl)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        ProceedPay.setBackground(new java.awt.Color(255, 102, 0));
        ProceedPay.setForeground(new java.awt.Color(255, 255, 255));
        ProceedPay.setText("Finish Payment");
        ProceedPay.addActionListener(this::ProceedPayActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ModeOfPayment, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(PayBill, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(InputPayment, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(ProceedPay))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(TotalAmountDue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(PayBill, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(TotalAmountDue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ModeOfPayment, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(InputPayment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ProceedPay)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ProceedPayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ProceedPayActionPerformed
        finishPayment();
    }//GEN-LAST:event_ProceedPayActionPerformed

    private void cashRbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cashRbActionPerformed
        payMethodChanged();
    }//GEN-LAST:event_cashRbActionPerformed

    private void creditRbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_creditRbActionPerformed
        payMethodChanged();
    }//GEN-LAST:event_creditRbActionPerformed

    private void payFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_payFieldActionPerformed
        
    }//GEN-LAST:event_payFieldActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        updateBill();
    }//GEN-LAST:event_jComboBox1ActionPerformed

private BigDecimal total = BigDecimal.ZERO;   

private void setupLogic() {
    
    usernameLbl.setText("(no member selected)");
    nameCustomerLbl.setText("");

    
    jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {
        "PHP 50.00", "PHP 100.00", "PHP 200.00", "PHP 500.00", "PHP 1000.00"
    }));
    jComboBox1.setSelectedItem("PHP 100.00");

    
    PaymentUtil.setupPaymentField(payField);

    
    javax.swing.ButtonGroup payGroup = new javax.swing.ButtonGroup();
    payGroup.add(cashRb);
    payGroup.add(creditRb);
    cashRb.setSelected(true);
    creditRb.setText("Credit (unavailable in top-up)");
    creditRb.setEnabled(false);   

    payField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
        @Override
        public void insertUpdate(javax.swing.event.DocumentEvent e) { updateChange(); }
        @Override
        public void removeUpdate(javax.swing.event.DocumentEvent e) { updateChange(); }
        @Override
        public void changedUpdate(javax.swing.event.DocumentEvent e) { updateChange(); }
    });

    updateBill();
    applyTheme();
}


public void setAccount(String username, String fullName) {
    usernameLbl.setText("@" + username);
    nameCustomerLbl.setText(fullName);
    pack();                              
    setLocationRelativeTo(getOwner());   
}


public void setAmountOptions(String... amounts) {
    jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(amounts));
    jComboBox1.setSelectedIndex(0);
    updateBill();
}


private void updateBill() {
    total = PaymentUtil.parseAmount((String) jComboBox1.getSelectedItem());
    updateChange();
}


private void updateChange() {
    BigDecimal paid = PaymentUtil.parseAmount(payField.getText());
    if (cashRb.isSelected() && paid.compareTo(total) >= 0) {
        changeLbl.setText("Change: " + PaymentUtil.money(paid.subtract(total)));
    } else {
        changeLbl.setText("Change:");
    }
}


private void payMethodChanged() {
    payField.requestFocusInWindow();
    updateChange();
}

//COLORSS!!
private void applyTheme() {
    final java.awt.Color ORANGE = new java.awt.Color(0xFF6600);
    final java.awt.Color BG = new java.awt.Color(0x0B0B0F);

    
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

    
    java.awt.Container body = getContentPane();
    paintTree(body, BG);
    javax.swing.JPanel root = new javax.swing.JPanel(new java.awt.BorderLayout());
    root.setBackground(BG);
    root.add(header, java.awt.BorderLayout.NORTH);
    root.add(body, java.awt.BorderLayout.CENTER);
    setContentPane(root);

    
    getRootPane().setBorder(javax.swing.BorderFactory.createLineBorder(ORANGE, 2));

    
    TotalAmountDue.setBackground(new java.awt.Color(0x15151B));
    TotalAmountDue.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(70, 70, 70), 1));

    
    payField.setBackground(new java.awt.Color(0xF2F2F2));
    payField.setForeground(java.awt.Color.BLACK);
    payField.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new java.awt.Color(150, 150, 150), 1),
            javax.swing.BorderFactory.createEmptyBorder(4, 6, 4, 6)));

    
    ProceedPay.setForeground(java.awt.Color.WHITE);
    ProceedPay.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
    ProceedPay.setContentAreaFilled(false);
    ProceedPay.setOpaque(false);
    ProceedPay.setBorderPainted(false);
    ProceedPay.setFocusPainted(false);
    ProceedPay.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 18, 8, 18));
    ProceedPay.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
    ProceedPay.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
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

    pack();   
}


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


private void finishPayment() {
    if (paymentDone) {
        return;   
    }
    if (total.signum() <= 0) {
        javax.swing.JOptionPane.showMessageDialog(this, "Please choose a top-up amount.",
                "Invalid Amount", javax.swing.JOptionPane.WARNING_MESSAGE);
        return;
    }
    BigDecimal paid = PaymentUtil.readCash(this, payField, total);
    if (paid == null) {
        return;   // readCash already showed the reason
    }
    if (!PaymentUtil.confirmAndCommit(this, ProceedPay, committer,
            PaymentUtil.paidBy(false), "this top up", total)) {
        return;
    }
    paymentDone = true;
    
    BigDecimal newBalance = currentBalance.add(total);   

    
    StringBuilder receipt = new StringBuilder("Top up successful.\n\n");
    receipt.append("Service: Top Up Credit\n");
    receipt.append("Account: ").append(usernameLbl.getText()).append(" (").append(nameCustomerLbl.getText()).append(")\n");
    receipt.append("Amount Credited: ").append(PaymentUtil.money(total)).append("\n");
    receipt.append("Payment Method: Cash\n");
    receipt.append("Amount Received: ").append(PaymentUtil.money(paid)).append("\n");
    receipt.append("Change: ").append(PaymentUtil.money(paid.subtract(total))).append("\n");
    receipt.append("Remaining Credit: ").append(PaymentUtil.money(newBalance)).append("\n");
    receipt.append("\nThis transaction has been recorded in Transactions.");


    java.awt.Window owner = getOwner();
    dispose();
    javax.swing.JOptionPane.showMessageDialog(owner, receipt.toString(),
            "Payment Confirmation", javax.swing.JOptionPane.INFORMATION_MESSAGE);
}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel InputPayment;
    private javax.swing.JPanel ModeOfPayment;
    private javax.swing.JPanel PayBill;
    private javax.swing.JButton ProceedPay;
    private javax.swing.JPanel TotalAmountDue;
    private javax.swing.JRadioButton cashRb;
    private javax.swing.JLabel changeLbl;
    private javax.swing.JRadioButton creditRb;
    private javax.swing.JLabel detailsTitleLbl;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel nameCustomerLbl;
    private javax.swing.JTextField payField;
    private javax.swing.JLabel payWithLbl;
    private javax.swing.JLabel titleLbl;
    private javax.swing.JLabel totalTitleLbl;
    private javax.swing.JLabel usernameLbl;
    // End of variables declaration//GEN-END:variables
}
