package reengineering.ddd.accounting.mybatis.associations;

import reengineering.ddd.accounting.model.Account;
import reengineering.ddd.accounting.model.Customer;
import reengineering.ddd.accounting.mybatis.ModelMapper;
import reengineering.ddd.mybatis.memory.EntityList;

import javax.inject.Inject;

public class CustomerAccounts extends EntityList<String, Account> implements Customer.Accounts {

    @Inject
    private ModelMapper mapper;

    private String customerId;

    @Override
    public void update(Account account, Account.AccountChange change) {
        mapper.updateAccount(customerId, account.getIdentity(), change);
    }
}
