/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
//@author Carlo
package bsit2hg1groupb.billingSystem;

public class Session implements Billable {

    //ATTRIBUTES
    private int sessionID;
    private Customer customer;
    private Station station;
    private long startTime;
    private long endTime;
    private double duration;
    private String currentActivity;
    private boolean isActive;

    //PARAMETERIZED CONSTRUCTOR
    public Session(int sessionID, Customer customer, Station station) {
        this.setSessionID(sessionID);
        this.setCustomer(customer);
        this.setStation(station);
        this.startTime = 0;
        this.endTime = 0;
        this.duration = 0.0;
        this.currentActivity = "None";
        this.isActive = false;
    }

    //GETTERS
    public int getSessionID() {
        return sessionID;
    }

    //SETTERS
    public void setSessionID(int sessionID) {
        if (sessionID <= 0) {
            throw new IllegalArgumentException("Session ID must be greater than 0.");
        }
        this.sessionID = sessionID;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null.");
        }
        this.customer = customer;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        if (station == null) {
            throw new IllegalArgumentException("Station cannot be null.");
        }
        this.station = station;
    }

    public String getCurrentActivity() {
        return currentActivity;
    }

    public void setCurrentActivity(String currentActivity) {
        if (currentActivity == null || currentActivity.isEmpty()) {
            throw new IllegalArgumentException("Current activity cannot be null or empty.");
        }
        this.currentActivity = currentActivity;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public double getDuration() {
        return duration;
    }

    public boolean isActive() {
        return isActive;
    }

    //METHODS
    public void startSession() {
        if (isActive) {
            throw new IllegalStateException("Session is already active.");
        }
        startTime = System.currentTimeMillis();
        endTime = 0;
        duration = 0.0;
        currentActivity = "In use";
        isActive = true;
    }

    public void endSession() {
        if (!isActive) {
            throw new IllegalStateException("Session is not active.");
        }
        endTime = System.currentTimeMillis();
        isActive = false;
        currentActivity = "Ended";
        duration = calculateDuration();
    }

    public double calculateDuration() {
        if (startTime == 0)
            return 0.0;
        long end = isActive ? System.currentTimeMillis() : endTime;
        return (end - startTime) / 3600000.0;
    }


    public double calculateTotalCharge() {
        return calculateDuration() * station.getHourlyRate();
    }

    @Override
    public String generateInvoice() {
        //TODO in UI: Invoice must only be generated after the session has ended.
        return "===== INVOICE =====\n"
                + "Session ID: " + sessionID + "\n"
                + "Customer: " + customer.getName() + "\n"
                + "Station: " + station.getStationID() + "\n"
                + String.format("Duration: %.2f hour(s)\n", calculateDuration())
                + String.format("Rate: %.2f per hour\n", station.getHourlyRate())
                + String.format("Total Charge: %.2f\n", calculateTotalCharge())
                + "===================";
    }
}//END OF CLASS