package reengineering.ddd.accounting.model;

import reengineering.ddd.accounting.description.TransactionDescription;
import reengineering.ddd.archtype.Entity;
import reengineering.ddd.archtype.EntityReference;

public class Transaction implements Entity<String, TransactionDescription> {
    private String identity;
    private TransactionDescription description;

    private EntityReference<SourceEvidence<?>> sourceEvidence;

    private EntityReference<Account> account;

    private Transaction() {
    }

    public Transaction(String identity, TransactionDescription description,
                       EntityReference<Account> account, EntityReference<SourceEvidence<?>> sourceEvidence) {
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
