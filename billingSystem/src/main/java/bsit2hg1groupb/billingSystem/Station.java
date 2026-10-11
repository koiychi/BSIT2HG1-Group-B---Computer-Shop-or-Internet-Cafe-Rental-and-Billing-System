/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bsit2hg1groupb.billingSystem;

<<<<<<< HEAD
public abstract class Station {


    private static int totalStationsCreated = 0;
    private String stationID;
    private double hourlyRate;
    private boolean hasInternet;
    private boolean isAvailable;

    // CONSTRUCTORS
    public Station(String stationID, double hourlyRate, boolean hasInternet, boolean isAvailable) {
        //FIXME: Needs validated setters
        this.stationID = stationID;
        this.hourlyRate = hourlyRate;
        this.hasInternet = hasInternet;
        this.isAvailable = isAvailable;
        totalStationsCreated++;
    }

    public Station(String stationID, double hourlyRate) {
        this(stationID, hourlyRate, false, true);
    }
    //TODO: Default Constructor



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
        return isAvailable;
    }

    // SETTERS
    //FIXME: Validate these setters
    //TODO: No validated setters stationID, setInternet
    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public void setAvailable(boolean available) {
        this.isAvailable = available;
    }

    // OTHER METHODS
    public abstract String getStationInfo();

} // END OF CLASS
=======
/**
 *
 * @author Koi
 */
public abstract class Station {
 
    //CONSTRUCTORS
    
    //GETTERS
    
    //SETTERS
    
    //OTHER METHODS
    
    
} //END OF CLASS
>>>>>>> parent of 5804012 (Update Station.java)
