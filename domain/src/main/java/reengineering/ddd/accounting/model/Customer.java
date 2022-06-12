package reengineering.ddd.accounting.model;

import reengineering.ddd.accounting.description.CustomerDescription;
import reengineering.ddd.archtype.Entity;
import reengineering.ddd.archtype.EntityCollection;

import java.util.Optional;

public class Customer implements Entity<String, CustomerDescription> {
    private String identity;
    private CustomerDescription description;

    private SourceEvidences sourceEvidences;

    public Customer(String identity, CustomerDescription description, SourceEvidences sourceEvidences) {
        this.identity = identity;
        this.description = description;
        this.sourceEvidences = sourceEvidences;
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

    public interface SourceEvidences {

        EntityCollection<SourceEvidence> findAll();

        Optional<SourceEvidence> findByIdentity(String identifier);
    }
}
