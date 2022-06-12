package reengineering.ddd.accounting.model;

import reengineering.ddd.accounting.description.TransactionDescription;
import reengineering.ddd.archtype.Entity;

import java.util.function.Supplier;

public class Transaction implements Entity<String, TransactionDescription> {
    private String identity;
    private TransactionDescription description;

    private Supplier<Account> account;
    private Supplier<SourceEvidence> sourceEvidence;

    public Transaction(String identity, TransactionDescription description,
                       Supplier<Account> account,
                       Supplier<SourceEvidence> sourceEvidence) {
        this.identity = identity;
        this.description = description;
        this.account = account;
        this.sourceEvidence = sourceEvidence;
    }

    @Override
    public String identity() {
        return identity;
    }

    @Override
    public TransactionDescription description() {
        return description;
    }

    public SourceEvidence sourceEvidence() {
        return sourceEvidence.get();
    }

    public Account account() {
        return account.get();
    }
}
