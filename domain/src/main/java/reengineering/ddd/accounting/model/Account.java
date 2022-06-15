package reengineering.ddd.accounting.model;

import reengineering.ddd.accounting.description.AccountDescription;
import reengineering.ddd.accounting.description.TransactionDescription;
import reengineering.ddd.accounting.description.basic.Amount;
import reengineering.ddd.archtype.Entity;
import reengineering.ddd.archtype.HasMany;

import java.util.List;

public class Account implements Entity<String, AccountDescription> {
    private String identity;
    private AccountDescription description;

    private Transactions transactions;

    private Account() {
    }

    public Account(String identity, AccountDescription description, Transactions transactions) {
        this.identity = identity;
        this.description = description;
        this.transactions = transactions;
    }

    @Override
    public String getIdentity() {
        return identity;
    }

    @Override
    public AccountDescription getDescription() {
        return description;
    }

    public HasMany<String, Transaction> transactions() {
        return transactions;
    }

    public AccountChange add(SourceEvidence<?> evidence, List<TransactionDescription> descriptions) {
        Amount changeTotal = Amount.sum(descriptions.stream().map(it -> transactions.add(this, evidence, it))
                .map(it -> it.getDescription().amount()).toArray(Amount[]::new));
        description = new AccountDescription(Amount.sum(description.current(), changeTotal));
        return new AccountChange(changeTotal);
    }

    public interface Transactions extends HasMany<String, Transaction> {
        Transaction add(Account account, SourceEvidence<?> evidence, TransactionDescription description);
    }

    public record AccountChange(Amount total) {
    }
}
