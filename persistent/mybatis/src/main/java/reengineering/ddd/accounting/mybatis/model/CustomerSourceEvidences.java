package reengineering.ddd.accounting.mybatis.model;

import reengineering.ddd.accounting.description.SourceEvidenceDescription;
import reengineering.ddd.accounting.model.Customer;
import reengineering.ddd.accounting.model.SourceEvidence;
import reengineering.ddd.accounting.mybatis.ModelMapper;
import reengineering.ddd.archtype.Many;
import reengineering.ddd.mybatis.EntityList;
import reengineering.ddd.mybatis.support.IdHolder;

import javax.inject.Inject;
import java.util.Optional;

public class CustomerSourceEvidences implements Customer.SourceEvidences {
    private String customerId;

    @Inject
    private ModelMapper mapper;

    @Override
    public Many<SourceEvidence<?>> findAll() {
        return new EntityList<>(mapper.findSourceEvidencesByCustomerId(customerId));
    }

    @Override
    public Optional<SourceEvidence<?>> findByIdentity(String identifier) {
        return Optional.of(mapper.findSourceEvidenceById(identifier));
    }

    @Override
    public SourceEvidence<?> add(SourceEvidenceDescription description) {
        IdHolder holder = new IdHolder();
        mapper.insertSourceEvidence(holder, customerId, description);
        mapper.insertSourceEvidenceDescription(holder.id(), description);
        return mapper.findSourceEvidenceById(holder.id());
    }
}
