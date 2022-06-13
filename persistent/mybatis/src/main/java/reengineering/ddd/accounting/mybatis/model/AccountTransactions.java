package reengineering.ddd.accounting.mybatis.model;

import reengineering.ddd.accounting.description.TransactionDescription;
import reengineering.ddd.accounting.model.Account;
import reengineering.ddd.accounting.model.Transaction;
import reengineering.ddd.archtype.EntityCollection;

public class AccountTransactions implements Account.Transactions {
    @Override
    public EntityCollection<Transaction> findAll() {
        return null;
    }

    @Override
    public Transaction add(TransactionDescription description) {
        return null;
    }
}
