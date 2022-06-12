package reengineering.ddd.accounting.api.representation;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import reengineering.ddd.accounting.description.SourceEvidenceDescription;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(SalesSettlementRequest.class)
})
public interface SourceEvidenceRequest<Description extends SourceEvidenceDescription> {
    Description description();
}
