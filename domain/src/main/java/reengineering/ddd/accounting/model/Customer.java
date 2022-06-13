package reengineering.ddd.accounting.model;

import reengineering.ddd.accounting.description.CustomerDescription;
import reengineering.ddd.accounting.description.SourceEvidenceDescription;
import reengineering.ddd.archtype.HasMany;
import reengineering.ddd.archtype.Entity;

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

    public Accounts accounts() {
        return accounts;
    }

    public interface SourceEvidences extends HasMany<String, SourceEvidence<?>> {
        SourceEvidence<?> add(SourceEvidenceDescription description);
    }

    public interface Accounts extends HasMany<String, Account> {
    }
}
