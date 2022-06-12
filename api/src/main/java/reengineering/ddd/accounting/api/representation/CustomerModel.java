package reengineering.ddd.accounting.api.representation;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import reengineering.ddd.accounting.description.CustomerDescription;
import reengineering.ddd.accounting.model.Customer;

import java.util.Arrays;

public class CustomerModel extends RepresentationModel<CustomerModel> {
    private String id;
    @JsonUnwrapped
    private CustomerDescription description;

    public CustomerModel(Customer customer, Link... links) {
        super(Arrays.asList(links));
        this.id = customer.identity();
        this.description = customer.description();
    }
}
