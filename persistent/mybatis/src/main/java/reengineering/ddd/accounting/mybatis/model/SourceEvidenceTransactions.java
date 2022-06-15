package reengineering.ddd.accounting.mybatis.model;

import reengineering.ddd.accounting.model.SourceEvidence;
import reengineering.ddd.accounting.model.Transaction;
import reengineering.ddd.accounting.mybatis.ModelMapper;
import reengineering.ddd.mybatis.database.EntityList;

import javax.inject.Inject;
import java.util.List;

public class SourceEvidenceTransactions extends EntityList<String, Transaction> implements SourceEvidence.Transactions {
    private String sourceEvidenceId;

    @Inject
    private ModelMapper mapper;

    @Override
    protected List<Transaction> findAllEntities() {
        return mapper.findTransactionsBySourceEvidenceId(sourceEvidenceId);
    }

    @Override
    protected List<Transaction> findEntities(int from, int to) {
        return mapper.findTransactionsBySourceEvidenceId(sourceEvidenceId);
    }

    @Override
    protected Transaction findEntity(String id) {
        return mapper.findTransactionByEvidenceAndId(sourceEvidenceId, id);
    }

    @Override
    public int size() {
        return mapper.countTransactionsBySourceEvidence(sourceEvidenceId);
    }
}
