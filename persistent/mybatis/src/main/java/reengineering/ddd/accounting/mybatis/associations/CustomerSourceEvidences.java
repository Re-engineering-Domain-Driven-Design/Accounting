package reengineering.ddd.accounting.mybatis.associations;

import reengineering.ddd.accounting.description.SourceEvidenceDescription;
import reengineering.ddd.accounting.model.Customer;
import reengineering.ddd.accounting.model.SourceEvidence;
import reengineering.ddd.accounting.mybatis.ModelMapper;
import reengineering.ddd.mybatis.database.EntityList;
import reengineering.ddd.mybatis.support.IdHolder;

import javax.inject.Inject;
import java.util.List;

public class CustomerSourceEvidences extends EntityList<String, SourceEvidence<?>> implements Customer.SourceEvidences {
    private String customerId;

    @Inject
    private ModelMapper mapper;

    @Override
    protected List<SourceEvidence<?>> findAllEntities() {
        return mapper.findSourceEvidencesByCustomerId(customerId);
    }

    @Override
    protected List<SourceEvidence<?>> findEntities(int from, int to) {
        return mapper.findSourceEvidencesByCustomerId(customerId);
    }

    @Override
    protected SourceEvidence<?> findEntity(String id) {
        return mapper.findSourceEvidenceByCustomerAndId(customerId, id);
    }

    @Override
    public SourceEvidence<?> add(SourceEvidenceDescription description) {
        IdHolder holder = new IdHolder();
        mapper.insertSourceEvidence(holder, customerId, description);
        mapper.insertSourceEvidenceDescription(holder.id(), description);
        return mapper.findSourceEvidenceByCustomerAndId(customerId, holder.id());
    }

    @Override
    public int size() {
        return mapper.countSourceEvidencesByCustomer(customerId);
    }
}
