/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package system;

/**
 *
 * @author theo
 */
public class Station {
    private String id;
    private String type;
    private String status;
    private double hourlyRate;

    public Station(String id, String type, String status, double hourlyRate) {
        this.id = id;
        this.type = type;
        this.status = status;
        this.hourlyRate = hourlyRate;
    }

    public String getId() {
        return id;
    }
    public String getType() {
        return type;
    }
    public String getStatus() {
        return status;
    }
    public double getHourlyRate() {
        return hourlyRate;
    }

    @Override
    public String toString() {
        return "Station " + id + " (" + status + ")";
    }
}
