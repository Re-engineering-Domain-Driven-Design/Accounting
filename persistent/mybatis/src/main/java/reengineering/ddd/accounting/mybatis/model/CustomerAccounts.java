package reengineering.ddd.accounting.mybatis.model;

import reengineering.ddd.accounting.model.Account;
import reengineering.ddd.accounting.model.Customer;
import reengineering.ddd.accounting.mybatis.ModelMapper;
import reengineering.ddd.mybatis.memory.EntityList;

public class CustomerAccounts extends EntityList<String, Account> implements Customer.Accounts {
    private ModelMapper mapper;

    private String customerId;

    @Override
    public void update(Account account, Account.AccountChange change) {
        mapper.updateAccount(customerId, account.identity(), change);
    }
}
