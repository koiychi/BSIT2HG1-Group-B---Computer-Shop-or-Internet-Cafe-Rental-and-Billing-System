/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package system;

/**
 *
 * @author koi
 */
import logic.MemberCustomer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

// ==========================================
// INITIALIZATION
// ==========================================
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
// READING MEMBER CUSTOMERS
// ==========================================

    // READ MEMBER BY USERNAME
    public static MemberCustomer findMember(String username) {
        String sql = "SELECT c.FirstName, c.LastName, "
                + "m.Username, m.Password, m.CreditBalance, m.DiscountRate "
                + "FROM CUSTOMER c "
                + "JOIN MEMBER_CUSTOMER m ON c.CustomerID = m.CustomerID "
                + "WHERE m.Username = ?";

        try (Connection c = getConnection();
                PreparedStatement pstmt = c.prepareStatement(sql)) {

            pstmt.setString(1, username);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                return new MemberCustomer(
                        rs.getString("FirstName") + " " + rs.getString("LastName"),
                        rs.getString("Username"),
                        rs.getString("Password"),
                        rs.getDouble("CreditBalance"),
                        rs.getDouble("DiscountRate")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error finding member: " + e.getMessage());
            return null;
        }
    }

    // READ ALL MEMBERS
    public static List<MemberCustomer> getAllMembers() {
        String sql = "SELECT c.FirstName, c.LastName, "
                + "m.Username, m.Password, m.CreditBalance, m.DiscountRate "
                + "FROM CUSTOMER c "
                + "JOIN MEMBER_CUSTOMER m ON c.CustomerID = m.CustomerID "
                + "ORDER BY c.LastName, c.FirstName";
        List<MemberCustomer> members = new ArrayList<>();

        try (Connection c = getConnection();
                Statement stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                members.add(new MemberCustomer(
                        rs.getString("FirstName") + " " + rs.getString("LastName"),
                        rs.getString("Username"),
                        rs.getString("Password"),
                        rs.getDouble("CreditBalance"),
                        rs.getDouble("DiscountRate")
                ));
            }

            return members;
        } catch (SQLException e) {
            System.err.println("Error reading members: " + e.getMessage());
            return members;
        }
    }

    // UPDATE MEMBER
    public static boolean updateMember(int customerId, String firstName, String lastName,
            String username, String password, double creditBalance, double discountRate) {
        String customerSql = "UPDATE CUSTOMER SET FirstName = ?, LastName = ? WHERE CustomerID = ?";
        String memberSql = "UPDATE MEMBER_CUSTOMER SET Username = ?, Password = ?, "
                + "CreditBalance = ?, DiscountRate = ? WHERE CustomerID = ?";

        try (Connection c = getConnection()) {
            c.setAutoCommit(false);
            try (PreparedStatement customerStmt = c.prepareStatement(customerSql);
                    PreparedStatement memberStmt = c.prepareStatement(memberSql)) {
                customerStmt.setString(1, firstName);
                customerStmt.setString(2, lastName);
                customerStmt.setInt(3, customerId);
                customerStmt.executeUpdate();

                memberStmt.setString(1, username);
                memberStmt.setString(2, password);
                memberStmt.setDouble(3, creditBalance);
                memberStmt.setDouble(4, discountRate);
                memberStmt.setInt(5, customerId);
                memberStmt.executeUpdate();

                c.commit();
                return true;
            } catch (SQLException e) {
                c.rollback();
                System.err.println("Member update rolled back: " + e.getMessage());
                return false;
            } finally {
                c.setAutoCommit(true);
            }
        } catch (SQLException e) {
            System.err.println("Error updating member: " + e.getMessage());
            return false;
        }
    }

    // DELETE CUSTOMER
    public static boolean deleteCustomer(int customerId) {
        String sql = "DELETE FROM CUSTOMER WHERE CustomerID = ?";

        try (Connection c = getConnection();
                PreparedStatement pstmt = c.prepareStatement(sql)) {
            pstmt.setInt(1, customerId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting customer: " + e.getMessage());
            return false;
        }
    }

    // RETRIEVE CUSTOMER
    public static ResultSet getCustomer(int customerId) {
        String sql = "SELECT * FROM CUSTOMER WHERE CustomerID = ?";

        try {
            PreparedStatement pstmt = getConnection().prepareStatement(sql);
            pstmt.setInt(1, customerId);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            System.err.println("Error reading customer: " + e.getMessage());
            return null;
        }
    }

    // UPDATE CUSTOMER
    public static boolean updateCustomer(int customerId, String firstName, String lastName) {
        String sql = "UPDATE CUSTOMER SET FirstName = ?, LastName = ? WHERE CustomerID = ?";

        try (Connection c = getConnection();
                PreparedStatement pstmt = c.prepareStatement(sql)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setInt(3, customerId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating customer: " + e.getMessage());
            return false;
        }
    }

// ==========================================
// STATION CRUD
// ==========================================

    // READ STATION
    public static ResultSet getStation(String stationId) {
        String sql = "SELECT * FROM STATION WHERE StationID = ?";

        try {
            PreparedStatement pstmt = getConnection().prepareStatement(sql);
            pstmt.setString(1, stationId);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            System.err.println("Error reading station: " + e.getMessage());
            return null;
        }
    }


// ==========================================
// TRANSACTION CRUD
// ==========================================

    // READ TRANSACTION
    public static ResultSet getTransaction(int transactionId) {
        String sql = "SELECT * FROM TRANSACTIONS WHERE TransactionID = ?";

        try {
            PreparedStatement pstmt = getConnection().prepareStatement(sql);
            pstmt.setInt(1, transactionId);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            System.err.println("Error reading transaction: " + e.getMessage());
            return null;
        }
    }

    // READ ALL TRANSACTIONS
    public static ResultSet getAllTransactions() {
        String sql = "SELECT * FROM TRANSACTIONS ORDER BY TransactionID ASC";

        try {
            Statement stmt = getConnection().createStatement();
            return stmt.executeQuery(sql);
        } catch (SQLException e) {
            System.err.println("Error reading transactions: " + e.getMessage());
            return null;
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
                c.rollback(); // Undo 
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
} //end of class
