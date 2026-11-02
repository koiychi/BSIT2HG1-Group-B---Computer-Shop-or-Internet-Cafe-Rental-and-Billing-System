package logic;

public class Customer {
    private String name;

    public Customer() {
        this("Default Customer");
    }

    public Customer(String name) {
        setName(name);
    }

    public Customer(Customer other) {
        if (other == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }
        setName(other.getName());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank");
        }
        this.name = name;
    }

    public String getCustomerInfo() {
        return "Customer Name: " + name;
    }
}
