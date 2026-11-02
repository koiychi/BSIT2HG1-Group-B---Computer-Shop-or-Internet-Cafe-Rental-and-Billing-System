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
import java.math.RoundingMode;
import service.SessionService;

public final class SessionPayment {

    private static final SessionService sessionService = new SessionService();

    private SessionPayment() {
    }

    // returns true only if the customer finished paying
    public static int payRegular(java.awt.Window owner, int customerId, String customerName,
            String stationId, String stationType, double hourlyRate, String duration) {
        int minutes = parseMinutes(duration);
        BigDecimal rate = BigDecimal.valueOf(hourlyRate).setScale(2, RoundingMode.HALF_UP);
        BigDecimal subtotal = rate.multiply(BigDecimal.valueOf(minutes))
                .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);

        int[] sessionId = {-1};
        PcRegular dlg = new PcRegular(owner);
        dlg.setCustomerName(customerName);
        dlg.setCommitter(paidBy -> saveSession(customerId, stationId, minutes, paidBy, sessionId));
        dlg.setBill(stationId + " (" + stationType + ")",
                duration + " x " + PaymentUtil.money(rate) + "/hr",
                subtotal, subtotal);
        dlg.setVisible(true);
        if (dlg.isPaid()) {
            PaymentUtil.firePaymentSaved();   // tell the dashboard etc. to reload
            return sessionId[0];
        }
        return -1;
    }

    public static int payMember(java.awt.Window owner, int customerId, String customerName,
            String username, String stationId, String stationType, double hourlyRate, String duration) {
        // fresh balance and discount from the database, not the earlier snapshot
        logic.MemberCustomer member = sessionService.findMember(username);
        if (member == null) {
            javax.swing.JOptionPane.showMessageDialog(owner, "Member not found.",
                    "Payment Failed", javax.swing.JOptionPane.ERROR_MESSAGE);
            return -1;
        }
        double discountRate = sessionService.getMemberDiscount(customerId);
        BigDecimal balance = BigDecimal.valueOf(member.getCreditAmount()).setScale(2, RoundingMode.HALF_UP);

        int minutes = parseMinutes(duration);
        BigDecimal rate = BigDecimal.valueOf(hourlyRate).setScale(2, RoundingMode.HALF_UP);
        BigDecimal subtotal = rate.multiply(BigDecimal.valueOf(minutes))
                .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);
        BigDecimal discountedTotal = subtotal.multiply(BigDecimal.valueOf(1 - discountRate))
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal discount = subtotal.subtract(discountedTotal);
        String percent = BigDecimal.valueOf(discountRate).multiply(BigDecimal.valueOf(100))
                .stripTrailingZeros().toPlainString();

        int[] sessionId = {-1};
        PcMember dlg = new PcMember(owner);
        String fullName = system.DatabaseManager.getCustomerFullName(customerId);
        dlg.setCustomerName(fullName.trim().isEmpty()
                ? "@" + username
                : fullName.trim() + " - @" + username);
        dlg.setCreditBalance(balance);
        dlg.setDiscountText("Discount " + percent + "%");
        dlg.setCommitter(paidBy -> saveSession(customerId, stationId, minutes, paidBy, sessionId));
        dlg.setBill(stationId + " (" + stationType + ")",
                duration + " x " + PaymentUtil.money(rate) + "/hr",
                subtotal, discount, discountedTotal);
        dlg.setVisible(true);
        if (dlg.isPaid()) {
            PaymentUtil.firePaymentSaved();   // tell the dashboard etc. to reload
            return sessionId[0];
        }
        return -1;
    }

    // returns null on success, or the database's error message
    private static String saveSession(int customerId, String stationId, int minutes,
            String paidBy, int[] out) {
        try {
            out[0] = sessionService.startSession(customerId, stationId, minutes, paidBy);
            return null;
        } catch (java.sql.SQLException ex) {
            return ex.getMessage();
        }
   
    }
        // "30 minutes", "1 hour", "1 hour 30 minutes", "2 hours" -> total minutes
    public static int parseMinutes(String duration) {
        int minutes = 0;
        String[] p = duration.trim().toLowerCase().split("\\s+");
        for (int i = 0; i + 1 < p.length; i += 2) {
            int n = Integer.parseInt(p[i]);
            minutes += p[i + 1].startsWith("min") ? n : n * 60;
        }
        return minutes;
    }
}