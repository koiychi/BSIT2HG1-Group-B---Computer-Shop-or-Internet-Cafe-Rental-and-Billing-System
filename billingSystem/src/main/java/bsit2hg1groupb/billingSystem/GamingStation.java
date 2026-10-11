package bsit2hg1groupb.billingSystem;

import java.util.ArrayList;
import java.util.List;

public class GamingStation extends Station {
    private List<String> gamesInstalled;

    //CONSTRUCTORS
    public GamingStation(int stationID, double hourlyRate, boolean hasInternet, List<String> gamesInstalled){
        super(stationID, hourlyRate, hasInternet);
        this.gamesInstalled = new ArrayList<>(gamesInstalled);
    }
    public GamingStation(){
        super(0, 30.0, true);
        this.gamesInstalled = new ArrayList<>();
    }
    //GETTERS
    public List<String> getGamesInstalled(){
        return new ArrayList<>(gamesInstalled);
    }
    //SETTERS
    public void setGamesInstalled(List<String> gamesInstalled){
        if(gamesInstalled == null){
            throw new IllegalArgumentException("Game Installed cannot be null");
        }
        this.gamesInstalled = gamesInstalled;
    }
    //OTHER METHODS
    public void playGame(String gameName){
        if(gamesInstalled.contains(gameName)){
            System.out.println("You are playing " + gameName);
        } else {
            System.out.println(gameName + " is not installed");
        }
    }
    public void addGame(String gameName){
        if(gameName == null){
            throw new IllegalArgumentException("Game name cannot be null");
        }
        if(!gamesInstalled.contains(gameName)){
            gamesInstalled.add(gameName);
            System.out.println(gameName + " has been installed");
        } else {
            System.out.println(gameName + " is already installed");
        }
    }
    public String getStationInfo(){
        return super.getStationInfo() + "\nGames Installed: " + gamesInstalled;
    }
    
} //END OF CLASS
