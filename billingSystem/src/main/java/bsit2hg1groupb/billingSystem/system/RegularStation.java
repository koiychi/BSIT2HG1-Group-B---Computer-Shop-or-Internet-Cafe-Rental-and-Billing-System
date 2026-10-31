package bsit2hg1groupb.billingSystem.system;

import java.util.ArrayList;
import java.util.List;

public class RegularStation extends Station {
    private List<String> softwareList;
    private boolean hasPrinter;

    // CONSTRUCTORS
    public RegularStation(String stationID, double hourlyRate, boolean hasInternet,
                          String status, String cpu, int ramGb,
                          List<String> softwareList, boolean hasPrinter) {
        super(stationID, hourlyRate, hasInternet, status);
        setCpu(cpu);
        setRamGb(ramGb);
        setSoftwareList(softwareList);
        setHasPrinter(hasPrinter);
    }

    public RegularStation(String stationID, double hourlyRate, boolean hasInternet,
                          boolean isAvailable, List<String> softwareList, boolean hasPrinter) {
        super(stationID, hourlyRate, hasInternet, isAvailable);
        setSoftwareList(softwareList);
        setHasPrinter(hasPrinter);
    }

    public RegularStation() {
        super("PC 0", 0.00, false, false);
        this.softwareList = new ArrayList<>();
        this.hasPrinter = false;
    }

    // GETTERS
    public List<String> getSoftwareList() { return new ArrayList<>(softwareList); }
    public boolean hasPrinter() { return hasPrinter; }

    @Override
    public String getType() { return "Regular"; }

    // SETTERS (validated)
    public void setHasPrinter(boolean hasPrinter) {
        this.hasPrinter = hasPrinter;   // boolean: any value is valid
    }

    public void setSoftwareList(List<String> softwareList) {
        if (softwareList == null) {
            throw new IllegalArgumentException("Software cannot be null");
        }
        List<String> cleaned = new ArrayList<>();
        for (String s : softwareList) {
            if (s == null || s.trim().isEmpty()) {
                throw new IllegalArgumentException("Software names cannot be blank.");
            }
            cleaned.add(s.trim());
        }
        this.softwareList = cleaned;
    }

    // OTHER METHODS
    public void addSoftware(String softwareName) {
        if (softwareName == null || softwareName.trim().isEmpty()) {
            throw new IllegalArgumentException("Software name cannot be blank.");
        }
        String name = softwareName.trim();
        if (!softwareList.contains(name)) {
            softwareList.add(name);
        }
    }

    @Override
    public String getStationInfo() {
        return "CPU: " + getCpu() + "\nRAM: " + getRamGb() + " GB"
             + "\nSoftware List: " + softwareList + "\nHas Printer: " + hasPrinter;
    }
} //END OF CLASS