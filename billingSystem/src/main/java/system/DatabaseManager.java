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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DatabaseManager {

// ==========================================
// INITIALIZATION
// ==========================================
    private static final String DBFile = "jdbc:sqlite:compShopDatabase.db";
    private static Connection connection = null;
    private static boolean tablesInitialized = false;

    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(DBFile);

        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON;");
        }

        return conn;
    }

    public static synchronized void initializeTables() {
        if (tablesInitialized) {
            return;
        }

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

            stmt.execute("CREATE TABLE IF NOT EXISTS SESSIONS ("
                    + "SessionID INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "CustomerID INTEGER NOT NULL, "
                    + "StationID TEXT NOT NULL, "
                    + "StartTime TEXT DEFAULT(datetime('now', 'localtime')), "
                    + "DurationMinutes INTEGER NOT NULL, "
                    + "EndTime TEXT NOT NULL, "
                    + "HourlyRate REAL NOT NULL, "
                    + "DiscountRate REAL DEFAULT 0.0, "
                    + "TotalAmount REAL NOT NULL, "
                    + "FOREIGN KEY (CustomerID) REFERENCES CUSTOMER(CustomerID), "
                    + "FOREIGN KEY (StationID) REFERENCES STATION(StationID));");

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

    public static int getRegularID(String fullName) {
        String[] parts = fullName.trim().split(" ", 2);
        String firstName = parts[0];
        String lastName = (parts.length > 1) ? parts[1] : "";
        String sql = "SELECT c.CustomerID FROM CUSTOMER c "
                + "JOIN REGULAR_CUSTOMER r ON c.CustomerID = r.CustomerID "
                + "WHERE LOWER(c.FirstName) = LOWER(?) AND LOWER(c.LastName) = LOWER(?)";
        try (Connection c = getConnection(); PreparedStatement pstmt = c.prepareStatement(sql)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("CustomerID");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding regular customer: " + e.getMessage());
            return -1;
        }

        // 2. Not found: create a new regular customer
        int id = insertBaseCustomer(firstName, lastName, "REGULAR");
        if (id == -1) {
            return -1;
        }
        try (Connection c = getConnection(); PreparedStatement pstmt = c.prepareStatement("INSERT INTO REGULAR_CUSTOMER (CustomerID) VALUES (?)")) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            return id;
        } catch (SQLException e) {
            System.err.println("Error creating regular customer: " + e.getMessage());
            return -1;
        }
    }

    public static int getCustomerIdByUsername(String username) {
        String sql = "SELECT CustomerID FROM MEMBER_CUSTOMER WHERE Username = ?";
        try (Connection c = getConnection(); PreparedStatement pstmt = c.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("CustomerID");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding member ID: " + e.getMessage());
        }
        return -1;
    }

    public static double getMemberDiscount(int customerId) {
        String sql = "SELECT DiscountRate FROM MEMBER_CUSTOMER WHERE CustomerID = ?";
        try (Connection c = getConnection(); PreparedStatement pstmt = c.prepareStatement(sql)) {
            pstmt.setInt(1, customerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("DiscountRate");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error reading member discount: " + e.getMessage());
        }
        return 0.0;
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

    // READ STATIONS BY TYPE
    public static List<Station> getStationsByType(String type) throws SQLException {
        List<Station> list = new ArrayList<>();
        String sql = "SELECT * FROM STATION WHERE UPPER(StationType) = UPPER(?)";

        PreparedStatement pstmt = getConnection().prepareStatement(sql);
        pstmt.setString(1, type);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            list.add(new Station(
                    rs.getString("StationID"),
                    rs.getString("StationType"),
                    rs.getString("AvailabilityStatus"),
                    rs.getDouble("HourlyRate")));
        }
        return list;
    }

// ==========================================
// READING MEMBER CUSTOMERS
// ==========================================
    // READ MEMBER BY USERNAME
    public static MemberCustomer findMember(String username) {
        String sql = "SELECT c.CustomerID c.FirstName, c.LastName, "
                + "m.Username, m.Password, m.CreditBalance, m.DiscountRate "
                + "FROM CUSTOMER c "
                + "JOIN MEMBER_CUSTOMER m ON c.CustomerID = m.CustomerID "
                + "WHERE m.Username = ?";

        try (Connection c = getConnection(); PreparedStatement pstmt = c.prepareStatement(sql)) {

            pstmt.setString(1, username);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                 MemberCustomer member = new MemberCustomer(
                        rs.getString("FirstName") + " " + rs.getString("LastName"),
                        rs.getString("Username"),
                        rs.getString("Password"),
                        rs.getDouble("CreditBalance"),
                        rs.getDouble("DiscountRate")
                );
                member.setCustomerID(rs.getInt("CustomerID"));
                return member;
            }
        } catch (SQLException e) {
            System.err.println("Error finding member: " + e.getMessage());
            return null;
        }
    }

    // READ ALL MEMBERS
    public static List<MemberCustomer> getAllMembers() {
        String sql = "SELECT c.CustomerID c.FirstName, c.LastName, "
                + "m.Username, m.Password, m.CreditBalance, m.DiscountRate "
                + "FROM CUSTOMER c "
                + "JOIN MEMBER_CUSTOMER m ON c.CustomerID = m.CustomerID "
                + "ORDER BY c.LastName, c.FirstName";
        List<MemberCustomer> members = new ArrayList<>();

        try (Connection c = getConnection(); Statement stmt = c.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
              MemberCustomer member = new MemberCustomer(
                        rs.getString("FirstName") + " " + rs.getString("LastName"),
                        rs.getString("Username"),
                        rs.getString("Password"),
                        rs.getDouble("CreditBalance"),
                        rs.getDouble("DiscountRate")
                );
                        member.setCustomerID(rs.getInt("CustomerID"));
                        members.add(member);
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
            try (PreparedStatement customerStmt = c.prepareStatement(customerSql); PreparedStatement memberStmt = c.prepareStatement(memberSql)) {
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

        try (Connection c = getConnection(); PreparedStatement pstmt = c.prepareStatement(sql)) {
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

        try (Connection c = getConnection(); PreparedStatement pstmt = c.prepareStatement(sql)) {
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

    public static int startSession(int customerId, String stationId, int durationMinutes, String paidBy) throws SQLException {
        String endTime = LocalDateTime.now().plusMinutes(durationMinutes).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        try (Connection c = getConnection()) {
            c.setAutoCommit(false);
            try {
                double rate = 0;
                try (PreparedStatement ps = c.prepareStatement("SELECT HourlyRate, AvailabilityStatus FROM STATION WHERE StationID = ?")) {
                    ps.setString(1, stationId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            throw new SQLException("Station not found.");
                        }
                        if (!"Available".equalsIgnoreCase(rs.getString("AvailabilityStatus"))) {
                            throw new SQLException("This station is no longer available.");
                        }
                        rate = rs.getDouble("HourlyRate");
                    }
                }
                double discount = 0.0;
                double balance = 0.0;
                try (PreparedStatement ps = c.prepareStatement(
                        "SELECT DiscountRate, CreditBalance FROM MEMBER_CUSTOMER WHERE CustomerID = ?")) {
                    ps.setInt(1, customerId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            discount = rs.getDouble("DiscountRate");
                            balance = rs.getDouble("CreditBalance");
                        }
                    }
                }

                double total = rate * (durationMinutes / 60.0) * (1 - discount);

                if ("Member Credits".equals(paidBy)) {
                    if (balance < total) {
                        throw new SQLException("Not enough credit balance.");
                    }
                    try (PreparedStatement ps = c.prepareStatement(
                            "UPDATE MEMBER_CUSTOMER SET CreditBalance = CreditBalance - ? WHERE CustomerID = ?")) {
                        ps.setDouble(1, total);
                        ps.setInt(2, customerId);
                        ps.executeUpdate();
                    }
                }
                int sessionId;
                try (PreparedStatement ps = c.prepareStatement(
                        "INSERT INTO SESSIONS (CustomerID, StationID, DurationMinutes, EndTime, HourlyRate, DiscountRate, TotalAmount) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, customerId);
                    ps.setString(2, stationId);
                    ps.setInt(3, durationMinutes);
                    ps.setString(4, endTime);
                    ps.setDouble(5, rate);
                    ps.setDouble(6, discount);
                    ps.setDouble(7, total);
                    ps.executeUpdate();
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (!keys.next()) {
                            throw new SQLException("Could not create the session.");
                        }
                        sessionId = keys.getInt(1);
                    }
                }
                try (PreparedStatement ps = c.prepareStatement(
                        "UPDATE STATION SET AvailabilityStatus = 'Occupied', SessionEndTime = ? WHERE StationID = ?")) {
                    ps.setString(1, endTime);
                    ps.setString(2, stationId);
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = c.prepareStatement(
                        "INSERT INTO TRANSACTIONS (CustomerID, StationID, TransactionType, Amount, PaidBy) VALUES (?, ?, ?, ?, ?)")) {
                    ps.setInt(1, customerId);
                    ps.setString(2, stationId);
                    ps.setString(3, "PC Rental");
                    ps.setDouble(4, total);
                    ps.setString(5, paidBy);
                    ps.executeUpdate();
                }

                c.commit();
                return sessionId;

            } catch (SQLException e) {
                c.rollback();
                throw e;
            } finally {
                c.setAutoCommit(true);
            }
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
