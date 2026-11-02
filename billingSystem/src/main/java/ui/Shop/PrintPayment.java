/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui.Shop;

/**
 *
 * @author cals
 */
import java.math.BigDecimal;
import java.util.List;
import javax.swing.JOptionPane;

public final class PrintPayment {

    // regular print customers are saved under this one record
    private static final String WALK_IN = "Walk-in Customer";

    private PrintPayment() {
    }

    // opens the pay dialog for the print tab's orders; true only if the customer finished paying
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
        
        PrintMember dlg = new PrintMember(owner instanceof java.awt.Frame ? (java.awt.Frame) owner : null, true);
        dlg.setCommitter(paidBy -> save(dlg, total, paidBy));
        dlg.setOrders(printOrders, photocopyOrders, total);
        dlg.setVisible(true);

        if (dlg.isPaid()) {
            PaymentUtil.firePaymentSaved();   // tell the dashboard, members tab, etc. to reload
            return true;
        }
        return false;
    }

    // returns null on success, or the database's error message
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
}