package service;

import system.DatabaseManager;
import logic.GamingStation;
import logic.Station;
import java.util.Comparator;
import java.util.List;

/**
 * Bridge between the station screens (AddStation, stationsTabPanel) and DatabaseManager.
 * Business rules live here so every screen gets them automatically.
 *
 * Errors: IllegalArgumentException = bad input, IllegalStateException = a rule or the
 * database said no. The UI catches both and shows ex.getMessage().
 */
public class StationsService {

    public static final String SORT_ID = "Station ID";
    public static final String SORT_RATE = "Hourly Rate";
    public static final String SORT_STATUS = "Status";

    // ---------- READ ----------
    public List<Station> getAllStations() {
        return DatabaseManager.getAllStations();
    }

    public Station findStation(String stationId) {
        if (stationId == null) {
            return null;
        }
        for (Station s : DatabaseManager.getAllStations()) {
            if (s.getStationID().equalsIgnoreCase(stationId.trim())) {
                return s;
            }
        }
        return null;
    }

    private Station requireStation(String stationId) {
        Station s = findStation(stationId);
        if (s == null) {
            throw new IllegalStateException("Station " + stationId + " was not found.");
        }
        return s;
    }

    // ---------- CREATE ----------
    public void addStation(Station s) {
        validateReadyToSave(s);
        if (findStation(s.getStationID()) != null) {
            throw new IllegalStateException("Station " + s.getStationID() + " already exists.");
        }
        if (!DatabaseManager.insertStation(s)) {
            throw new IllegalStateException("The station could not be saved. Check the console for details.");
        }
    }

    // ---------- UPDATE (full edit) ----------
    public void editStation(Station s) {
        validateReadyToSave(s);
        Station existing = requireStation(s.getStationID());
        if (!existing.getType().equals(s.getType())) {
            throw new IllegalStateException("A station's type cannot be changed.");
        }
        if (Station.IN_USE.equals(existing.getStatus()) && Station.MAINTENANCE.equals(s.getStatus())) {
            throw new IllegalStateException(
                    "End the current session before putting " + existing.getStationID() + " under maintenance.");
        }
        if (!DatabaseManager.updateStation(s)) {
            throw new IllegalStateException("The station could not be updated. Check the console for details.");
        }
    }

    // ---------- UPDATE (status only) ----------
    public void changeStatus(String stationId, String newStatus) {
        changeStatus(stationId, newStatus, null);
    }

    public void changeStatus(String stationId, String newStatus, String sessionEndTime) {
        if (!Station.AVAILABLE.equals(newStatus)
                && !Station.IN_USE.equals(newStatus)
                && !Station.MAINTENANCE.equals(newStatus)) {
            throw new IllegalArgumentException("Invalid station status: " + newStatus);
        }
        Station existing = requireStation(stationId);
        if (Station.IN_USE.equals(existing.getStatus()) && Station.MAINTENANCE.equals(newStatus)) {
            throw new IllegalStateException(
                    "End the current session before putting " + existing.getStationID() + " under maintenance.");
        }
        if (!DatabaseManager.updateStation(existing.getStationID(), newStatus, sessionEndTime)) {
            throw new IllegalStateException("The status could not be updated. Check the console for details.");
        }
    }

    // ---------- DELETE ----------
    public void deleteStation(String stationId) {
        Station existing = requireStation(stationId);
        if (Station.IN_USE.equals(existing.getStatus())) {
            throw new IllegalStateException(
                    "Station " + existing.getStationID() + " is in use and cannot be deleted.");
        }
        if (!DatabaseManager.deleteStation(existing.getStationID())) {
            throw new IllegalStateException("The station could not be deleted. Check the console for details.");
        }
    }

    // ---------- SEARCH / FILTER / SORT (in memory, on a list already loaded) ----------
    // type: "All", "Regular" or "Gaming"; status: "All", "Available", "In Use" or "Maintenance"
    public List<Station> searchFilterSort(List<Station> stations, String searchText,
                                          String type, String status, String sortBy) {
        String q = (searchText == null) ? "" : searchText.trim().toLowerCase();

        Comparator<Station> order;
        if (SORT_RATE.equals(sortBy)) {
            order = Comparator.comparingDouble(Station::getHourlyRate).thenComparing(Station::getStationID);
        } else if (SORT_STATUS.equals(sortBy)) {
            order = Comparator.comparing(Station::getStatus).thenComparing(Station::getStationID);
        } else {
            order = Comparator.comparing(Station::getStationID);
        }

        return stations.stream()
                .filter(s -> q.isEmpty()
                        || s.getStationID().toLowerCase().contains(q)
                        || s.getCpu().toLowerCase().contains(q))
                .filter(s -> type == null || "All".equals(type) || s.getType().equalsIgnoreCase(type))
                .filter(s -> status == null || "All".equals(status) || s.getStatus().equals(status))
                .sorted(order)
                .toList();
    }

    // ---------- BUSINESS RULES ----------
    private void validateReadyToSave(Station s) {
        if (s == null) {
            throw new IllegalArgumentException("There is no station to save.");
        }
        if (s.getHourlyRate() <= 0) {
            throw new IllegalArgumentException("Hourly rate must be greater than zero.");
        }
        if (s.getCpu() == null || s.getCpu().trim().isEmpty()) {
            throw new IllegalArgumentException("CPU is required.");
        }
        if (s.getRamGb() <= 0) {
            throw new IllegalArgumentException("RAM is required.");
        }
        if (s instanceof GamingStation) {
            GamingStation g = (GamingStation) s;
            if (g.getGpu() == null || g.getGpu().trim().isEmpty()) {
                throw new IllegalArgumentException("GPU is required for a gaming station.");
            }
            if (g.getMonitorHz() <= 0) {
                throw new IllegalArgumentException("Monitor refresh rate is required for a gaming station.");
            }
        }
    }
}