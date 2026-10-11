/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bsit2hg1groupb.billingSystem;

/**
 *
 * @author Koi
 */
import java.util.ArrayList;
import java.util.List;

public class RegularStation extends Station {
    private List<String> softwareList;
    private boolean hasPrinter;

    //CONSTRUCTORS
    public RegularStation(int stationID, double hourlyRate, boolean hasInternet, List<String> softwareList, boolean hasPrinter){
        super(stationID, hourlyRate, hasInternet);
        this.softwareList = new ArrayList<>(softwareList);
        this.hasPrinter = hasPrinter;
    }
    //GETTERS
    public List<String> getSoftwareList(){
        return new ArrayList<>(softwareList);
    }
    public boolean hasPrinter(){
        return hasPrinter;
    }
    //SETTERS
    public void setHasPrinter(boolean hasPrinter){
       this.hasPrinter = hasPrinter;
    }
    public void addSoftware(String softwareName){
        if (!softwareList.contains(softwareName)) {
            softwareList.add(softwareName);
            System.out.println(softwareName + " has been installed.");
        } else {
            System.out.println(softwareName + " is already installed.");
        }
    }
    //OTHER METHODS
    public String getStationInfo(){
        return super.getStationInfo() + "\nSoftware List: " + softwareList + "\nHas Printer: " + hasPrinter;
    }
} //END OF CLASS
