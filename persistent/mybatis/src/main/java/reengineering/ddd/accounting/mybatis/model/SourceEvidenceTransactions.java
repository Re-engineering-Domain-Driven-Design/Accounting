package reengineering.ddd.accounting.mybatis.model;

import reengineering.ddd.accounting.model.SourceEvidence;
import reengineering.ddd.accounting.model.Transaction;
import reengineering.ddd.archtype.EntityCollection;

public class SourceEvidenceTransactions implements SourceEvidence.Transactions {

    @Override
    public EntityCollection<Transaction> findAll() {
        return null;
    }
}
