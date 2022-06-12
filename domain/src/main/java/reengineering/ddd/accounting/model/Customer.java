package reengineering.ddd.accounting.model;

import reengineering.ddd.accounting.description.CustomerDescription;
import reengineering.ddd.archtype.Entity;

public class Customer implements Entity<String, CustomerDescription> {
    private String identity;
    private CustomerDescription description;

    public Customer(String identity, CustomerDescription description) {
        this.identity = identity;
        this.description = description;
    }

    public String identity() {
        return identity;
    }

    public CustomerDescription description() {
        return description;
    }
}
