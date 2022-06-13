package reengineering.ddd.accounting.mybatis.model;

import reengineering.ddd.accounting.model.Account;
import reengineering.ddd.accounting.model.Customer;
import reengineering.ddd.archtype.Many;
import reengineering.ddd.mybatis.EntityList;

import java.util.List;
import java.util.Optional;

public class CustomerAccounts implements Customer.Accounts {
    private List<Account> accounts;

    @Override
    public Many<Account> findAll() {
        return new EntityList<>(accounts);
    }

    @Override
    public Optional<Account> findByIdentity(String identifier) {
        return accounts.stream().filter(a -> a.identity().equals(identifier)).findFirst();
    }
}
