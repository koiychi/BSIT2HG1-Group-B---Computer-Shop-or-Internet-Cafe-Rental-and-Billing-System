package logic;

import java.util.List;

@SuppressWarnings("ALL")
public class Tester {
    public static void main(String[] testingBranch) {
        System.out.println("REGULAR STATION");
        RegularStation regularStation = new RegularStation("PC 1", 20.0, true, true, List.of("Microsoft Office", "Adobe Photoshop"), true);
        System.out.println(regularStation.getStationInfo());
        regularStation.addSoftware("Visual Studio Code");
        System.out.println(regularStation.getStationInfo());

        System.out.println("\n\nGAMING STATION");
        GamingStation gamingStation = new GamingStation("PC 2", 30.0, true, true, List.of("Fortnite", "Minecraft"));
        System.out.println(gamingStation.getStationInfo());
        gamingStation.addGame("Call of Duty");
        System.out.println(gamingStation.getStationInfo());

        System.out.println("CUSTOMER");
        Customer customer = new Customer("John Doe");
        System.out.println(customer.getCustomerInfo());

        System.out.println("MEMBER CUSTOMER");
        MemberCustomer member = new MemberCustomer();
        System.out.println(member.getCustomerInfo());

        System.out.println("\n\nBILLING TEST");
        Session session = new Session(1, customer, regularStation);
        System.out.println(session.generateInvoice());

        PrintJob printJob = new PrintJob("Print", "A4", "Plain", 1, false, 5.0);
        List<Billable> billables = List.of(session, printJob);
        for (Billable billable : billables) {
            System.out.println(billable.generateInvoice());
        }



    }
}
