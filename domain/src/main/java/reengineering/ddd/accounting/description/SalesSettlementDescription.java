package reengineering.ddd.accounting.description;

import java.util.ArrayList;
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

    public Ref<String> getOrder() {
        return order;
    }

    public Amount getTotal() {
        return total;
    }

    public Ref<String> getAccount() {
        return account;
    }

    public List<Detail> getDetails() {
        return details;
    }

    public static class Detail {
        private Amount amount;

        public Detail(Amount amount) {
            this.amount = amount;
        }

        private Detail() {
        }

        public Amount getAmount() {
            return amount;
        }
    }
}
