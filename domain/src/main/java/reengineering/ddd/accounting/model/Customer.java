package reengineering.ddd.accounting.model;

import reengineering.ddd.accounting.description.CustomerDescription;

public class Customer {
    private String id;
    private CustomerDescription description;

    public Customer(String id, CustomerDescription description) {
        this.id = id;
        this.description = description;
    }

    public String id() {
        return id;
    }

    public CustomerDescription description() {
        return description;
    }
}
