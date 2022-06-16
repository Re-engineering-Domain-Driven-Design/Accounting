package reengineering.ddd.accounting.mybatis.associations;

import reengineering.ddd.accounting.model.SourceEvidence;
import reengineering.ddd.accounting.model.Transaction;
import reengineering.ddd.mybatis.memory.EntityList;

public class SourceEvidenceTransactions extends EntityList<String, Transaction> implements SourceEvidence.Transactions {
}
