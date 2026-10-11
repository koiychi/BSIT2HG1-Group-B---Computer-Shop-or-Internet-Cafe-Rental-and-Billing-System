/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bsit2hg1groupb.billingSystem;

/**
 *
 * @author Koi
 */
public class MemberCustomer extends Customer {
 private String username;
 private String password;
private double creditAmount;
private double discountRate;
    //CONSTRUCTORS
    public MemberCustomer(String name, String username, String password, double creditAmount, double discountRate) {
        super(name);
        setUsername(username);
        setPassword(password);
        setCreditAmount(creditAmount);
        setDiscountRate(discountRate);
    }
   public MemberCustomer(){
        this("Default Customer", "username", "password", 0.0, 0.20);
   };

    //GETTERS
    public String getUsername() {
        return username;
    }
    public double getCreditAmount() {
        return creditAmount;
    }
    public double getDiscountRate() {
        return discountRate;
    }
    //SETTERS
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
        if (creditAmount < 0) {
            throw new IllegalArgumentException("Credit amount cannot be negative");
        } this.creditAmount = creditAmount;
    }
    public void setDiscountRate(double discountRate) {
        if (discountRate < 0) {
            throw new IllegalArgumentException("Discount rate cannot be negative");
        } this.discountRate = discountRate;
    }
    //OTHER METHODS
    public boolean checkPassword(String inputPass) {
        if (inputPass == null || inputPass.isBlank()) {
            return false;
        }
        return this.password.equals(inputPass);
    }
    public boolean deductCreditAmount(double deductionAmount) {
        if (deductionAmount >0 && this.creditAmount >= deductionAmount) {
            this.creditAmount -= deductionAmount;
            return true;
        } return false;
    }
@Override
    public String getCustomerInfo(){
        return super.getCustomerInfo();
}
} //END OF CLASS
