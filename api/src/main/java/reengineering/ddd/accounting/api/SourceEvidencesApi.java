package reengineering.ddd.accounting.api;

import org.springframework.hateoas.CollectionModel;
import reengineering.ddd.accounting.api.representation.SourceEvidenceModel;
import reengineering.ddd.accounting.model.Customer;

import javax.ws.rs.GET;
import java.util.stream.Collectors;

public class SourceEvidencesApi {
    private Customer customer;

    public SourceEvidencesApi(Customer customer) {
        this.customer = customer;
    }

    @GET
    public CollectionModel<SourceEvidenceModel> findAll() {
        return CollectionModel.of(customer.sourceEvidences().findAll().stream().map(SourceEvidenceModel::new).collect(Collectors.toList()));
    }
}
