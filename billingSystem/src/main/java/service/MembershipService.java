package service;

import logic.MemberCustomer;
import system.DatabaseManager;
import java.util.List;

public class MembershipService {
         public List<MemberCustomer> getMembers() {
                  return DatabaseManager.getAllMembers();
         }

         public boolean updateMember(String username, String fullName, double balance, double discountRate) {
                  if (username == null || username.isBlank()
                          || fullName == null || fullName.isBlank()
                          || !Double.isFinite(balance) || balance < 0
                          || !Double.isFinite(discountRate) || discountRate < 0 || discountRate > 1) {
                           return false;
                  }
                  return DatabaseManager.updateMember(username, fullName, balance, discountRate);
         }

         public boolean deleteMember(String username) {
                  if (username == null || username.isBlank()) {
                           return false;
                  }
                  return DatabaseManager.deleteMember(username);
         }

         //ADDING A MEMBER CUSTOMER

         public boolean addMemberCustomer(
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
                           return DatabaseManager.addCustomer(
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
