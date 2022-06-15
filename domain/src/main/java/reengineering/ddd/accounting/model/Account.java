package reengineering.ddd.accounting.model;

import reengineering.ddd.accounting.description.AccountDescription;
import reengineering.ddd.accounting.description.TransactionDescription;
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
    public String identity() {
        return identity;
    }

    @Override
    public AccountDescription description() {
        return description;
    }

    public HasMany<String, Transaction> transactions() {
        return transactions;
    }

    public void add(SourceEvidence<?> evidence, List<TransactionDescription> descriptions) {
        descriptions.forEach(it -> transactions.add(this, evidence, it));
    }

    public interface Transactions extends HasMany<String, Transaction> {
        Transaction add(Account account, SourceEvidence<?> evidence, TransactionDescription description);
    }
}
