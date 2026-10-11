/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bsit2hg1groupb.billingSystem;

/**
 *
 * @author Koi
 */

public class Customer {
    private String name;
    //CONSTRUCTORS
public  Customer(String name) {
setName(name);
}
public Customer() {
    this("Default Customer");
}
    //GETTERS
public String getName() {
    return name;
}
    //SETTERS
    public void setName(String name) {
    if (name == null || name.isBlank()) {
        throw new IllegalArgumentException("Name cannot be null or blank");
    } this.name = name;
    }
    //OTHER METHODS
public String getCustomerInfo() {
    return "Customer Name: " + name;
}
    
} //END OF CLASS
