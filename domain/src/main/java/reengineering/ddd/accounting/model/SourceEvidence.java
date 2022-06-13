package reengineering.ddd.accounting.model;

import reengineering.ddd.accounting.description.SourceEvidenceDescription;
import reengineering.ddd.accounting.description.TransactionDescription;
import reengineering.ddd.archtype.HasMany;
import reengineering.ddd.archtype.Entity;

import java.util.List;
import java.util.Map;

public interface SourceEvidence<Description extends SourceEvidenceDescription> extends Entity<String, Description> {

    interface Transactions extends HasMany<String, Transaction> {
    }

    Transactions transactions();

    Map<String, List<TransactionDescription>> toTransactions();
}
