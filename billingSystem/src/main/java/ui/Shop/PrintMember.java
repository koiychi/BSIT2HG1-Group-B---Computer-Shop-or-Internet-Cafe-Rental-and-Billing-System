/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package ui.Shop;

import logic.Billable;
import java.math.BigDecimal;
import java.math.RoundingMode;




public class PrintMember extends javax.swing.JDialog {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(PrintMember.class.getName());

    public PrintMember(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        setUndecorated(true);  
        initComponents();
        setupLogic();
        setLocationRelativeTo(parent);
    }
    private boolean paymentDone = false;
    private PaymentUtil.Committer committer;
    private BigDecimal creditBalance = BigDecimal.ZERO;

    public boolean isPaid() {
        return paymentDone;
    }

    public void setCommitter(PaymentUtil.Committer committer) {
        this.committer = committer;
    }

    
    public void setCreditBalance(BigDecimal balance) {
        creditBalance = balance;
        creditRb.setText("Credit (" + PaymentUtil.money(balance) + ")");
        creditRb.setEnabled(balance.compareTo(BigDecimal.ZERO) > 0);
    }

    




    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        TotalAmountDue = new javax.swing.JPanel();
        totalTitleLbl = new javax.swing.JLabel();
        totalLbl = new javax.swing.JLabel();
        ModeOfPayment = new javax.swing.JPanel();
        payWithLbl = new javax.swing.JLabel();
        cashRb = new javax.swing.JRadioButton();
        creditRb = new javax.swing.JRadioButton();
        InputPayment = new javax.swing.JPanel();
        payField = new javax.swing.JTextField();
        changeLbl = new javax.swing.JLabel();
        ProceedPay = new javax.swing.JButton();
        PayBill = new javax.swing.JPanel();
        titleLbl = new javax.swing.JLabel();
        subtotalTitleLbl = new javax.swing.JLabel();
        printLbl = new javax.swing.JLabel();
        photocopyLbl = new javax.swing.JLabel();
        calcTotalLbl = new javax.swing.JLabel();
        copyLbl = new javax.swing.JLabel();
        dicountLbl = new javax.swing.JLabel();
        customerTypeLbl = new javax.swing.JLabel();
        customerTypeBox = new javax.swing.JComboBox<>();
        discountLbl = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(293, 353));

        TotalAmountDue.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(38, 42, 52), 2, true));

        totalTitleLbl.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        totalTitleLbl.setText("Total Amount Due");

        totalLbl.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        totalLbl.setText("PHP 00.00");

        javax.swing.GroupLayout TotalAmountDueLayout = new javax.swing.GroupLayout(TotalAmountDue);
        TotalAmountDue.setLayout(TotalAmountDueLayout);
        TotalAmountDueLayout.setHorizontalGroup(
            TotalAmountDueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TotalAmountDueLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(TotalAmountDueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(totalLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totalTitleLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        TotalAmountDueLayout.setVerticalGroup(
            TotalAmountDueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TotalAmountDueLayout.createSequentialGroup()
                .addComponent(totalTitleLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(totalLbl)
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
                .addComponent(creditRb, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

        titleLbl.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        titleLbl.setText("Pay Bill");

        subtotalTitleLbl.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        subtotalTitleLbl.setText("SubTotal");

        printLbl.setText("Print:  Size - color (#)");

        photocopyLbl.setText("Photocopy: Size - color (#)");

        calcTotalLbl.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        calcTotalLbl.setText("PHP 00.00");

        copyLbl.setText("Page Count(#) x value");

        dicountLbl.setText("Discount 10%");

        customerTypeLbl.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        customerTypeLbl.setText("Customer Type");

        customerTypeBox.setEditable(true);
        customerTypeBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Member", "Regular" }));
        customerTypeBox.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));

        discountLbl.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        discountLbl.setText("PHP -00.00");
        discountLbl.setToolTipText("");

        javax.swing.GroupLayout PayBillLayout = new javax.swing.GroupLayout(PayBill);
        PayBill.setLayout(PayBillLayout);
        PayBillLayout.setHorizontalGroup(
            PayBillLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PayBillLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PayBillLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(titleLbl)
                    .addComponent(subtotalTitleLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(customerTypeBox, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(customerTypeLbl))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PayBillLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(PayBillLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(photocopyLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(PayBillLayout.createSequentialGroup()
                        .addComponent(dicountLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(discountLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(PayBillLayout.createSequentialGroup()
                        .addComponent(copyLbl)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(calcTotalLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(printLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(15, 15, 15))
        );
        PayBillLayout.setVerticalGroup(
            PayBillLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PayBillLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titleLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(customerTypeLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(customerTypeBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(subtotalTitleLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(printLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(photocopyLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PayBillLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(copyLbl)
                    .addComponent(calcTotalLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PayBillLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dicountLbl)
                    .addComponent(discountLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ModeOfPayment, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
            .addComponent(PayBill, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(PayBill, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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

    private void cashRbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cashRbActionPerformed
        payMethodChanged();
    }//GEN-LAST:event_cashRbActionPerformed

    private void creditRbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_creditRbActionPerformed
        payMethodChanged();
    }//GEN-LAST:event_creditRbActionPerformed

    private void ProceedPayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ProceedPayActionPerformed
        finishPayment();
    }//GEN-LAST:event_ProceedPayActionPerformed

    private void payFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_payFieldActionPerformed
        
    }//GEN-LAST:event_payFieldActionPerformed
private BigDecimal total = BigDecimal.ZERO;
private BigDecimal subtotal = BigDecimal.ZERO;
private BigDecimal discountAmt = BigDecimal.ZERO;   
private BigDecimal creditTotal = BigDecimal.ZERO;

    private void setupLogic() {
        
        PaymentUtil.setupPaymentField(payField);

        
        javax.swing.ButtonGroup payGroup = new javax.swing.ButtonGroup();
        payGroup.add(cashRb);
        payGroup.add(creditRb);
        cashRb.setSelected(true);
        
        customerTypeBox.setEditable(false);   
        customerTypeBox.setSelectedItem("Regular"); 
        customerTypeBox.addActionListener(e -> customerTypeChanged());
        applyRegularMode();                   

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

    
private void updateBill() {
    boolean credit = creditRb.isSelected();
    total = credit ? creditTotal : subtotal;   

    dicountLbl.setVisible(credit);    
    discountLbl.setVisible(credit);   

    calcTotalLbl.setText(PaymentUtil.money(subtotal));
    discountLbl.setText("PHP -" + discountAmt.setScale(2, RoundingMode.HALF_UP));
    totalLbl.setText(PaymentUtil.money(total));

    updateChange();
}
       
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
        
        setMinimumSize(null);     
        setPreferredSize(null);
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

private String typePlain = "";
private String detailsPlain = "";
private logic.Customer customer = new logic.Customer();   // plain customer = no discount

public void setBill(Billable bill) {
    if (bill == null) {
        throw new IllegalArgumentException("Bill cannot be null.");
    }
    BigDecimal sub = BigDecimal.valueOf(bill.calculateTotalCharge()).setScale(2, RoundingMode.HALF_UP);
    if (sub.signum() <= 0) {
        throw new IllegalArgumentException("Total amount due must be greater than 0.");
    }
    String invoice = bill.generateInvoice();
    typePlain = PaymentUtil.invoiceTitle(invoice);
    detailsPlain = PaymentUtil.invoiceBody(invoice);
    printLbl.setText(typePlain);
    photocopyLbl.setText("<html>" + detailsPlain.replace("&", "&amp;").replace("<", "&lt;")
            .replace("\n", "<br>") + "</html>");
    printLbl.setVisible(true);
    photocopyLbl.setVisible(true);
    copyLbl.setText("Order total");
    subtotal = sub;
    applyDiscount();
    updateBill();
    pack();
    setLocationRelativeTo(getOwner());
}

// the customer decides its own discount: 0 for a plain Customer, the saved rate for a MemberCustomer
private void applyDiscount() {
    double rate = customer.getDiscountRate();
    discountAmt = subtotal.multiply(BigDecimal.valueOf(rate)).setScale(2, RoundingMode.HALF_UP);
    creditTotal = subtotal.subtract(discountAmt);
    String percent = BigDecimal.valueOf(rate).multiply(BigDecimal.valueOf(100))
            .stripTrailingZeros().toPlainString();
    dicountLbl.setText("Discount " + percent + "%");
}

private boolean memberMode = false;
private int memberId = -1;
private boolean switchingType = false;

public boolean isMemberMode() {
    return memberMode;
}

public int getMemberId() {
    return memberId;
}


private void applyRegularMode() {
    memberMode = false;
    memberId = -1;
    customer = new logic.Customer();
    creditBalance = BigDecimal.ZERO;
    applyDiscount();
    creditRb.setText("Membership required");
    creditRb.setEnabled(false);
    cashRb.setSelected(true);
    payMethodChanged();
}

private void customerTypeChanged() {
    if (switchingType) {
        return;
    }
    if ("Member".equals(String.valueOf(customerTypeBox.getSelectedItem()))) {
        String username = SelectMemberPrint.pick(this);
        if (username != null && applyMember(username.trim())) {
            return;
        }
        switchingType = true;                       
        customerTypeBox.setSelectedItem("Regular");
        switchingType = false;
    }
    applyRegularMode();
}


private boolean applyMember(String username) {
    logic.MemberCustomer member = system.DatabaseManager.findMember(username);
    int id = system.DatabaseManager.getCustomerIdByUsername(username);
    if (member == null || id <= 0) {
        javax.swing.JOptionPane.showMessageDialog(this, "Member not found.",
                "Member Lookup", javax.swing.JOptionPane.ERROR_MESSAGE);
        return false;
    }
    customer = member;
    applyDiscount();
    setCreditBalance(BigDecimal.valueOf(member.getCreditAmount()).setScale(2, RoundingMode.HALF_UP));
    memberMode = true;
    memberId = id;
    cashRb.setSelected(true);
    payMethodChanged();
    return true;
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
        payField.setEnabled(cashRb.isSelected());   
        InputPayment.setVisible(cashRb.isSelected());   
        updateBill();
        pack();
        setLocationRelativeTo(getOwner());
        
        if (cashRb.isSelected()) {
            payField.requestFocusInWindow();
        } else {
            payField.setText("PHP 0.00");
        }
        updateBill();   
    }

        private void finishPayment() {
        if (paymentDone) {
            return;
        }
        boolean useCredit = creditRb.isSelected();
        BigDecimal paid = BigDecimal.ZERO;

        if (!useCredit) {
            try {
                paid = PaymentUtil.parseAmountStrict(payField.getText());
            } catch (IllegalArgumentException ex) {
                javax.swing.JOptionPane.showMessageDialog(this, ex.getMessage(),
                        "Invalid Payment", javax.swing.JOptionPane.WARNING_MESSAGE);
                payField.requestFocusInWindow();
                return;
            }
            if (paid.compareTo(BigDecimal.ZERO) <= 0) {
                javax.swing.JOptionPane.showMessageDialog(this,
                        "Please enter a payment amount greater than PHP 0.00.",
                        "Invalid Payment", javax.swing.JOptionPane.WARNING_MESSAGE);
                payField.requestFocusInWindow();
                return;
            }
            if (paid.compareTo(total) < 0) {
                javax.swing.JOptionPane.showMessageDialog(this,
                        "The amount paid is less than the total amount due.",
                        "Insufficient Payment", javax.swing.JOptionPane.WARNING_MESSAGE);
                payField.requestFocusInWindow();
                return;
            }
        }

        if (useCredit && creditBalance.compareTo(total) < 0) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Insufficient Shop Credit.\n\n"
                    + "Credit Balance: " + PaymentUtil.money(creditBalance) + "\n"
                    + "Total Amount Due: " + PaymentUtil.money(total),
                    "Insufficient Credit", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!PaymentUtil.confirmPayment(this, "this payment", total)) {
            return;
        }
        if (committer != null) {
            ProceedPay.setEnabled(false);
            String error = PaymentUtil.safeCommit(committer, PaymentUtil.paidBy(useCredit));
            if (error != null) {
                ProceedPay.setEnabled(true);
                javax.swing.JOptionPane.showMessageDialog(this, error,
                        "Payment Failed", javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        paymentDone = true;
        if (useCredit) {
            creditBalance = creditBalance.subtract(total);
        }

        String method = useCredit ? "Shop Credit" : "Cash";
        StringBuilder receipt = new StringBuilder("Payment successful.\n\n");
        receipt.append(memberMode ? "Service: Print - Member (" : "Service: Print - Regular (")
                 .append(typePlain).append(")\n");
        receipt.append("Details: ").append(detailsPlain.replace("\n", "\n    ")).append("\n");
        receipt.append("Total Amount: ").append(PaymentUtil.money(total)).append("\n");
        receipt.append("Payment Method: ").append(method).append("\n");

        if (!useCredit) {
            receipt.append("Amount Received: ").append(PaymentUtil.money(paid)).append("\n");
            receipt.append("Change: ").append(PaymentUtil.money(paid.subtract(total))).append("\n");
        }

        if (memberMode) {
            receipt.append("Remaining Credit: ").append(PaymentUtil.money(creditBalance)).append("\n");
        }
        receipt.append("\nThis transaction has been recorded in Transactions.");

        java.awt.Window owner = getOwner();
        dispose();
        javax.swing.JOptionPane.showMessageDialog(owner, receipt.toString(),
                "Payment Confirmation", javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String args[]) {
        
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        


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

        
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                PrintMember dialog = new PrintMember(new javax.swing.JFrame(), true);
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
    private javax.swing.JPanel InputPayment;
    private javax.swing.JPanel ModeOfPayment;
    private javax.swing.JPanel PayBill;
    private javax.swing.JButton ProceedPay;
    private javax.swing.JPanel TotalAmountDue;
    private javax.swing.JLabel calcTotalLbl;
    private javax.swing.JRadioButton cashRb;
    private javax.swing.JLabel changeLbl;
    private javax.swing.JLabel copyLbl;
    private javax.swing.JRadioButton creditRb;
    private javax.swing.JComboBox<String> customerTypeBox;
    private javax.swing.JLabel customerTypeLbl;
    private javax.swing.JLabel dicountLbl;
    private javax.swing.JLabel discountLbl;
    private javax.swing.JTextField payField;
    private javax.swing.JLabel payWithLbl;
    private javax.swing.JLabel photocopyLbl;
    private javax.swing.JLabel printLbl;
    private javax.swing.JLabel subtotalTitleLbl;
    private javax.swing.JLabel titleLbl;
    private javax.swing.JLabel totalLbl;
    private javax.swing.JLabel totalTitleLbl;
    // End of variables declaration//GEN-END:variables
}
