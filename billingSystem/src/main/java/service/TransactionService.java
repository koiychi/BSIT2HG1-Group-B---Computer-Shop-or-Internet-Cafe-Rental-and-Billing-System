package service;

import java.util.List;
import system.DatabaseManager;

public class TransactionService {

    public List<Object[]> getHistory() {
        return DatabaseManager.getTransactionHistory();
    }

    public double getTotalSales() {
        return DatabaseManager.getTotalSales();
    }
}
