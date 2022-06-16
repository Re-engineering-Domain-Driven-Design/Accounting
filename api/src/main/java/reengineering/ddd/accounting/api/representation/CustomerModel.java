package reengineering.ddd.accounting.api.representation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import reengineering.ddd.accounting.api.ApiTemplates;
import reengineering.ddd.accounting.description.CustomerDescription;
import reengineering.ddd.accounting.model.Customer;

import javax.ws.rs.core.UriInfo;

import static reengineering.ddd.accounting.api.ApiTemplates.*;

public class CustomerModel extends RepresentationModel<CustomerModel> {
    @JsonProperty
    private String id;
    @JsonUnwrapped
    private CustomerDescription description;

    public CustomerModel(Customer customer, UriInfo info) {
        this.id = customer.getIdentity();
        this.description = customer.getDescription();
        add(Link.of(customer(info).build(customer.getIdentity()).getPath(), "self"));
        add(Link.of(sourceEvidences(info).build(customer.getIdentity()).getPath(), "source-evidences"));
        customer.accounts().findAll().forEach(a ->
                add(Link.of(accountTransactions(info).build(customer.getIdentity(), a.getIdentity()).getPath(), "account-" + a.getIdentity() + "-transactions")));
    }
}
