/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bsit2hg1groupb.billingSystem;

/**
 *
 * @author koi
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    private static final String DBFile = "jdbc:sqlite:compShopDatabase.db";
    private static Connection connection = null;

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection(DBFile);

                try (Statement stmt = connection.createStatement()) {
                    stmt.execute("PRAGMA foreign_keys = ON;");
                }

                // I-initialize ang mga tables ayon sa EERD Diagram
                initializeTables();
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Driver Error: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Database Connection Error: " + e.getMessage());
        }
        return connection;
    }

    private static void initializeTables() {
        try (Statement stmt = getConnection().createStatement()) {

            // 1. CUSTOMER (SUPERTYPE)
            stmt.execute("CREATE TABLE IF NOT EXISTS CUSTOMER ("
                    + "CustomerID INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "FirstName TEXT NOT NULL, "
                    + "LastName TEXT NOT NULL, "
                    + "CustomerType TEXT NOT NULL CHECK (CustomerType IN ('REGULAR', 'MEMBER')));");

            // 1A. REGULAR_CUSTOMER (SUBTYPE)
            stmt.execute("CREATE TABLE IF NOT EXISTS REGULAR_CUSTOMER ("
                    + "CustomerID INTEGER PRIMARY KEY, "
                    + "FOREIGN KEY (CustomerID) REFERENCES CUSTOMER(CustomerID) ON DELETE CASCADE);");

            // 1B. MEMBER_CUSTOMER (SUBTYPE)
            stmt.execute("CREATE TABLE IF NOT EXISTS MEMBER_CUSTOMER ("
                    + "CustomerID INTEGER PRIMARY KEY, "
                    + "Username TEXT UNIQUE NOT NULL, "
                    + "Password TEXT NOT NULL, "
                    + "CreditBalance REAL DEFAULT 0.0, "
                    + "DiscountRate REAL DEFAULT 0.0, "
                    + "FOREIGN KEY (CustomerID) REFERENCES CUSTOMER(CustomerID) ON DELETE CASCADE);");

            // 2. STATION (SUPERTYPE)
            stmt.execute("CREATE TABLE IF NOT EXISTS STATION ("
                    + "StationID TEXT PRIMARY KEY, "
                    + "HourlyRate REAL NOT NULL, "
                    + "CPU_Model TEXT NOT NULL, "
                    + "RAM_GB INTEGER NOT NULL, "
                    + "AvailabilityStatus TEXT DEFAULT 'Available' CHECK (AvailabilityStatus IN ('Available', 'Occupied', 'Maintenance')), "
                    + "SessionEndTime TEXT, "
                    + "StationType TEXT NOT NULL CHECK (StationType IN ('REGULAR', 'GAMING')));");

            // 2A. REGULAR_STATION (SUBTYPE)
            stmt.execute("CREATE TABLE IF NOT EXISTS REGULAR_STATION ("
                    + "StationID TEXT PRIMARY KEY, "
                    + "FOREIGN KEY (StationID) REFERENCES STATION(StationID) ON DELETE CASCADE);");

            // 2B. GAMING_STATION (SUBTYPE)
            stmt.execute("CREATE TABLE IF NOT EXISTS GAMING_STATION ("
                    + "StationID TEXT PRIMARY KEY, "
                    + "GPU_Model TEXT NOT NULL, "
                    + "Monitor_Hz INTEGER NOT NULL, "
                    + "FOREIGN KEY (StationID) REFERENCES STATION(StationID) ON DELETE CASCADE);");

            // 3. APPS & STATION_APPS 
            stmt.execute("CREATE TABLE IF NOT EXISTS INSTALLED_APPS ("
                    + "AppID INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "AppName TEXT UNIQUE NOT NULL);");

            stmt.execute("CREATE TABLE IF NOT EXISTS STATION_APPS ("
                    + "StationID TEXT NOT NULL, "
                    + "AppID INTEGER NOT NULL, "
                    + "PRIMARY KEY (StationID, AppID), "
                    + "FOREIGN KEY (StationID) REFERENCES STATION(StationID) ON DELETE CASCADE, "
                    + "FOREIGN KEY (AppID) REFERENCES INSTALLED_APPS(AppID) ON DELETE CASCADE);");

            // 4. TRANSACTION
            stmt.execute("CREATE TABLE IF NOT EXISTS TRANSACTIONS ("
                    + "TransactionID INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "CustomerID INTEGER NOT NULL, "
                    + "StationID TEXT, "
                    + "DateAndTime TEXT DEFAULT (datetime('now', 'localtime')), "
                    + "TransactionType TEXT NOT NULL, "
                    + "Amount REAL NOT NULL, "
                    + "PaidBy TEXT NOT NULL CHECK (PaidBy IN ('Cash', 'Member Credits')), "
                    + "FOREIGN KEY (CustomerID) REFERENCES CUSTOMER(CustomerID), "
                    + "FOREIGN KEY (StationID) REFERENCES STATION(StationID) ON DELETE SET NULL);");

            System.out.println("Database initialized successfully!");

        } catch (SQLException e) {
            System.err.println("Error initializing tables: " + e.getMessage());
        }
    }

// ==========================================
// ADDING CUSTOMERS (METHOD OVERLOADING)
// ==========================================
    
    // 1. BASE HELPER METHOD
    private static int insertBaseCustomer(String firstName, String lastName, String customerType) {
        String sql = "INSERT INTO CUSTOMER (FirstName, LastName, CustomerType) VALUES (?, ?, ?)";
        try (Connection c = getConnection(); PreparedStatement pstmt = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, customerType);
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error inserting base customer: " + e.getMessage());
        }
        return -1;
    }

    // 2. OVERLOADED METHOD 1: REGULAR CUSTOMER
    public static boolean addCustomer(String firstName, String lastName) {
        int id = insertBaseCustomer(firstName, lastName, "REGULAR");
        if (id == -1) {
            return false;
        }

        String sql = "INSERT INTO REGULAR_CUSTOMER (CustomerID) VALUES (?)";
        try (Connection c = getConnection(); PreparedStatement pstmt = c.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error adding regular customer: " + e.getMessage());
            return false;
        }
    }

    // 3. OVERLOADED METHOD 2: MEMBER CUSTOMER
    public static boolean addCustomer(String firstName, String lastName, String user, String pass, double balance) {
        int id = insertBaseCustomer(firstName, lastName, "MEMBER");
        if (id == -1) {
            return false;
        }

        String sql = "INSERT INTO MEMBER_CUSTOMER (CustomerID, Username, Password, CreditBalance, DiscountRate) VALUES (?, ?, ?, ?, 0.10)";
        try (Connection c = getConnection(); PreparedStatement pstmt = c.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.setString(2, user);
            pstmt.setString(3, pass);
            pstmt.setDouble(4, balance);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error adding member customer: " + e.getMessage());
            return false;
        }
    }

// ==========================================
// UPDATING STATIONS
// ==========================================
    
    // Version 1: Status & End Time 
    public static boolean updateStation(String stationId, String status, String sessionEndTime) {
        String sql = "UPDATE STATION SET AvailabilityStatus = ?, SessionEndTime = ? WHERE StationID = ?";

        try (Connection c = getConnection(); PreparedStatement pstmt = c.prepareStatement(sql)) {

            pstmt.setString(1, status);
            pstmt.setString(2, sessionEndTime);
            pstmt.setString(3, stationId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating station status: " + e.getMessage());
            return false;
        }
    }

    // Version 2: Overloaded for Regular Station Specs
    public static boolean updateStation(String stationId, double hourlyRate, String cpuModel, int ramGb) {
        String sql = "UPDATE STATION SET HourlyRate = ?, CPU_Model = ?, RAM_GB = ? WHERE StationID = ?";

        try (Connection c = getConnection(); PreparedStatement pstmt = c.prepareStatement(sql)) {

            pstmt.setDouble(1, hourlyRate);
            pstmt.setString(2, cpuModel);
            pstmt.setInt(3, ramGb);
            pstmt.setString(4, stationId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating regular station specs: " + e.getMessage());
            return false;
        }
    }

    // Version 3: Overloaded for Gaming Station Specs (+ GPU & Monitor Hz)
    public static boolean updateStation(String stationId, double hourlyRate, String cpuModel, int ramGb, String gpuModel, int monitorHz) {
        String sqlBase = "UPDATE STATION SET HourlyRate = ?, CPU_Model = ?, RAM_GB = ? WHERE StationID = ?";
        String sqlSub = "UPDATE GAMING_STATION SET GPU_Model = ?, Monitor_Hz = ? WHERE StationID = ?";

        try (Connection c = getConnection()) {
            c.setAutoCommit(false); // Transaction start

            try (PreparedStatement pstmt1 = c.prepareStatement(sqlBase); PreparedStatement pstmt2 = c.prepareStatement(sqlSub)) {

                // 1. Update Base Table (STATION)
                pstmt1.setDouble(1, hourlyRate);
                pstmt1.setString(2, cpuModel);
                pstmt1.setInt(3, ramGb);
                pstmt1.setString(4, stationId);
                pstmt1.executeUpdate();

                // 2. Update Subtype Table (GAMING_STATION)
                pstmt2.setString(1, gpuModel);
                pstmt2.setInt(2, monitorHz);
                pstmt2.setString(3, stationId);
                pstmt2.executeUpdate();

                c.commit(); // Transaction save
                return true;
            } catch (SQLException e) {
                c.rollback(); // Undo kapag nagka-error
                System.err.println("Transaction rolled back: " + e.getMessage());
                return false;
            } finally {
                c.setAutoCommit(true);
            }
        } catch (SQLException e) {
            System.err.println("Error updating gaming station: " + e.getMessage());
            return false;
        }
    }

// ==========================================
// ADD TRANSACTION
// ==========================================
    
    //PC Rental
    public static boolean addTransaction(int customerId, String stationId, String transactionType, double amount, String paidBy) {
        String sql = "INSERT INTO TRANSACTIONS (CustomerID, StationID, TransactionType, Amount, PaidBy) VALUES (?, ?, ?, ?, ?)";

        try (Connection c = getConnection(); PreparedStatement pstmt = c.prepareStatement(sql)) {

            pstmt.setInt(1, customerId);
            pstmt.setString(2, stationId);
            pstmt.setString(3, transactionType);
            pstmt.setDouble(4, amount);
            pstmt.setString(5, paidBy); // 'Cash' or 'Member Credits'

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error logging transaction with station: " + e.getMessage());
            return false;
        }
    }

    //Print Services and Top-ups
    public static boolean addTransaction(int customerId, String transactionType, double amount, String paidBy) {
        // Ipapasok bilang null ang StationID
        return addTransaction(customerId, null, transactionType, amount, paidBy);
    }

    public static double getTotalSales() {
        String sql = "SELECT SUM(Amount) FROM TRANSACTIONS";
        try (Connection c = getConnection(); Statement stmt = c.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            System.err.println("Error calculating sales: " + e.getMessage());
        }
        return 0.0;
    }

    
    
    
    // Tester
    public static void main(String[] args) {
        System.out.println("=== STARTING DATABASE INTEGRATION TEST ===\n");

        // 1. TEST: ADD REGULAR CUSTOMER (Overloaded addCustomer)
        System.out.println("[TEST 1] Adding Regular Customer...");
        boolean isRegularAdded = DatabaseManager.addCustomer("Juan", "Dela Cruz");
        System.out.println("Regular Customer Added: " + isRegularAdded);

        // 2. TEST: ADD MEMBER CUSTOMER (Overloaded addCustomer)
        System.out.println("\n[TEST 2] Adding Member Customer...");
        boolean isMemberAdded = DatabaseManager.addCustomer("Miles", "Rodriguez", "koiychi", "securePass123", 150.00);
        System.out.println("Member Customer Added: " + isMemberAdded);

        // 3. TEST: ADD TRANSACTIONS
        System.out.println("\n[TEST 3] Logging Transactions...");
        
        // Transaction A: PC Rental for Customer ID 1 at Station PC-01
        boolean tx1 = DatabaseManager.addTransaction(1, "PC-01", "Rental", 40.00, "Cash");
        
        // Transaction B: Printing Services for Customer ID 1 (No Station)
        boolean tx2 = DatabaseManager.addTransaction(1, "Printing", 15.00, "Cash");

        // Transaction C: Top-Up Credits for Member Customer ID 2
        boolean tx3 = DatabaseManager.addTransaction(2, "Top-Up", 100.00, "Member Credits");

        System.out.println("Rental Transaction Logged: " + tx1);
        System.out.println("Printing Transaction Logged: " + tx2);
        System.out.println("Top-Up Transaction Logged: " + tx3);

        // 4. TEST: FETCH & PRINT TRANSACTIONS LOG FROM DATABASE
        System.out.println("\n[TEST 4] Fetching Transactions Log from SQLite...");
        printTransactionLogs();

        System.out.println("\n=== ALL TESTS COMPLETED SUCCESSFULLY ===");
    }

    // Console-based logger simulator para makita ang laman na parang JTable
    private static void printTransactionLogs() {
        String sql = "SELECT "
                   + "  t.TransactionID, "
                   + "  c.FirstName || ' ' || c.LastName AS CustomerName, "
                   + "  IFNULL(t.StationID, 'N/A') AS Station, "
                   + "  t.TransactionType, "
                   + "  t.Amount, "
                   + "  t.PaidBy, "
                   + "  t.DateAndTime "
                   + "FROM TRANSACTIONS t "
                   + "JOIN CUSTOMER c ON t.CustomerID = c.CustomerID "
                   + "ORDER BY t.TransactionID ASC";

        try (java.sql.Connection c = DatabaseManager.getConnection();
             java.sql.Statement stmt = c.createStatement();
             java.sql.ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("---------------------------------------------------------------------------------------------------------");
            System.out.printf("| %-5s | %-18s | %-8s | %-12s | %-10s | %-15s | %-19s |\n", 
                              "TxID", "Customer Name", "Station", "Type", "Amount", "Paid By", "Date & Time");
            System.out.println("---------------------------------------------------------------------------------------------------------");

            while (rs.next()) {
                System.out.printf("| %-5d | %-18s | %-8s | %-12s | ₱%-9.2f | %-15s | %-19s |\n",
                        rs.getInt("TransactionID"),
                        rs.getString("CustomerName"),
                        rs.getString("Station"),
                        rs.getString("TransactionType"),
                        rs.getDouble("Amount"),
                        rs.getString("PaidBy"),
                        rs.getString("DateAndTime"));
            }
            System.out.println("---------------------------------------------------------------------------------------------------------");

        } catch (java.sql.SQLException e) {
            System.err.println("Error reading test logs: " + e.getMessage());
        }
    }

} //end of class
