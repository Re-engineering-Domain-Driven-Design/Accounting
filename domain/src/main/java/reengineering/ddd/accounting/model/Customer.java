package reengineering.ddd.accounting.model;

import reengineering.ddd.accounting.description.CustomerDescription;
import reengineering.ddd.accounting.description.SalesSettlementDescription;
import reengineering.ddd.accounting.description.SourceEvidenceDescription;
import reengineering.ddd.accounting.description.TransactionDescription;
import reengineering.ddd.archtype.HasMany;
import reengineering.ddd.archtype.Entity;

import java.util.List;
import java.util.Map;

public class Customer implements Entity<String, CustomerDescription> {
    private String identity;
    private CustomerDescription description;

    private SourceEvidences sourceEvidences;

    private Accounts accounts;

    public Customer(String identity, CustomerDescription description,
                    SourceEvidences sourceEvidences, Accounts accounts) {
        this.identity = identity;
        this.description = description;
        this.sourceEvidences = sourceEvidences;
        this.accounts = accounts;
    }

    private Customer() {
    }

    public String identity() {
        return identity;
    }

    public CustomerDescription description() {
        return description;
    }

    public SourceEvidences sourceEvidences() {
        return sourceEvidences;
    }

    public HasMany<String, Account> accounts() {
        return accounts;
    }

    public SourceEvidence<?> add(SourceEvidenceDescription description) {
        SourceEvidence<?> evidence = sourceEvidences.add(description);
        Map<String, List<TransactionDescription>> transactions = evidence.toTransactions();
        for (String accountId : transactions.keySet()) {
            Account account = accounts.findByIdentity(accountId).orElseThrow(() -> new AccountNotFoundException(accountId));
            transactions.get(accountId).forEach(account.transactions()::add);
        }
        return evidence;
    }

    public interface SourceEvidences extends HasMany<String, SourceEvidence<?>> {
        SourceEvidence<?> add(SourceEvidenceDescription description);
    }

    public interface Accounts extends HasMany<String, Account> {
    }
}
