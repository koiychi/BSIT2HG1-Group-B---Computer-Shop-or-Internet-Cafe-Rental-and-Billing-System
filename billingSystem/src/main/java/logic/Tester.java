package logic;

import java.sql.SQLOutput;
import java.util.List;

import logic.Customer;
import logic.GamingStation;
import logic.MemberCustomer;
import logic.RegularStation;
import logic.Session;

@SuppressWarnings("ALL")
public class Tester {
    public static void main(String[] testingBranch) {
        //=================================
        //  A. TESTING STATION CLASSES
        //=================================

        //A1. STATION
        //Station is abstract, cannot create an instance of it

        //A2. REGULAR STATION
        //Inherits from Station
        System.out.println("REGULAR STATION");
        RegularStation regularStation = new RegularStation("PC 1", 20.0, true, true, List.of("Microsoft Office", "Adobe Photoshop"), true);
        System.out.println(regularStation.getStationInfo());
        regularStation.addSoftware("Visual Studio Code");
        System.out.println(regularStation.getStationInfo());

        //A3. GAMING STATION
        //Inherits from Station
        System.out.println("\n\nGAMING STATION");
        GamingStation gamingStation = new GamingStation("PC 2", 30.0, true, true, List.of("Fortnite", "Minecraft"));
        System.out.println(gamingStation.getStationInfo());
        gamingStation.addGame("Call of Duty");
        System.out.println(gamingStation.getStationInfo());

        //=================================
        //  B. TESTING CUSTOMER CLASSES
        //=================================

        //B1. CUSTOMER
        //Superclass for MemberCustomer
        System.out.println("CUSTOMER");
        Customer customer = new Customer("John Doe");
        System.out.println(customer.getCustomerInfo());

        //B2. MEMBER CUSTOMER
        //Inheriting from Customer
        System.out.println("MEMBER CUSTOMER");
        MemberCustomer member = new MemberCustomer();
        System.out.println(member.getCustomerInfo());

        //C. SESSION BILLING
        System.out.println("\n\nBILLING TEST");
        Session session = new Session(1, customer, regularStation);
        System.out.println(session.generateInvoice());

        //OOP / Java conventions check:
        //Encapsulation: yes, fields are mostly private
        //Inheritance: yes (RegularStation, GamingStation, MemberCustomer)
        //Abstraction: yes (Station abstract, Billable interface)
        //Polymorphism: yes (getStationInfo(), getCustomerInfo(), generateInvoice())
        //Java naming conventions: mostly good (camelCase, PascalCase, lowercase package name)
        //Code quality issues: leftover TODO/FIXME, @SuppressWarnings("ALL"), model classes using System.out.println, and an invalid main-class config



    }
}
