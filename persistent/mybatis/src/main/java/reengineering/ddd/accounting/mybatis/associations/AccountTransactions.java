package reengineering.ddd.accounting.mybatis.associations;

import reengineering.ddd.accounting.description.TransactionDescription;
import reengineering.ddd.accounting.model.Account;
import reengineering.ddd.accounting.model.SourceEvidence;
import reengineering.ddd.accounting.model.Transaction;
import reengineering.ddd.accounting.mybatis.ModelMapper;
import reengineering.ddd.mybatis.database.EntityList;
import reengineering.ddd.mybatis.support.IdHolder;

import javax.inject.Inject;
import java.util.List;

public class AccountTransactions extends EntityList<String, Transaction> implements Account.Transactions {
    private String accountId;

    @Inject
    private ModelMapper mapper;

    @Override
    protected List<Transaction> findEntities(int from, int to) {
        return mapper.findTransactionsByAccountId(accountId, from, to - from);
    }

    @Override
    protected Transaction findEntity(String id) {
        return mapper.findTransactionByAccountAndId(accountId, id);
    }

    @Override
    public int size() {
        return mapper.countTransactionsInAccount(accountId);
    }

    @Override
    public Transaction add(Account account, SourceEvidence<?> evidence, TransactionDescription description) {
        IdHolder holder = new IdHolder();
        mapper.insertTransaction(holder, account.getIdentity(), evidence.getIdentity(), description);
        return mapper.findTransactionByAccountAndId(accountId, holder.id());
    }
}
