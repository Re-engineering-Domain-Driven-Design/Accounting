package reengineering.ddd.accounting.mybatis.model;

import reengineering.ddd.accounting.description.SourceEvidenceDescription;
import reengineering.ddd.accounting.model.Customer;
import reengineering.ddd.accounting.model.SourceEvidence;
import reengineering.ddd.accounting.mybatis.mapper.SourceEvidenceMapper;
import reengineering.ddd.archtype.EntityCollection;
import reengineering.ddd.mybatis.EntityList;

import javax.inject.Inject;
import java.util.Optional;

public class CustomerSourceEvidences implements Customer.SourceEvidences {
    private String customerId;

    @Inject
    private SourceEvidenceMapper mapper;

    @Override
    public EntityCollection<SourceEvidence<?>> findAll() {
        return new EntityList<>(mapper.findByCustomerId(customerId));
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
