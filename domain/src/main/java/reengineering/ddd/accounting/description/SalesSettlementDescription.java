package reengineering.ddd.accounting.description;

import reengineering.ddd.accounting.description.basic.Amount;
import reengineering.ddd.accounting.description.basic.Ref;

import java.util.List;

public class SalesSettlementDescription implements SourceEvidenceDescription {
    private Ref<String> order;

    private Amount total;

    private Ref<String> account;

    private List<Detail> details;

    private SalesSettlementDescription() {
    }

    public SalesSettlementDescription(Ref<String> order, Amount total, Ref<String> account, Detail... details) {
        this.order = order;
        this.total = total;
        this.account = account;
        this.details = List.of(details);
    }

    public Ref<String> order() {
        return order;
    }

    public Amount total() {
        return total;
    }

    public Ref<String> account() {
        return account;
    }

    public List<Detail> details() {
        return details;
    }

    public record Detail(Amount amount) {
    }
}
