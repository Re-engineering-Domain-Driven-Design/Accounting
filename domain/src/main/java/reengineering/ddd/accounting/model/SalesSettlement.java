package reengineering.ddd.accounting.model;

import reengineering.ddd.accounting.description.SalesSettlementDescription;
import reengineering.ddd.accounting.description.SourceEvidenceDescription;

public class SalesSettlement implements SourceEvidence {
    private String identity;
    private SalesSettlementDescription description;

    public SalesSettlement(String identity, SalesSettlementDescription description) {
        this.identity = identity;
        this.description = description;
    }

    @Override
    public String identity() {
        return identity;
    }

    @Override
    public SourceEvidenceDescription description() {
        return description;
    }
}
