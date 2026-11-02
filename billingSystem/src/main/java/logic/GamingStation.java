package logic;

import java.util.ArrayList;
import java.util.List;

public class GamingStation extends Station {
    private List<String> gamesInstalled;
    private String gpu = "";
    private int monitorHz = 0;

    public GamingStation(String stationID, double hourlyRate, boolean hasInternet,
                         String status, String cpu, int ramGb,
                         String gpu, int monitorHz, List<String> gamesInstalled) {
        super(stationID, hourlyRate, hasInternet, status);
        setCpu(cpu);
        setRamGb(ramGb);
        setGpu(gpu);
        setMonitorHz(monitorHz);
        setGamesInstalled(gamesInstalled);
    }

    public GamingStation(String stationID, double hourlyRate, boolean hasInternet,
                         boolean isAvailable, List<String> gamesInstalled) {
        super(stationID, hourlyRate, hasInternet, isAvailable);
        setGamesInstalled(gamesInstalled);
    }

    public GamingStation() {
        super("PC 0", 0.00, false, false);
        this.gamesInstalled = new ArrayList<>();
    }

    public GamingStation(GamingStation other) {
        super(other.getStationID(), other.getHourlyRate(), other.hasInternet(),
                other.getStatus());
        setCpu(other.getCpu());
        setRamGb(other.getRamGb());
        setGpu(other.getGpu());
        setMonitorHz(other.getMonitorHz());
        setGamesInstalled(other.getGamesInstalled());
    }

    public List<String> getGamesInstalled() {
        return new ArrayList<>(gamesInstalled);
    }

    public String getGpu() {
        return gpu;
    }

    public int getMonitorHz() {
        return monitorHz;
    }

    @Override
    public String getType() {
        return "Gaming";
    }

    public void setGamesInstalled(List<String> gamesInstalled) {
        if (gamesInstalled == null) {
            throw new IllegalArgumentException("Games installed cannot be null");
        }
        List<String> cleaned = new ArrayList<>();
        for (String g : gamesInstalled) {
            if (g == null || g.trim().isEmpty()) {
                throw new IllegalArgumentException("Game names cannot be blank.");
            }
            cleaned.add(g.trim());
        }
        this.gamesInstalled = cleaned;
    }

    public void setGpu(String gpu) {
        if (gpu == null || gpu.trim().isEmpty()) {
            throw new IllegalArgumentException("GPU is required.");
        }
        this.gpu = gpu.trim();
    }

    public void setMonitorHz(int monitorHz) {
        if (monitorHz < 30 || monitorHz > 500) {
            throw new IllegalArgumentException("Monitor refresh rate must be between 30 and 500 Hz.");
        }
        this.monitorHz = monitorHz;
    }

    public void addGame(String gameName) {
        if (gameName == null || gameName.trim().isEmpty()) {
            throw new IllegalArgumentException("Game name cannot be blank.");
        }
        String name = gameName.trim();
        if (!gamesInstalled.contains(name)) {
            gamesInstalled.add(name);
        }
    }

    public void playGame(String gameName) {
        if (gamesInstalled.contains(gameName)) {
            System.out.println("You are playing " + gameName);
        } else {
            System.out.println(gameName + " is not installed");
        }
    }

    @Override
    public String getStationInfo() {
        return "CPU: " + getCpu() + "\nRAM: " + getRamGb() + " GB"
             + "\nGPU: " + gpu + "\nMonitor: " + monitorHz + " Hz"
             + "\nGames Installed: " + gamesInstalled;
    }
}