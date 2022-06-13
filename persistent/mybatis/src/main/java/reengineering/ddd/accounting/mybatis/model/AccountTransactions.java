package reengineering.ddd.accounting.mybatis.model;

import reengineering.ddd.accounting.description.TransactionDescription;
import reengineering.ddd.accounting.model.Account;
import reengineering.ddd.accounting.model.Transaction;
import reengineering.ddd.accounting.mybatis.ModelMapper;
import reengineering.ddd.archtype.EntityCollection;
import reengineering.ddd.mybatis.EntityList;

import javax.inject.Inject;

public class AccountTransactions implements Account.Transactions {
    private String accountId;

    @Inject
    private ModelMapper mapper;

    @Override
    public EntityCollection<Transaction> findAll() {
        return new EntityList<>(mapper.findTransactionsByAccountId(accountId));
    }

    @Override
    public Transaction add(TransactionDescription description) {
        return null;
    }
}
