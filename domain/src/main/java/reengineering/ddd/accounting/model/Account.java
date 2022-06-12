package reengineering.ddd.accounting.model;

import reengineering.ddd.accounting.description.AccountDescription;
import reengineering.ddd.accounting.description.TransactionDescription;
import reengineering.ddd.archtype.Entity;
import reengineering.ddd.archtype.EntityCollection;

public class Account implements Entity<String, AccountDescription> {
    private String identity;
    private AccountDescription description;

    private Transactions transactions;

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

    public Transactions transactions() {
        return transactions;
    }

    public interface Transactions {
        EntityCollection<Transaction> findAll();

        Transaction add(TransactionDescription description);
    }
}
