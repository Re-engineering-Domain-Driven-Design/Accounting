package reengineering.ddd.accounting.model;

import reengineering.ddd.accounting.description.SalesSettlementDescription;
import reengineering.ddd.accounting.description.TransactionDescription;
import reengineering.ddd.archtype.HasMany;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class SalesSettlement implements SourceEvidence<SalesSettlementDescription> {
    private String identity;
    private SalesSettlementDescription description;

    private HasMany<String, Transaction> transactions;

    public SalesSettlement() {
    }

    public SalesSettlement(String identity, SalesSettlementDescription description, HasMany<String, Transaction> transactions) {
        this.identity = identity;
        this.description = description;
        this.transactions = transactions;
    }

    @Override
    public String getIdentity() {
        return identity;
    }

    @Override
    public SalesSettlementDescription getDescription() {
        return description;
    }

    @Override
    public HasMany<String, Transaction> transactions() {
        return transactions;
    }

    @Override
    public Map<String, List<TransactionDescription>> toTransactions() {
        return Map.of(getDescription().getAccount().id(),
                getDescription().getDetails().stream().map(detail -> new TransactionDescription(detail.getAmount(), LocalDateTime.now())).toList());
    }
}
