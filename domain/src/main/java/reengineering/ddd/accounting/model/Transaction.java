package reengineering.ddd.accounting.model;

import reengineering.ddd.accounting.description.TransactionDescription;
import reengineering.ddd.archtype.Entity;

public class Transaction implements Entity<String, TransactionDescription> {
    private String identity;
    private TransactionDescription description;

    public Transaction(String identity, TransactionDescription description) {
        this.identity = identity;
        this.description = description;
    }

    @Override
    public String identity() {
        return identity;
    }

    @Override
    public TransactionDescription description() {
        return description;
    }
}
