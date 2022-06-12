package reengineering.ddd.accounting.api;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import reengineering.ddd.accounting.api.representation.SourceEvidenceModel;
import reengineering.ddd.accounting.model.Customer;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.stream.Collectors;

public class SourceEvidencesApi {
    private Customer customer;

    public SourceEvidencesApi(Customer customer) {
        this.customer = customer;
    }

    @GET
    @Path("{evidence-id}")
    public SourceEvidenceModel findById(@PathParam("evidence-id") String id,
                                        @Context UriInfo info) {
        return customer.sourceEvidences().findByIdentity(id).map(evidence -> new SourceEvidenceModel(customer, evidence, info))
                .orElseThrow(() -> new WebApplicationException(Response.Status.NOT_FOUND));
    }

    @GET
    public CollectionModel<SourceEvidenceModel> findAll(@Context UriInfo info) {
        return CollectionModel.of(customer.sourceEvidences().findAll().stream()
                        .map(evidence -> new SourceEvidenceModel(customer, evidence, info)).collect(Collectors.toList()),
                Link.of(ApiTemplates.sourceEvidences(info).build(customer.identity()).getPath(), "self"));
    }
}
