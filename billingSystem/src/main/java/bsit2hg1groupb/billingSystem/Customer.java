package bsit2hg1groupb.billingSystem;

/**
 *
 * @author hazyth (Theo)
 */
public class Customer {
    private String name;

    // CONSTRUCTORS
    public Customer() {
        this("Default Customer");
    }

    public Customer(String name) {
        setName(name);
    }

    // GETTERS
    public String getName() {
        return name;
    }

    // SETTERS
    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank");
        }
        this.name = name;
    }

    // OTHER METHODS
    public String getCustomerInfo() {
        return "Customer Name: " + name;
    }
} //END OF CLASS
