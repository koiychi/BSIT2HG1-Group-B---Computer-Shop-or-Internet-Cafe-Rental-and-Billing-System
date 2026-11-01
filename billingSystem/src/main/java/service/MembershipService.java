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

         public MemberCustomer findMember(String username) {
                           if (username == null || username.isBlank()) {
                                    return null;
                           }
                           return DatabaseManager.findMember(username.trim());
         }

         public int getCustomerIdByUsername(String username) {
                           if (username == null || username.isBlank()) {
                                    return -1;
                           }
                           return DatabaseManager.getCustomerIdByUsername(username.trim());
         }

         public int getOrCreateRegularCustomerId(String fullName) {
                           if (fullName == null || fullName.isBlank()) {
                                    return -1;
                           }
                           return DatabaseManager.getOrCreateRegularID(fullName);
         }

         public String getCustomerFullName(int customerId) {
                  if (customerId <= 0) {
                           return null;
                  }
                  return DatabaseManager.getCustomerFullName(customerId);
         }

         public void addCredit(int customerId, double amount) throws java.sql.SQLException {
                           if (customerId <= 0) {
                                    throw new IllegalArgumentException("A valid member is required.");
                           }
                           if (!Double.isFinite(amount) || amount <= 0) {
                                    throw new IllegalArgumentException("Top-up amount must be greater than zero.");
                           }
                           DatabaseManager.addCredit(customerId, amount);
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
