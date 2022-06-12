package reengineering.ddd.accounting.api;

import org.springframework.hateoas.Link;
import reengineering.ddd.accounting.api.representation.CustomerModel;
import reengineering.ddd.accounting.model.Customer;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.container.ResourceContext;
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
        return new CustomerModel(customer, info);
    }

    @Path("source-evidences")
    public SourceEvidencesApi sourceEvidences(@Context ResourceContext context) {
        return context.initResource(new SourceEvidencesApi(customer));
    }

    @Path("accounts")
    public AccountsApi accounts() {
        return new AccountsApi(customer);
    }
}
