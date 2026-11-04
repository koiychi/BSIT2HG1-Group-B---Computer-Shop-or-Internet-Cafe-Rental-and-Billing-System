/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui.Shop;

import logic.PrintJob;
import logic.PrintOrderBill;
import java.math.BigDecimal;
import java.util.List;
import javax.swing.JOptionPane;

public final class PrintPayment {

    
    private static final String WALK_IN = "Walk-in Customer";

    private PrintPayment() {
    }

    
    public static boolean pay(java.awt.Window owner, List<String> printOrders,
            List<String> photocopyOrders, BigDecimal total) {
        if (printOrders.isEmpty() && photocopyOrders.isEmpty()
                || total.compareTo(BigDecimal.ZERO) <= 0) {
            JOptionPane.showMessageDialog(owner, "Add at least one order first.",
                    "Nothing to Pay", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (!system.ShopManager.ensureShopOpen()) {
            return false;
        }
        PrintOrderBill bill;
        BigDecimal billTotal;
        try {
            bill = toBill(printOrders, photocopyOrders);
            billTotal = bill.getAmountDue();   // also rejects a bill of 0 or less
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(owner, ex.getMessage(),
                    "Invalid Order", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        PrintMember dlg = new PrintMember(owner);
        dlg.setCommitter(paidBy -> save(dlg, billTotal, paidBy));
        dlg.setBill(bill);
        dlg.setVisible(true);

        if (dlg.isPaid()) {
            PaymentUtil.firePaymentSaved();   
            return true;
        }
        return false;
    }

    
    private static String save(PrintMember dlg, BigDecimal subtotal, String paidBy) {
        try {
            int customerId = dlg.isMemberMode()
                    ? dlg.getMemberId()
                    : system.DatabaseManager.getOrCreateRegularID(WALK_IN);
            if (customerId <= 0) {
                return "Could not find the customer record.";
            }
            system.DatabaseManager.addPrintPayment(customerId, subtotal.doubleValue(), paidBy);
            return null;
        } catch (java.sql.SQLException ex) {
            return ex.getMessage();
        }
    }
        // a row looks like: Colored - Short (8.5 x 11) x 3 (PHP 30.00)
    private static PrintOrderBill toBill(List<String> printOrders, List<String> photocopyOrders) {
        PrintOrderBill bill = new PrintOrderBill();
        addJobs(bill, "Print", printOrders);
        addJobs(bill, "Photocopy", photocopyOrders);
        return bill;
    }

    private static void addJobs(PrintOrderBill bill, String service, List<String> rows) {
        for (String row : rows) {
            int dash = row.indexOf(" - ");
            int times = row.lastIndexOf(" x ");
            int open = row.lastIndexOf("(PHP ");
            int close = row.lastIndexOf(")");
            if (dash < 0 || times < dash || open < times || close < open) {
                throw new IllegalArgumentException("This order could not be read: " + row);
            }

            String color = row.substring(0, dash).trim();
            String size = row.substring(dash + 3, times).trim();
            int bracket = size.indexOf('(');
            if (bracket > 0) {
                size = size.substring(0, bracket).trim();
            }

            int pages;
            try {
                pages = Integer.parseInt(row.substring(times + 3, open).trim());
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("This order could not be read: " + row);
            }
            if (pages <= 0) {
                throw new IllegalArgumentException("This order could not be read: " + row);
            }
            if (pages <= 0) {
                throw new IllegalArgumentException("This order could not be read: " + row);
            }
            double lineTotal = PaymentUtil.parseAmountStrict(row.substring(open + 5, close)).doubleValue();

            // the panel has no paper type, so "Standard" fills the required field
            bill.addJob(new PrintJob(service, size, "Standard", pages,
                    "Colored".equalsIgnoreCase(color), lineTotal / pages));
        }
    }
}