package bsit2hg1groupb.billingSystem.system;

import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

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

    // GETTERS
    public List<String> getSoftwareList() {
        return new ArrayList<>(softwareList);
    }

    public boolean hasPrinter() {
        return hasPrinter;
    }

    @Override
    public String getType() {
        return "Regular";
    }

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

    // OTHER METHODS
    @Override
    public double calculateTotalCost(double durationInHours) {
        return baseCost(durationInHours);
    }

    @Override
    public Map<String, String> getSpecs() {
        Map<String, String> specs = new LinkedHashMap<>();
        specs.put("Has Printer", String.valueOf(hasPrinter));
        return specs;
    }

    @Override
    public Station withChanges(double hourlyRate, boolean hasInternet, String status,
            String cpu, int ramGb, Map<String, String> specs) {
        boolean printer = Boolean.parseBoolean(specs.getOrDefault("Has Printer", "false"));
        return new RegularStation(getStationID(), hourlyRate, hasInternet, status, cpu, ramGb,
                getSoftwareList(), printer);
    }
} //END OF CLASS
