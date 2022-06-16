package reengineering.ddd.accounting.api;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import reengineering.ddd.accounting.api.representation.SourceEvidenceModel;
import reengineering.ddd.accounting.api.representation.SourceEvidenceReader;
import reengineering.ddd.accounting.api.representation.TransactionModel;
import reengineering.ddd.accounting.model.Account;
import reengineering.ddd.accounting.model.Customer;
import reengineering.ddd.accounting.model.SourceEvidence;
import reengineering.ddd.archtype.Many;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.stream.Collectors;

import static reengineering.ddd.accounting.api.ApiTemplates.accountTransactions;
import static reengineering.ddd.accounting.api.ApiTemplates.sourceEvidences;

public class SourceEvidencesApi {
    private Customer customer;

    @Inject
    private SourceEvidenceReader reader;

    public SourceEvidencesApi(Customer customer) {
        this.customer = customer;
    }

    @GET
    @Path("{evidence-id}")
    public SourceEvidenceModel findById(@PathParam("evidence-id") String id,
                                        @Context UriInfo info) {
        return customer.sourceEvidences().findByIdentity(id).map(evidence -> SourceEvidenceModel.of(customer, evidence, info))
                .orElseThrow(() -> new WebApplicationException(Response.Status.NOT_FOUND));
    }

    @GET
    public CollectionModel<SourceEvidenceModel> findAll(@Context UriInfo info, @DefaultValue("0") @QueryParam("page") int page) {
        return new Pagination<>(customer.sourceEvidences().findAll(), 40).page(page,
                evidence -> SourceEvidenceModel.simple(customer, evidence, info),
                p -> sourceEvidences(info).queryParam("page", p).build(customer.getIdentity()));
    }

    @POST
    public Response create(String json, @Context UriInfo info) {
        SourceEvidence evidence = customer.add(reader.read(json)
                .orElseThrow(() -> new WebApplicationException(Response.Status.NOT_ACCEPTABLE)).description());
        return Response.created(ApiTemplates.sourceEvidence(info).build(customer.getIdentity(), evidence.getIdentity())).build();
    }
}
