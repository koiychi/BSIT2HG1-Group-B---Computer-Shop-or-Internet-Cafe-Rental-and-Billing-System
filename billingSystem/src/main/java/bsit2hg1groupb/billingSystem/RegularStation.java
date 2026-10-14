package bsit2hg1groupb.billingSystem;

import java.util.ArrayList;
import java.util.List;

public class RegularStation extends Station {
    private List<String> softwareList;
    private boolean hasPrinter;

    //CONSTRUCTORS
    public RegularStation(String stationID, double hourlyRate, boolean hasInternet, boolean isAvailable, List<String> softwareList, boolean hasPrinter) {
        super(stationID, hourlyRate, hasInternet, isAvailable);
        this.softwareList = new ArrayList<>(softwareList);
        this.hasPrinter = hasPrinter;
    }

    public RegularStation() {
        super("PC 0", 0.00, false, false);
        this.softwareList = new ArrayList<>();
        this.hasPrinter = false;
    }

    //GETTERS
    public List<String> getSoftwareList() {
        return new ArrayList<>(softwareList);
    }

    public boolean hasPrinter() {
        return hasPrinter;
    }

    //SETTERS //TODO: VALIDATED SETTERS; After validating, use in the parameterized constructor
    public void setHasPrinter(boolean hasPrinter) {
        this.hasPrinter = hasPrinter;
    }

    public void setSoftwareList(List<String> softwareList) {
        if (softwareList == null) {
            throw new IllegalArgumentException("Software cannot be null");
        }
        this.softwareList = new ArrayList<>(softwareList);
    }

    //OTHER METHODS
    public void addSoftware(String softwareName) {
        if (!softwareList.contains(softwareName)) {
            softwareList.add(softwareName);
            System.out.println(softwareName + " has been installed.");
        } else {
            System.out.println(softwareName + " is already installed.");
        }
    }

    public String getStationInfo() {
        return "\nSoftware List: " + softwareList + "\nHas Printer: " + hasPrinter;
    }
} //END OF CLASS
