/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui.Shop;
import java.math.BigDecimal;
import java.math.RoundingMode;
import service.SessionService;
import logic.PcRentalBill;
import service.SessionService;
import service.StationsService;

public final class SessionPayment {

    private static final SessionService sessionService = new SessionService();
    private static final StationsService stationsService = new StationsService();

    private SessionPayment() {
    }

    
public static int payRegular(java.awt.Window owner, int customerId, String customerName,
        String stationId, String stationType, double hourlyRate, String duration) {
    PcRentalBill bill = createBill(owner, stationId, duration);
    if (bill == null) {
        return -1;
    }
    int minutes = bill.getMinutes();

    int[] sessionId = {-1};
    PcRegular dlg = new PcRegular(owner);
    dlg.setCustomerName(customerName);
    dlg.setCommitter(paidBy -> saveSession(customerId, stationId, minutes, paidBy, sessionId));
    dlg.setBill(bill);
    dlg.setVisible(true);
    if (dlg.isPaid()) {
        PaymentUtil.firePaymentSaved();
        return sessionId[0];
    }
    return -1;
}

public static int payMember(java.awt.Window owner, int customerId, String customerName,
        String username, String stationId, String stationType, double hourlyRate, String duration) {

    logic.MemberCustomer member = sessionService.findMember(username);
    if (member == null) {
        javax.swing.JOptionPane.showMessageDialog(owner, "Member not found.",
                "Payment Failed", javax.swing.JOptionPane.ERROR_MESSAGE);
        return -1;
    }
    if (sessionService.hasActiveSessionForMember(username)) {
        javax.swing.JOptionPane.showMessageDialog(owner,
                "This member already has an active session and cannot start another one.",
                "Session Not Allowed", javax.swing.JOptionPane.WARNING_MESSAGE);
        return -1;
    }
    PcRentalBill bill = createBill(owner, stationId, duration);
    if (bill == null) {
        return -1;
    }
    int minutes = bill.getMinutes();
    BigDecimal balance = BigDecimal.valueOf(member.getCreditAmount()).setScale(2, RoundingMode.HALF_UP);

    int[] sessionId = {-1};
    PcMember dlg = new PcMember(owner);
    String fullName = system.DatabaseManager.getCustomerFullName(customerId);
    dlg.setCustomerName(fullName.trim().isEmpty()
            ? "@" + username
            : fullName.trim() + " - @" + username);
    dlg.setCreditBalance(balance);
    dlg.setCommitter(paidBy -> saveSession(customerId, stationId, minutes, paidBy, sessionId));
    dlg.setBill(bill, member);
    dlg.setVisible(true);
    if (dlg.isPaid()) {
        PaymentUtil.firePaymentSaved();
        return sessionId[0];
    }
    return -1;
}

    
    private static String saveSession(int customerId, String stationId, int minutes,
            String paidBy, int[] out) {
        try {
            out[0] = sessionService.startSession(customerId, stationId, minutes, paidBy);
            return null;
        } catch (java.sql.SQLException ex) {
            return ex.getMessage();
        }
    }

    private static PcRentalBill createBill(java.awt.Window owner, String stationId, String duration) {
        try {
            logic.Station station = stationsService.findStation(stationId);
            if (station == null) {
                throw new IllegalArgumentException("Station " + stationId + " was not found.");
            }
            return new PcRentalBill(station, parseMinutes(duration));
        } catch (IllegalArgumentException ex) {
            javax.swing.JOptionPane.showMessageDialog(owner, ex.getMessage(),
                    "Payment Failed", javax.swing.JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
   
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
