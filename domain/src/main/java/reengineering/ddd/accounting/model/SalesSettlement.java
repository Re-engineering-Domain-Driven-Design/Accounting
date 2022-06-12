package reengineering.ddd.accounting.model;

import reengineering.ddd.accounting.description.SalesSettlementDescription;
import reengineering.ddd.accounting.description.SourceEvidenceDescription;

public class SalesSettlement implements SourceEvidence {
    private String identity;
    private SalesSettlementDescription description;

    private Transactions transactions;

    public SalesSettlement(String identity, SalesSettlementDescription description, Transactions transactions) {
        this.identity = identity;
        this.description = description;
        this.transactions = transactions;
    }

    @Override
    public String identity() {
        return identity;
    }

    @Override
    public SourceEvidenceDescription description() {
        return description;
    }

    @Override
    public Transactions transactions() {
        return transactions;
    }
}
