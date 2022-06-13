package reengineering.ddd.accounting.api.representation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;
import reengineering.ddd.accounting.api.ApiTemplates;
import reengineering.ddd.accounting.description.TransactionDescription;
import reengineering.ddd.accounting.model.Customer;
import reengineering.ddd.accounting.model.Transaction;

import javax.ws.rs.core.UriInfo;

@Relation(collectionRelation = "transactions")
public class TransactionModel extends RepresentationModel<TransactionModel> {
    @JsonProperty
    private String id;
    @JsonUnwrapped
    private TransactionDescription description;

    public TransactionModel(Customer customer, Transaction transaction, UriInfo info) {
        this.id = transaction.identity();
        this.description = transaction.description();
    }

    public static TransactionModel withEvidenceLink(Customer customer, Transaction transaction, UriInfo info) {
        return new TransactionModel(customer, transaction, info).add(Link.of(ApiTemplates.sourceEvidence(info).build(customer.identity(), transaction.sourceEvidence().identity()).getPath(), "source-evidence"));
    }
}
