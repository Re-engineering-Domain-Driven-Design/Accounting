package reengineering.ddd.accounting.api.representation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import reengineering.ddd.accounting.api.ApiTemplates;
import reengineering.ddd.accounting.description.CustomerDescription;
import reengineering.ddd.accounting.model.Customer;

import javax.ws.rs.core.UriInfo;

public class CustomerModel extends RepresentationModel<CustomerModel> {
    @JsonProperty
    private String id;
    @JsonUnwrapped
    private CustomerDescription description;

    public CustomerModel(Customer customer, UriInfo info) {
        this.id = customer.identity();
        this.description = customer.description();
        add(Link.of(ApiTemplates.customer(info).build(customer.identity()).getPath(), "self"));
    }
}
