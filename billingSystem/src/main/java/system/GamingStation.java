
package system;
/**
 *
 * @author Gian
 */
import java.util.ArrayList;
import java.util.List;

public class GamingStation extends Station {
    private List<String> gamesInstalled;

    //CONSTRUCTORS
    public GamingStation(String stationID, double hourlyRate, boolean hasInternet, boolean isAvailable, List<String> gamesInstalled) {
        super(stationID, hourlyRate, hasInternet, isAvailable);
        setGamesInstalled(gamesInstalled);
    }

    public GamingStation() {
        super("PC 0", 0.00, false, false);
        this.gamesInstalled = new ArrayList<>();
    }

    //GETTERS
    public List<String> getGamesInstalled() {
        return new ArrayList<>(gamesInstalled);
    }

    //SETTERS
    public void setGamesInstalled(List<String> gamesInstalled) {
        if (gamesInstalled == null) {
            throw new IllegalArgumentException("Game Installed cannot be null");
        }
        this.gamesInstalled = new ArrayList<>(gamesInstalled);
    }

    //OTHER METHODS
    public void playGame(String gameName) {
        if (gamesInstalled.contains(gameName)) {
            System.out.println("You are playing " + gameName);
        } else {
            System.out.println(gameName + " is not installed");
        }
    }

    public void addGame(String gameName) {
        if (gameName == null) {
            throw new IllegalArgumentException("Game name cannot be null");
        } else if (!gamesInstalled.contains(gameName)) {
            gamesInstalled.add(gameName);
            System.out.println(gameName + " has been installed");
        } else {
            System.out.println(gameName + " is already installed");
        }
    }

    //TODO: Aside from gamesInstalled, add other station info for GamingStation
    public String getStationInfo() {
        return "\nGames Installed: " + gamesInstalled;
    }

} //END OF CLASS
