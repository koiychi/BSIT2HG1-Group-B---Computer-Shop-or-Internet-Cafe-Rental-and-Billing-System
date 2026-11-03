package bsit2hg1groupb.billingSystem.system;

import java.util.Map;

public abstract class Station {

    public static final String AVAILABLE = "Available";
    public static final String IN_USE = "In Use";
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
    public static int getTotalStations() {
        return totalStationsCreated;
    }

    public String getStationID() {
        return stationID;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public boolean hasInternet() {
        return hasInternet;
    }

    public boolean isAvailable() {
        return AVAILABLE.equals(status);
    }   // derived from status

    public String getStatus() {
        return status;
    }

    public String getCpu() {
        return cpu;
    }

    public int getRamGb() {
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
        if (Double.isNaN(hourlyRate) || Double.isInfinite(hourlyRate) || hourlyRate <= 0) {
            throw new IllegalArgumentException("Hourly rate must be greater than 0.");
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

    // Cost of using this station for the given time
    public abstract double calculateTotalCost(double durationInHours);

    // Type-specific specs as label -> text, e.g. "GPU" -> "RTX 3060"
    public abstract Map<String, String> getSpecs();

    // Returns a NEW station of the same type with updated values. Each subclass validates its own specs.
    public abstract Station withChanges(double hourlyRate, boolean hasInternet, String status,
            String cpu, int ramGb, Map<String, String> specs);

    protected double baseCost(double durationInHours) {
        if (durationInHours <= 0) {
            throw new IllegalArgumentException("Duration must be greater than 0 hours.");
        }
        return getHourlyRate() * durationInHours;
    }
    // Throws IllegalArgumentException if the station is not complete enough to be saved

    public void validateComplete() {
        if (hourlyRate <= 0) {
            throw new IllegalArgumentException("Hourly rate must be greater than 0.");
        }
        if (cpu == null || cpu.trim().isEmpty()) {
            throw new IllegalArgumentException("CPU is required.");
        }
        if (ramGb <= 0) {
            throw new IllegalArgumentException("RAM is required.");
        }
    }

} //END OF CLASS
