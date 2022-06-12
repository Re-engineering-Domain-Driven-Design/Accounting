package reengineering.ddd.accounting.api.representation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;
import reengineering.ddd.accounting.description.SourceEvidenceDescription;
import reengineering.ddd.accounting.model.SourceEvidence;

@Relation(collectionRelation = "evidences")
public class SourceEvidenceModel extends RepresentationModel<SourceEvidenceModel> {
    @JsonProperty
    private String id;

    @JsonUnwrapped
    private SourceEvidenceDescription description;

    public SourceEvidenceModel(SourceEvidence evidence) {
        this.id = evidence.identity();
        this.description = evidence.description();
    }
}
