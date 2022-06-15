package reengineering.ddd.accounting.model;

import reengineering.ddd.accounting.description.TransactionDescription;
import reengineering.ddd.archtype.Entity;
import reengineering.ddd.archtype.HasOne;

public class Transaction implements Entity<String, TransactionDescription> {
    private String identity;
    private TransactionDescription description;

    private HasOne<SourceEvidence<?>> sourceEvidence;

    private HasOne<Account> account;

    private Transaction() {
    }

    public Transaction(String identity, TransactionDescription description,
                       HasOne<Account> account, HasOne<SourceEvidence<?>> sourceEvidence) {
        this.identity = identity;
        this.description = description;
        this.account = account;
        this.sourceEvidence = sourceEvidence;
    }

    @Override
    public String getIdentity() {
        return identity;
    }

    @Override
    public TransactionDescription getDescription() {
        return description;
    }

    public SourceEvidence sourceEvidence() {
        return sourceEvidence.get();
    }

    public Account account() {
        return account.get();
    }
}
