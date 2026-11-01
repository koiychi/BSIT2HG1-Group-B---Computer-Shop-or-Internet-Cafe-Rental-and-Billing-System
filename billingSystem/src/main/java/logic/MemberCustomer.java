/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logic;

/**
 *
 * @author @author hazyth (Theo)
 */
public class MemberCustomer extends Customer {
   private int customerID;
private String username;
private String password;
private double creditAmount;
private double discountRate;
    // CONSTRUCTORS
    public MemberCustomer() {
        this("Default Customer", "username", "password", 0.0, 0.20);
    }

    public MemberCustomer(String name, String username, String password, double creditAmount, double discountRate) {
        super(name);
        setUsername(username);
        setPassword(password);
        setCreditAmount(creditAmount);
        setDiscountRate(discountRate);
    }

    // GETTERS
public int getCustomerID() {
    return customerID;
}

public void setCustomerID(int customerID) {
    if (customerID < 0) {
        throw new IllegalArgumentException("Customer ID cannot be negative");
    }
    this.customerID = customerID;
}
    
    public String getUsername() {
        return username;
    }

    public double getCreditAmount() {
        return creditAmount;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    // SETTERS
    public void setUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or blank");
        }
        this.username = username;
    }

    public void setPassword(String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or blank");
        }
        this.password = password;
    }

    public void setCreditAmount(double creditAmount) {
        if (!Double.isFinite(creditAmount) || creditAmount < 0) {
            throw new IllegalArgumentException("Credit amount cannot be negative");
        }
        this.creditAmount = creditAmount;
    }

    public void setDiscountRate(double discountRate ) {
        if (!Double.isFinite(discountRate)|| discountRate < 0 || discountRate > 1) {
            throw new IllegalArgumentException("Discount rate must be between 0 and 1");
        }
        this.discountRate = discountRate;
        
    }

    // OTHER METHODS
    public boolean checkPassword(String inputPass) {
        if (inputPass == null || inputPass.isBlank()) {
            return false;
        }
        return this.password.equals(inputPass);
    }

    public boolean deductCreditAmount(double deductionAmount) {
        if (deductionAmount > 0 && this.creditAmount >= deductionAmount) {
            this.creditAmount -= deductionAmount;
            return true;
        }
        return false;
    }

    @Override
    public String getCustomerInfo() {
        return super.getCustomerInfo()
                + "\nUsername: " + this.username
                + "\nCredit Amount: " + this.creditAmount
                + "\nDiscount Rate: " + this.discountRate;
    }

    private Exception IllegalArgumentsException(String discount_rate_must_be_between_0_and_1) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

} //END OF CLASS
