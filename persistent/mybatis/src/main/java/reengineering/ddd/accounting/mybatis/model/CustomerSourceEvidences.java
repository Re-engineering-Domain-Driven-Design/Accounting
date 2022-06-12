package reengineering.ddd.accounting.mybatis.model;

import reengineering.ddd.accounting.description.SourceEvidenceDescription;
import reengineering.ddd.accounting.model.Customer;
import reengineering.ddd.accounting.model.SourceEvidence;
import reengineering.ddd.archtype.EntityCollection;

import java.util.Optional;

public class CustomerSourceEvidences implements Customer.SourceEvidences {
    private String customerId;

    @Override
    public EntityCollection<SourceEvidence<?>> findAll() {
        return null;
    }

    @Override
    public Optional<SourceEvidence<?>> findByIdentity(String identifier) {
        return Optional.empty();
    }

    @Override
    public SourceEvidence<?> add(SourceEvidenceDescription description) {
        return null;
    }
}
