/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logic;

/**
 *
 * @author Cals
 */
public class PcRentalBill implements Billable {

    private Station station;
    private int minutes;

    public PcRentalBill(Station station, int minutes) {
        setStation(station);
        setMinutes(minutes);
    }

    public Station getStation() {
        return station;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setStation(Station station) {
        if (station == null) {
            throw new IllegalArgumentException("Station cannot be null.");
        }
        this.station = station;
    }

    public void setMinutes(int minutes) {
        if (minutes <= 0) {
            throw new IllegalArgumentException("Duration must be greater than zero.");
        }
        this.minutes = minutes;
    }

    @Override
    public double calculateTotalCharge() {
        return station.calculateTotalCost(minutes / 60.0);
    }

    // line 1 is the label, line 2 is the details
    @Override
    public String generateInvoice() {
        return station.getStationID() + " (" + station.getType() + ")\n"
                + (minutes / 60) + " hr " + (minutes % 60) + " min x "
                + String.format("PHP %.2f/hr", station.getHourlyRate());
    }
}