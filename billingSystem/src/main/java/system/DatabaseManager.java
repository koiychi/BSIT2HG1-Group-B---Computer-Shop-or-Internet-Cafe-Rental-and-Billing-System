/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package system;

/**
 *
 * @author koi
 */
import logic.GamingStation;
import logic.MemberCustomer;
import logic.RegularStation;
import logic.Station;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

// ==========================================
// INITIALIZATION
// ==========================================
    private static final String DBFile = "jdbc:sqlite:compShopDatabase.db";
    private static Connection connection = null;
    private static boolean tablesInitialized = false;
    private static boolean initializationInProgress = false;

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection(DBFile);

                try (Statement stmt = connection.createStatement()) {
                    stmt.execute("PRAGMA foreign_keys = ON;");
                }

                if (!tablesInitialized && !initializationInProgress) {
                    initializeTables();
                }
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Driver Error: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Database Connection Error: " + e.getMessage());
        }
        return connection;
    }

    public static void initializeTables() {
        if (tablesInitialized || initializationInProgress) {
            return;
        }
        initializationInProgress = true;
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
                    + "HasInternet INTEGER NOT NULL DEFAULT 0, "
                    + "AvailabilityStatus TEXT DEFAULT 'Available' CHECK (AvailabilityStatus IN ('Available', 'Occupied', 'Maintenance')), "
                    + "SessionEndTime TEXT, "
                    + "StationType TEXT NOT NULL CHECK (StationType IN ('REGULAR', 'GAMING')));");

            // 2A. REGULAR_STATION (SUBTYPE)
            stmt.execute("CREATE TABLE IF NOT EXISTS REGULAR_STATION ("
                    + "StationID TEXT PRIMARY KEY, "
                    + "HasPrinter INTEGER NOT NULL DEFAULT 0, "
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

            stmt.execute("CREATE TABLE IF NOT EXISTS SHOP_STATUS ("
                    + "ShopID INTEGER PRIMARY KEY CHECK (ShopID = 1), "
                    + "IsOpen INTEGER NOT NULL DEFAULT 1);");
            addColumnIfMissing(stmt, "SHOP_STATUS", "ShopID", "INTEGER DEFAULT 1");
            stmt.execute("INSERT OR IGNORE INTO SHOP_STATUS (ShopID, IsOpen) VALUES (1, 1);");
                        // Upgrade older database files that were created before these columns existed
            addColumnIfMissing(stmt, "STATION", "HasInternet", "INTEGER NOT NULL DEFAULT 0");
            addColumnIfMissing(stmt, "REGULAR_STATION", "HasPrinter", "INTEGER NOT NULL DEFAULT 0");

            tablesInitialized = true;
            System.out.println("Database initialized successfully!");

        } catch (SQLException e) {
            System.err.println("Error initializing tables: " + e.getMessage());
        } finally {
            initializationInProgress = false;
        }
    }

        public static boolean getShopOpenStatus() throws SQLException {
            String sql = "SELECT IsOpen FROM SHOP_STATUS WHERE ShopID = 1";
            try (Connection c = getConnection();
                 PreparedStatement pstmt = c.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {
                return rs.next() && rs.getInt(1) == 1;
            }
        }

        public static void setShopOpenStatus(boolean open) throws SQLException {
            String sql = "UPDATE SHOP_STATUS SET IsOpen = ? WHERE ShopID = 1";
            try (Connection c = getConnection();
                 PreparedStatement pstmt = c.prepareStatement(sql)) {
                pstmt.setInt(1, open ? 1 : 0);
                pstmt.executeUpdate();
            }
        }

        public static void resetOccupiedStations() throws SQLException {
            String sql = "UPDATE STATION SET AvailabilityStatus = 'Available', SessionEndTime = NULL "
                    + "WHERE AvailabilityStatus = 'Occupied'";
            try (Connection c = getConnection();
                 PreparedStatement pstmt = c.prepareStatement(sql)) {
                pstmt.executeUpdate();
            }
        }

        public static List<Station> getStationsByType(String type) throws SQLException {
            List<Station> stations = new ArrayList<>();
            for (Station station : getAllStations()) {
                if (station.getType().equalsIgnoreCase(type)) {
                    stations.add(station);
                }
            }
            return stations;
        }

        public static MemberCustomer findMember(String username) {
            String sql = "SELECT c.FirstName || ' ' || c.LastName, m.Username, m.Password, "
                    + "m.CreditBalance, m.DiscountRate "
                    + "FROM MEMBER_CUSTOMER m JOIN CUSTOMER c ON c.CustomerID = m.CustomerID "
                    + "WHERE m.Username = ?";
            try (Connection c = getConnection();
                 PreparedStatement pstmt = c.prepareStatement(sql)) {
                pstmt.setString(1, username);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return new MemberCustomer(rs.getString(1), rs.getString(2),
                                rs.getString(3), rs.getDouble(4), rs.getDouble(5));
                    }
                }
            } catch (SQLException e) {
                System.err.println("Error finding member: " + e.getMessage());
            }
            return null;
        }

        public static List<MemberCustomer> getAllMembers() {
            List<MemberCustomer> members = new ArrayList<>();
            String sql = "SELECT c.CustomerID, c.FirstName || ' ' || c.LastName AS FullName, "
                    + "m.Username, m.Password, m.CreditBalance, m.DiscountRate "
                    + "FROM MEMBER_CUSTOMER m "
                    + "JOIN CUSTOMER c ON c.CustomerID = m.CustomerID "
                    + "ORDER BY m.Username";

            try (Connection c = getConnection();
                 PreparedStatement pstmt = c.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    MemberCustomer member = new MemberCustomer(
                            rs.getString("FullName"),
                            rs.getString("Username"),
                            rs.getString("Password"),
                            rs.getDouble("CreditBalance"),
                            rs.getDouble("DiscountRate"));
                    member.setCustomerID(rs.getInt("CustomerID"));
                    members.add(member);
                }
            } catch (SQLException e) {
                System.err.println("Error loading members: " + e.getMessage());
            }
            return members;
        }

        public static int getCustomerIdByUsername(String username) {
            String sql = "SELECT CustomerID FROM MEMBER_CUSTOMER WHERE Username = ?";
            try (Connection c = getConnection();
                 PreparedStatement pstmt = c.prepareStatement(sql)) {
                pstmt.setString(1, username);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            } catch (SQLException e) {
                System.err.println("Error finding customer ID: " + e.getMessage());
            }
            return -1;
        }

        public static int getRegularID(String name) {
            String sql = "SELECT CustomerID FROM CUSTOMER "
                    + "WHERE CustomerType = 'REGULAR' AND (FirstName || ' ' || LastName) = ?";
            try (Connection c = getConnection();
                 PreparedStatement pstmt = c.prepareStatement(sql)) {
                pstmt.setString(1, name);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            } catch (SQLException e) {
                System.err.println("Error finding regular customer ID: " + e.getMessage());
            }
            return -1;
        }

        public static double getMemberDiscount(int customerId) {
            String sql = "SELECT DiscountRate FROM MEMBER_CUSTOMER WHERE CustomerID = ?";
            try (Connection c = getConnection();
                 PreparedStatement pstmt = c.prepareStatement(sql)) {
                pstmt.setInt(1, customerId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getDouble(1);
                    }
                }
            } catch (SQLException e) {
                System.err.println("Error finding member discount: " + e.getMessage());
            }
            return 0.0;
        }
        // Adds a column only if the table doesn't have it yet (safe to run every time)
    private static void addColumnIfMissing(Statement stmt, String table, String column, String definition)
            throws SQLException {
        boolean exists = false;
        try (ResultSet rs = stmt.executeQuery("PRAGMA table_info(" + table + ")")) {
            while (rs.next()) {
                if (column.equalsIgnoreCase(rs.getString("name"))) {
                    exists = true;
                    break;
                }
            }
        }
        if (!exists) {
            stmt.execute("ALTER TABLE " + table + " ADD COLUMN " + column + " " + definition);
            System.out.println("Added missing column " + table + "." + column);
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

    public static boolean updateMember(String username, String fullName, double balance, double discountRate) {
        String[] nameParts = splitFullName(fullName);
        String updateCustomer = "UPDATE CUSTOMER SET FirstName = ?, LastName = ? "
                + "WHERE CustomerID = (SELECT CustomerID FROM MEMBER_CUSTOMER WHERE Username = ?)";
        String updateMember = "UPDATE MEMBER_CUSTOMER SET CreditBalance = ?, DiscountRate = ? "
                + "WHERE Username = ?";

        try (Connection c = getConnection()) {
            c.setAutoCommit(false);
            try (PreparedStatement customerStatement = c.prepareStatement(updateCustomer);
                 PreparedStatement memberStatement = c.prepareStatement(updateMember)) {
                customerStatement.setString(1, nameParts[0]);
                customerStatement.setString(2, nameParts[1]);
                customerStatement.setString(3, username);
                customerStatement.executeUpdate();

                memberStatement.setDouble(1, balance);
                memberStatement.setDouble(2, discountRate);
                memberStatement.setString(3, username);
                int updated = memberStatement.executeUpdate();

                if (updated == 0) {
                    c.rollback();
                    return false;
                }
                c.commit();
                return true;
            } catch (SQLException e) {
                c.rollback();
                throw e;
            } finally {
                c.setAutoCommit(true);
            }
        } catch (SQLException e) {
            System.err.println("Error updating member: " + e.getMessage());
            return false;
        }
    }

    public static boolean deleteMember(String username) {
        String sql = "DELETE FROM CUSTOMER WHERE CustomerID = "
                + "(SELECT CustomerID FROM MEMBER_CUSTOMER WHERE Username = ?)";
        try (Connection c = getConnection();
             PreparedStatement statement = c.prepareStatement(sql)) {
            statement.setString(1, username);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting member: " + e.getMessage());
            return false;
        }
    }

    private static String[] splitFullName(String fullName) {
        int separator = fullName.indexOf(' ');
        if (separator <= 0 || separator == fullName.length() - 1) {
            return new String[]{fullName, ""};
        }
        return new String[]{
            fullName.substring(0, separator),
            fullName.substring(separator + 1)
        };
    }

// ==========================================
// UPDATING STATIONS
// ==========================================
    // Version 1: Status & End Time 
    public static boolean updateStation(String stationId, String status, String sessionEndTime) {
        String sql = "UPDATE STATION SET AvailabilityStatus = ?, SessionEndTime = ? WHERE StationID = ?";

        try (Connection c = getConnection(); PreparedStatement pstmt = c.prepareStatement(sql)) {

            pstmt.setString(1, toDbStatus(status));
            pstmt.setString(2, sessionEndTime);
            pstmt.setString(3, stationId);

            boolean updated = pstmt.executeUpdate() > 0;
            if (updated) {
                ShopManager.refreshDashboard();
            }
            return updated;
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

            boolean updated = pstmt.executeUpdate() > 0;
            if (updated) {
                ShopManager.refreshDashboard();
            }
            return updated;
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
                ShopManager.refreshDashboard();
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
// STATIONS: CREATE / READ / UPDATE / DELETE
// ==========================================

    // --- Status translation: UI says "In Use", database says "Occupied" ---
    private static String toDbStatus(String uiStatus) {
        return Station.IN_USE.equals(uiStatus) ? "Occupied" : uiStatus;
    }

    private static String fromDbStatus(String dbStatus) {
        return "Occupied".equals(dbStatus) ? Station.IN_USE : dbStatus;
    }

    // --- Duplicate check (friendly pre-check; the PRIMARY KEY is the real guarantee) ---
    public static boolean stationExists(String stationId) {
        String sql = "SELECT 1 FROM STATION WHERE StationID = ?";
        try (Connection c = getConnection(); PreparedStatement pstmt = c.prepareStatement(sql)) {
            pstmt.setString(1, stationId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Error checking station: " + e.getMessage());
            return false;
        }
    }

    // --- CREATE: saves STATION + subtype table + apps, all-or-nothing ---
    public static boolean insertStation(Station s) {
        String sql = "INSERT INTO STATION (StationID, HourlyRate, CPU_Model, RAM_GB, "
                + "AvailabilityStatus, StationType, HasInternet) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection c = getConnection()) {
            c.setAutoCommit(false);
            try {
                try (PreparedStatement pstmt = c.prepareStatement(sql)) {
                    pstmt.setString(1, s.getStationID());
                    pstmt.setDouble(2, s.getHourlyRate());
                    pstmt.setString(3, s.getCpu());
                    pstmt.setInt(4, s.getRamGb());
                    pstmt.setString(5, toDbStatus(s.getStatus()));
                    pstmt.setString(6, s.getType().toUpperCase());   // "REGULAR" / "GAMING"
                    pstmt.setInt(7, s.hasInternet() ? 1 : 0);
                    pstmt.executeUpdate();
                }
                insertSubtype(c, s);
                c.commit();
                ShopManager.refreshDashboard();
                return true;
            } catch (SQLException e) {
                c.rollback();
                System.err.println("Station insert rolled back: " + e.getMessage());
                return false;
            } finally {
                c.setAutoCommit(true);
            }
        } catch (SQLException e) {
            System.err.println("Error inserting station: " + e.getMessage());
            return false;
        }
    }

    // Writes the subtype row and the installed apps, using the SAME connection
    private static void insertSubtype(Connection c, Station s) throws SQLException {
        if (s instanceof GamingStation) {
            GamingStation g = (GamingStation) s;
            try (PreparedStatement pstmt = c.prepareStatement(
                    "INSERT INTO GAMING_STATION (StationID, GPU_Model, Monitor_Hz) VALUES (?, ?, ?)")) {
                pstmt.setString(1, g.getStationID());
                pstmt.setString(2, g.getGpu());
                pstmt.setInt(3, g.getMonitorHz());
                pstmt.executeUpdate();
            }
            saveApps(c, g.getStationID(), g.getGamesInstalled());
        } else if (s instanceof RegularStation) {
            RegularStation r = (RegularStation) s;
            try (PreparedStatement pstmt = c.prepareStatement(
                    "INSERT INTO REGULAR_STATION (StationID, HasPrinter) VALUES (?, ?)")) {
                pstmt.setString(1, r.getStationID());
                pstmt.setInt(2, r.hasPrinter() ? 1 : 0);
                pstmt.executeUpdate();
            }
            saveApps(c, r.getStationID(), r.getSoftwareList());
        }
    }

    // Adds each app name to INSTALLED_APPS (if new) and links it to the station
    private static void saveApps(Connection c, String stationId, List<String> apps) throws SQLException {
        String addApp = "INSERT OR IGNORE INTO INSTALLED_APPS (AppName) VALUES (?)";
        String link = "INSERT OR IGNORE INTO STATION_APPS (StationID, AppID) "
                + "SELECT ?, AppID FROM INSTALLED_APPS WHERE AppName = ?";
        try (PreparedStatement p1 = c.prepareStatement(addApp); PreparedStatement p2 = c.prepareStatement(link)) {
            for (String app : apps) {
                p1.setString(1, app);
                p1.executeUpdate();
                p2.setString(1, stationId);
                p2.setString(2, app);
                p2.executeUpdate();
            }
        }
    }

    // --- READ: loads every station as a RegularStation or GamingStation object ---
    public static List<Station> getAllStations() {
        List<Station> stations = new ArrayList<>();
        String sql = "SELECT s.StationID, s.HourlyRate, s.CPU_Model, s.RAM_GB, s.AvailabilityStatus, "
                + "s.StationType, s.HasInternet, g.GPU_Model, g.Monitor_Hz, r.HasPrinter "
                + "FROM STATION s "
                + "LEFT JOIN GAMING_STATION g ON s.StationID = g.StationID "
                + "LEFT JOIN REGULAR_STATION r ON s.StationID = r.StationID "
                + "ORDER BY s.StationID";

        try (Connection c = getConnection()) {
            Map<String, List<String>> appsByStation = loadAppsByStation(c);

            try (Statement stmt = c.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    String id = rs.getString("StationID");
                    try {
                        double rate = rs.getDouble("HourlyRate");
                        boolean internet = rs.getInt("HasInternet") == 1;
                        String status = fromDbStatus(rs.getString("AvailabilityStatus"));
                        String cpu = rs.getString("CPU_Model");
                        int ram = rs.getInt("RAM_GB");
                        List<String> apps = appsByStation.getOrDefault(id, new ArrayList<String>());

                        if ("GAMING".equals(rs.getString("StationType"))) {
                            stations.add(new GamingStation(id, rate, internet, status, cpu, ram,
                                    rs.getString("GPU_Model"), rs.getInt("Monitor_Hz"), apps));
                        } else {
                            stations.add(new RegularStation(id, rate, internet, status, cpu, ram,
                                    apps, rs.getInt("HasPrinter") == 1));
                        }
                    } catch (IllegalArgumentException e) {
                        // A bad row should not hide every other station
                        System.err.println("Skipped invalid station " + id + ": " + e.getMessage());
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error loading stations: " + e.getMessage());
        }
        return stations;
    }

    private static Map<String, List<String>> loadAppsByStation(Connection c) throws SQLException {
        Map<String, List<String>> map = new HashMap<>();
        String sql = "SELECT sa.StationID, a.AppName FROM STATION_APPS sa "
                + "JOIN INSTALLED_APPS a ON sa.AppID = a.AppID ORDER BY a.AppName";
        try (Statement stmt = c.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                map.computeIfAbsent(rs.getString(1), k -> new ArrayList<String>()).add(rs.getString(2));
            }
        }
        return map;
    }

    // --- UPDATE: edits everything about one station (ID and type cannot change) ---
    public static boolean updateStation(Station s) {
        String sqlBase = "UPDATE STATION SET HourlyRate = ?, CPU_Model = ?, RAM_GB = ?, "
                + "AvailabilityStatus = ?, HasInternet = ? WHERE StationID = ?";

        try (Connection c = getConnection()) {
            c.setAutoCommit(false);
            try {
                try (PreparedStatement pstmt = c.prepareStatement(sqlBase)) {
                    pstmt.setDouble(1, s.getHourlyRate());
                    pstmt.setString(2, s.getCpu());
                    pstmt.setInt(3, s.getRamGb());
                    pstmt.setString(4, toDbStatus(s.getStatus()));
                    pstmt.setInt(5, s.hasInternet() ? 1 : 0);
                    pstmt.setString(6, s.getStationID());
                    if (pstmt.executeUpdate() == 0) {          // no such station
                        c.rollback();
                        return false;
                    }
                }

                if (s instanceof GamingStation) {
                    GamingStation g = (GamingStation) s;
                    try (PreparedStatement pstmt = c.prepareStatement(
                            "UPDATE GAMING_STATION SET GPU_Model = ?, Monitor_Hz = ? WHERE StationID = ?")) {
                        pstmt.setString(1, g.getGpu());
                        pstmt.setInt(2, g.getMonitorHz());
                        pstmt.setString(3, g.getStationID());
                        pstmt.executeUpdate();
                    }
                } else if (s instanceof RegularStation) {
                    RegularStation r = (RegularStation) s;
                    try (PreparedStatement pstmt = c.prepareStatement(
                            "UPDATE REGULAR_STATION SET HasPrinter = ? WHERE StationID = ?")) {
                        pstmt.setInt(1, r.hasPrinter() ? 1 : 0);
                        pstmt.setString(2, r.getStationID());
                        pstmt.executeUpdate();
                    }
                }

                // Replace the app list: remove old links, add the current ones
                try (PreparedStatement pstmt = c.prepareStatement(
                        "DELETE FROM STATION_APPS WHERE StationID = ?")) {
                    pstmt.setString(1, s.getStationID());
                    pstmt.executeUpdate();
                }
                List<String> apps = (s instanceof GamingStation)
                        ? ((GamingStation) s).getGamesInstalled()
                        : ((RegularStation) s).getSoftwareList();
                saveApps(c, s.getStationID(), apps);

                c.commit();
                return true;
            } catch (SQLException e) {
                c.rollback();
                System.err.println("Station update rolled back: " + e.getMessage());
                return false;
            } finally {
                c.setAutoCommit(true);
            }
        } catch (SQLException e) {
            System.err.println("Error updating station: " + e.getMessage());
            return false;
        }
    }

    // --- DELETE: the subtype row and app links are removed by ON DELETE CASCADE ---
    public static boolean deleteStation(String stationId) {
        String sql = "DELETE FROM STATION WHERE StationID = ?";
        try (Connection c = getConnection(); PreparedStatement pstmt = c.prepareStatement(sql)) {
            pstmt.setString(1, stationId);
            boolean deleted = pstmt.executeUpdate() > 0;
            if (deleted) {
                ShopManager.refreshDashboard();
            }
            return deleted;
        } catch (SQLException e) {
            System.err.println("Error deleting station: " + e.getMessage());
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

            boolean recorded = pstmt.executeUpdate() > 0;
            if (recorded) {
                ShopManager.refreshDashboard();
            }
            return recorded;
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
        /*
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
*/
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

        try (java.sql.Connection c = DatabaseManager.getConnection(); java.sql.Statement stmt = c.createStatement(); java.sql.ResultSet rs = stmt.executeQuery(sql)) {

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
