package reengineering.ddd.accounting.api.representation;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import reengineering.ddd.accounting.description.SalesSettlementDescription;

@JsonTypeName("sales-settlement")
public class SalesSettlementRequest implements SourceEvidenceRequest<SalesSettlementDescription> {
    @JsonUnwrapped
    private SalesSettlementDescription description;

    private SalesSettlementRequest() {
    }

    public SalesSettlementRequest(SalesSettlementDescription description) {
        this.description = description;
    }

    @Override
    public SalesSettlementDescription description() {
        return description;
    }
}
