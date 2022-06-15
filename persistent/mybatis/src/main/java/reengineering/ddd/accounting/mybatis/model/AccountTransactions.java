package reengineering.ddd.accounting.mybatis.model;

import reengineering.ddd.accounting.description.TransactionDescription;
import reengineering.ddd.accounting.model.Account;
import reengineering.ddd.accounting.model.SourceEvidence;
import reengineering.ddd.accounting.model.Transaction;
import reengineering.ddd.accounting.mybatis.ModelMapper;
import reengineering.ddd.archtype.Many;
import reengineering.ddd.mybatis.EntityList;
import reengineering.ddd.mybatis.support.IdHolder;

import javax.inject.Inject;
import java.util.Optional;

public class AccountTransactions implements Account.Transactions {
    private String accountId;

    @Inject
    private ModelMapper mapper;

    @Override
    public Many<Transaction> findAll() {
        return new EntityList<>(mapper.findTransactionsByAccountId(accountId));
    }

    @Override
    public Optional<Transaction> findByIdentity(String identifier) {
        return Optional.of(mapper.findTransactionById(identifier));
    }

    @Override
    public Transaction add(Account account, SourceEvidence<?> evidence, TransactionDescription description) {
        IdHolder holder = new IdHolder();
        mapper.insertTransaction(holder, account.identity(), evidence.identity(), description);
        return mapper.findTransactionById(holder.id());
    }
}
