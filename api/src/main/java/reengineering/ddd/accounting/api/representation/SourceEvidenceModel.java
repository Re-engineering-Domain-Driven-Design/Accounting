package reengineering.ddd.accounting.api.representation;

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

@Relation(collectionRelation = "evidences")
public class SourceEvidenceModel extends RepresentationModel<SourceEvidenceModel> {
    @JsonProperty
    private String id;

    @JsonUnwrapped
    private SourceEvidenceDescription description;

    public SourceEvidenceModel(Customer customer, SourceEvidence evidence, UriInfo info) {
        this.id = evidence.identity();
        this.description = evidence.description();
        add(Link.of(ApiTemplates.sourceEvidence(info).build(customer.identity(), evidence.identity()).getPath(), "self"));
    }
}
