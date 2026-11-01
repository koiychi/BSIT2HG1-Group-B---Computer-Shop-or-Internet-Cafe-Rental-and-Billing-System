package service;

import java.sql.SQLException;
import logic.MemberCustomer;
import system.DatabaseManager;

/**
 * Coordinates session-related reads and writes for the UI.
 */
public class SessionService {

    public MemberCustomer findMember(String username) {
        return DatabaseManager.findMember(username);
    }

    public double getMemberDiscount(int customerId) {
        return DatabaseManager.getMemberDiscount(customerId);
    }

    public int startSession(int customerId, String stationId, int durationMinutes,
            String paidBy) throws SQLException {
        if (customerId <= 0) {
            throw new IllegalArgumentException("A valid customer is required.");
        }
        if (stationId == null || stationId.isBlank()) {
            throw new IllegalArgumentException("A station is required.");
        }
        if (durationMinutes <= 0) {
            throw new IllegalArgumentException("Duration must be greater than zero.");
        }
        if (paidBy == null || paidBy.isBlank()) {
            throw new IllegalArgumentException("A payment method is required.");
        }
        return DatabaseManager.startSession(customerId, stationId.trim(), durationMinutes, paidBy);
    }

    public int getCustomerIdByUsername(String username) {
        return DatabaseManager.getCustomerIdByUsername(username);
    }

    public int getOrCreateRegularCustomerId(String fullName) {
        return DatabaseManager.getOrCreateRegularID(fullName);
    }
}
