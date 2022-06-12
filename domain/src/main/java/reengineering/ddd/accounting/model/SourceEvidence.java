package reengineering.ddd.accounting.model;

import reengineering.ddd.accounting.description.SourceEvidenceDescription;
import reengineering.ddd.archtype.Entity;
import reengineering.ddd.archtype.EntityCollection;

public interface SourceEvidence extends Entity<String, SourceEvidenceDescription> {

    interface Transactions {
        EntityCollection<Transaction> findAll();
    }

    Transactions transactions();
}
