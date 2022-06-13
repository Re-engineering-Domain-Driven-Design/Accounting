package reengineering.ddd.accounting.model;

import reengineering.ddd.accounting.description.SourceEvidenceDescription;
import reengineering.ddd.accounting.description.TransactionDescription;
import reengineering.ddd.archtype.Association;
import reengineering.ddd.archtype.Entity;
import reengineering.ddd.archtype.EntityCollection;

import java.util.List;
import java.util.Map;

public interface SourceEvidence<Description extends SourceEvidenceDescription> extends Entity<String, Description> {

    interface Transactions extends Association<String, Transaction> {
    }

    Transactions transactions();

    Map<String, List<TransactionDescription>> toTransactions();
}
