package reengineering.ddd.accounting.model;

import reengineering.ddd.accounting.description.SalesSettlementDescription;
import reengineering.ddd.accounting.description.TransactionDescription;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class SalesSettlement implements SourceEvidence<SalesSettlementDescription> {
    private String identity;
    private SalesSettlementDescription description;

    private Transactions transactions;

    public SalesSettlement() {
    }

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
    public SalesSettlementDescription description() {
        return description;
    }

    @Override
    public Transactions transactions() {
        return transactions;
    }

    @Override
    public Map<String, List<TransactionDescription>> toTransactions() {
        return Map.of(description.getAccount().id(),
                description.getDetails().stream().map(detail -> new TransactionDescription(detail.getAmount(), LocalDateTime.now())).toList());
    }
}
