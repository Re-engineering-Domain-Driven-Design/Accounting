package reengineering.ddd.accounting.api;

import org.springframework.hateoas.Link;
import reengineering.ddd.accounting.api.representation.CustomerModel;
import reengineering.ddd.accounting.model.Customer;

import javax.ws.rs.GET;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class CustomerApi {
    private Customer customer;

    public CustomerApi(Customer customer) {
        this.customer = customer;
    }

    @GET
    public CustomerModel get(@Context UriInfo info) {
        URI self = info.getBaseUriBuilder().path(CustomersApi.class)
                .path(CustomersApi.class, "findById").build(customer.id());

        return new CustomerModel(customer, Link.of(self.getPath(), "self"));
    }
}
