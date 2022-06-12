package reengineering.ddd.accounting.api.representation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;
import reengineering.ddd.accounting.description.TransactionDescription;
import reengineering.ddd.accounting.model.Transaction;

@Relation(collectionRelation = "transactions")
public class TransactionModel extends RepresentationModel<TransactionModel> {
    @JsonProperty
    private String id;
    @JsonUnwrapped
    private TransactionDescription description;

    public TransactionModel(Transaction transaction) {
        this.id = transaction.identity();
        this.description = transaction.description();
    }
}
