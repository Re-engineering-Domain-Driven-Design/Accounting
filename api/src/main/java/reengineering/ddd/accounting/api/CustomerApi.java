package reengineering.ddd.accounting.api;

import reengineering.ddd.accounting.domain.Customer;

public class CustomerApi {
    private Customer customer;

    public CustomerApi(Customer customer) {
        this.customer = customer;
    }
}
