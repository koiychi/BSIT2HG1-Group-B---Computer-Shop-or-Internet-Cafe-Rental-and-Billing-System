package service;

import system.DatabaseManager;
import logic.MemberCustomer;

public class MembershipService {
    //ADDING A MEMBER CUSTOMER
    public boolean addMemberCustomer (
    String firstName,
    String lastName,
    String username,
    String password,
    double balance) {
        
try {
    if (firstName == null || firstName.isBlank()
        || lastName == null || lastName.isBlank()
        || username == null || username.isBlank()
        || password == null || password.isBlank()
        || !Double.isFinite(balance) || balance < 0) {
    return false;

}
        MemberCustomer member = new MemberCustomer (
        firstName + " " + lastName,
        username,
        password,
        balance,
        0.10
        );
        
return DatabaseManager.addCustomer (
firstName,
lastName,
username,
password,
balance
       );

} catch (IllegalArgumentException e) {
    System.err.println("Invalid details: " + e.getMessage());
    return false;
    
}
    }
}

               

