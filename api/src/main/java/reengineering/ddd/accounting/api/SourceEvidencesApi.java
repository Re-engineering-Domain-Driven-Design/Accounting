package reengineering.ddd.accounting.api;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import reengineering.ddd.accounting.api.representation.SourceEvidenceModel;
import reengineering.ddd.accounting.model.Customer;
import reengineering.ddd.accounting.model.SourceEvidence;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Optional;
import java.util.stream.Collectors;

public class SourceEvidencesApi {
    private Customer customer;

    public SourceEvidencesApi(Customer customer) {
        this.customer = customer;
    }

    @GET
    @Path("{evidence-id}")
    public SourceEvidenceModel findById(@PathParam("evidence-id") String id) {
        return customer.sourceEvidences().findByIdentity(id).map(SourceEvidenceModel::new)
                .orElseThrow(() -> new WebApplicationException(Response.Status.NOT_FOUND));
    }

    @GET
    public CollectionModel<SourceEvidenceModel> findAll(@Context UriInfo info) {
        URI self = info.getRequestUri();

        return CollectionModel.of(customer.sourceEvidences().findAll().stream().map(SourceEvidenceModel::new).collect(Collectors.toList()),
                Link.of(self.getPath(), "self"));
    }
}
