package reengineering.ddd.accounting.api.representation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;
import reengineering.ddd.accounting.api.ApiTemplates;
import reengineering.ddd.accounting.description.SourceEvidenceDescription;
import reengineering.ddd.accounting.model.Customer;
import reengineering.ddd.accounting.model.SourceEvidence;

import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.stream.Collectors;

@Relation(collectionRelation = "evidences")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SourceEvidenceModel extends RepresentationModel<SourceEvidenceModel> {
    @JsonProperty
    private String id;

    @JsonUnwrapped
    private SourceEvidenceDescription description;

    @JsonProperty
    private List<TransactionModel> transactions;

    private SourceEvidenceModel(Customer customer, SourceEvidence<?> evidence, SourceEvidenceDescription description, List<TransactionModel> transactions, UriInfo info) {
        this.id = evidence.identity();
        this.description = description;
        this.transactions = transactions;
        add(Link.of(ApiTemplates.sourceEvidence(info).build(customer.identity(), evidence.identity()).getPath(), "self"));

    }

    public static SourceEvidenceModel simple(Customer customer, SourceEvidence<?> evidence, UriInfo info) {
        return new SourceEvidenceModel(customer, evidence, null, null, info);
    }

    public static SourceEvidenceModel of(Customer customer, SourceEvidence<?> evidence, UriInfo info) {
        return new SourceEvidenceModel(customer, evidence, evidence.description(), evidence.transactions().findAll().stream().map(tx -> new TransactionModel(customer, tx, info)).collect(Collectors.toList()), info);
    }
}
