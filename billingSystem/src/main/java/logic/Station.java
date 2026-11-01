package bsit2hg1groupb.billingSystem.system;

public abstract class Station {

    public static final String AVAILABLE   = "Available";
    public static final String IN_USE      = "In Use";
    public static final String MAINTENANCE = "Maintenance";

    private static int totalStationsCreated = 0;

    private final String stationID;  
    private double hourlyRate;
    private final boolean hasInternet;
    private String status;
    private String cpu = ""; 
    private int ramGb = 0;

    // CONSTRUCTORS
    public Station(String stationID, double hourlyRate, boolean hasInternet, String status) {
        this.stationID = validateStationID(stationID);
        setHourlyRate(hourlyRate);
        this.hasInternet = hasInternet;
        setStatus(status);
        totalStationsCreated++;
    }

    public Station(String stationID, double hourlyRate, boolean hasInternet, boolean isAvailable) {
        this(stationID, hourlyRate, hasInternet, isAvailable ? AVAILABLE : IN_USE);
    }

    public Station(String stationID, double hourlyRate) {
        this(stationID, hourlyRate, false, true);
    }

    public Station() {
        this("PC 0", 0.00, false, false);
    }

    // VALIDATION
    private static String validateStationID(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Station ID is required.");
        }
        String cleaned = id.trim();
        if (cleaned.length() > 20) {
            throw new IllegalArgumentException("Station ID must be 20 characters or fewer.");
        }
        return cleaned;
    }

    // GETTERS
    public static int getTotalStations(){ 
        return totalStationsCreated; 
    }
    public String getStationID(){
        return stationID; 
    }
    public double getHourlyRate(){ 
        return hourlyRate; 
    }
    public boolean hasInternet(){ 
        return hasInternet; 
    }
    public boolean isAvailable(){ 
        return AVAILABLE.equals(status); 
    }   // derived from status
    public String getStatus(){
        return status; 
    }
    public String getCpu(){
        return cpu; 
    }
    public int getRamGb(){
        return ramGb; 
    }
    // SETTERS (validated)
    public void setStatus(String status) {
        if (!AVAILABLE.equals(status) && !IN_USE.equals(status) && !MAINTENANCE.equals(status)) {
            throw new IllegalArgumentException("Invalid station status: " + status);
        }
        this.status = status;
    }

    public void setAvailable(boolean available) {
        if (available) {
            this.status = AVAILABLE;
        } else if (AVAILABLE.equals(status)) {
            this.status = IN_USE;   
        }
    }

    public void setHourlyRate(double hourlyRate) {
        if (Double.isNaN(hourlyRate) || hourlyRate < 0) {
            throw new IllegalArgumentException("Hourly rate cannot be negative.");
        }
        this.hourlyRate = hourlyRate;
    }

    public void setCpu(String cpu) {
        if (cpu == null || cpu.trim().isEmpty()) {
            throw new IllegalArgumentException("CPU is required.");
        }
        this.cpu = cpu.trim();
    }

    public void setRamGb(int ramGb) {
        if (ramGb < 1 || ramGb > 256) {
            throw new IllegalArgumentException("RAM must be between 1 and 256 GB.");
        }
        this.ramGb = ramGb;
    }

    // OTHER METHODS
    public abstract String getType();
    public abstract String getStationInfo();

} //END OF CLASS