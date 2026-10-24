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
            stmt.execute("CREATE TABLE IF NOT EXISTS customer ("
                    + "customer_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "full_name TEXT NOT NULL, "
                    + "customer_type TEXT NOT NULL CHECK (customer_type IN ('REGULAR', 'MEMBER')));");

            // 1A. REGULAR_CUSTOMER (SUBTYPE)
            stmt.execute("CREATE TABLE IF NOT EXISTS regular_customer ("
                    + "customer_id INTEGER PRIMARY KEY, "
                    + "FOREIGN KEY (customer_id) REFERENCES customer(customer_id) ON DELETE CASCADE);");

            // 1B. MEMBER_CUSTOMER (SUBTYPE)
            stmt.execute("CREATE TABLE IF NOT EXISTS member_customer ("
                    + "customer_id INTEGER PRIMARY KEY, "
                    + "username TEXT UNIQUE NOT NULL, "
                    + "password TEXT NOT NULL, "
                    + "credit_balance REAL DEFAULT 0.0, "
                    + "discount_rate REAL DEFAULT 0.0, "
                    + "FOREIGN KEY (customer_id) REFERENCES customer(customer_id) ON DELETE CASCADE);");

            // 2. STATION (SUPERTYPE)
            stmt.execute("CREATE TABLE IF NOT EXISTS station ("
                    + "station_id TEXT PRIMARY KEY, "
                    + "hourly_rate REAL NOT NULL, "
                    + "cpu_model TEXT NOT NULL, "
                    + "ram_gb INTEGER NOT NULL, "
                    + "availability_status TEXT DEFAULT 'Available' CHECK (availability_status IN ('Available', 'In Use', 'Maintenance')), "
                    + "session_end_time TEXT, "
                    + "station_type TEXT NOT NULL CHECK (station_type IN ('REGULAR', 'GAMING')));");

            // 2A. REGULAR_STATION (SUBTYPE)
            stmt.execute("CREATE TABLE IF NOT EXISTS regular_station ("
                    + "station_id TEXT PRIMARY KEY, "
                    + "FOREIGN KEY (station_id) REFERENCES station(station_id) ON DELETE CASCADE);");

            // 2B. GAMING_STATION (SUBTYPE)
            stmt.execute("CREATE TABLE IF NOT EXISTS gaming_station ("
                    + "station_id TEXT PRIMARY KEY, "
                    + "gpu_model TEXT NOT NULL, "
                    + "monitor_hz INTEGER NOT NULL, "
                    + "FOREIGN KEY (station_id) REFERENCES station(station_id) ON DELETE CASCADE);");

            // 3. APPS & STATION_APPS (1NF JUNCTION TABLE)
            stmt.execute("CREATE TABLE IF NOT EXISTS app ("
                    + "app_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "app_name TEXT UNIQUE NOT NULL);");

            stmt.execute("CREATE TABLE IF NOT EXISTS station_app ("
                    + "station_id TEXT NOT NULL, "
                    + "app_id INTEGER NOT NULL, "
                    + "PRIMARY KEY (station_id, app_id), "
                    + "FOREIGN KEY (station_id) REFERENCES station(station_id) ON DELETE CASCADE, "
                    + "FOREIGN KEY (app_id) REFERENCES app(app_id) ON DELETE CASCADE);");

            // 4. TRANSACTION
            stmt.execute("CREATE TABLE IF NOT EXISTS transaction_log ("
                    + "transaction_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "customer_id INTEGER NOT NULL, "
                    + "station_id TEXT, "
                    + "date_and_time TEXT DEFAULT (datetime('now', 'localtime')), "
                    + "transaction_type TEXT NOT NULL, "
                    + "amount REAL NOT NULL, "
                    + "status TEXT NOT NULL CHECK (status IN ('Completed', 'Pending', 'Cancelled')), "
                    + "FOREIGN KEY (customer_id) REFERENCES customer(customer_id), "
                    + "FOREIGN KEY (station_id) REFERENCES station(station_id) ON DELETE SET NULL);");

            System.out.println("SQLite Database initialized with final EERD schema successfully!");

        } catch (SQLException e) {
            System.err.println("Error initializing tables: " + e.getMessage());
        }
    }

    // ==========================================
    // 3. HELPER METHOD (Kukuha ng Customer ID)
    // ==========================================
    public static int addCustomer(String name, String type) {
        String sql = "INSERT INTO customer (full_name, customer_type) VALUES (?, ?)";
        try (Connection c = getConnection(); PreparedStatement pstmt = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, name);
            pstmt.setString(2, type);
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // Ibabalik ang na-generate na ID
            }
        } catch (SQLException e) {
            System.err.println("Error in addCustomer: " + e.getMessage());
        }
        return -1;
    }

    // ==========================================
    // 4. HIWALAY NA ADD METHODS
    // ==========================================
    // A. Para sa Regular Customer
    public static boolean addRegularCustomer(String name) {
        int id = addCustomer(name, "REGULAR");
        if (id == -1) {
            return false;
        }

        String sql = "INSERT INTO regular_customer (customer_id) VALUES (?)";
        try (Connection c = getConnection(); PreparedStatement pstmt = c.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error adding regular customer: " + e.getMessage());
            return false;
        }
    }

    // B. Para sa Member Customer
    public static boolean addMemberCustomer(String name, String user, String pass, double balance) {
        int id = addCustomer(name, "MEMBER");
        if (id == -1) {
            return false;
        }

        String sql = "INSERT INTO member_customer (customer_id, username, password, credit_balance, discount_rate) VALUES (?, ?, ?, ?, 0.10)";
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

    // Tester Launcher
    public static void main(String[] args) {
        Connection testConn = getConnection();
        if (testConn != null) {
            System.out.println("SUCCESS: Connected to SQLite DB!");
        } else {
            System.out.println("FAILED: Could not connect to SQLite DB.");
        }
    }

} //end of class
