package reengineering.ddd.accounting.mybatis.model;

import reengineering.ddd.accounting.model.SourceEvidence;
import reengineering.ddd.accounting.model.Transaction;
import reengineering.ddd.accounting.mybatis.ModelMapper;
import reengineering.ddd.archtype.EntityCollection;
import reengineering.ddd.mybatis.EntityList;

import javax.inject.Inject;
import java.util.Optional;

public class SourceEvidenceTransactions implements SourceEvidence.Transactions {
    private String sourceEvidenceId;

    @Inject
    private ModelMapper mapper;

    @Override
    public EntityCollection<Transaction> findAll() {
        return new EntityList<>(mapper.findTransactionsBySourceEvidenceId(sourceEvidenceId));
    }

    @Override
    public Optional<Transaction> findByIdentity(String identifier) {
        return Optional.empty();
    }
}
