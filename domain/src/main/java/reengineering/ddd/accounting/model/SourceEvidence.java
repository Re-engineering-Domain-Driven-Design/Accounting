package reengineering.ddd.accounting.model;

import reengineering.ddd.accounting.description.SourceEvidenceDescription;
import reengineering.ddd.archtype.Entity;
import reengineering.ddd.archtype.EntityCollection;

public interface SourceEvidence<Description extends SourceEvidenceDescription> extends Entity<String, Description> {

    interface Transactions {
        EntityCollection<Transaction> findAll();
    }

    Transactions transactions();
}
