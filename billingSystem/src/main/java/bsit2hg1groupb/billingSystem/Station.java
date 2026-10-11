package bsit2hg1groupb.billingSystem;
public abstract class Station {


    private String stationID;
    private double hourlyRate;
    private boolean hasInternet;
    private boolean isAvailable;
    private static int totalStationsCreated = 0;

    // CONSTRUCTORS
    public Station(String stationID, double hourlyRate, boolean hasInternet, boolean isAvailable) {
        this.stationID = stationID;
        this.hourlyRate = hourlyRate;
        this.hasInternet = hasInternet;
        this.isAvailable = isAvailable;
        totalStationsCreated++;
    }

    public Station(String stationID, double hourlyRate) {
        this(stationID, hourlyRate, false, true);
    }

    // GETTERS
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
        return isAvailable;
    }

    public static int getTotalStations() {
        return totalStationsCreated;
    }

    // SETTERS
    public void setAvailable(boolean available) {
        this.isAvailable = available;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    // OTHER METHODS
    public abstract String getStationInfo();

} // END OF CLASS