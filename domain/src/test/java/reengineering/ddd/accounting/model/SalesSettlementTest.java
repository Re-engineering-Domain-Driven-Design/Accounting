package reengineering.ddd.accounting.model;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reengineering.ddd.accounting.description.SalesSettlementDescription;
import reengineering.ddd.accounting.description.TransactionDescription;
import reengineering.ddd.accounting.description.basic.Amount;
import reengineering.ddd.accounting.description.basic.Ref;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SalesSettlementTest {
    @Test
    public void should_generate_transaction_based_on_details() {
        SalesSettlement salesSettlement = new SalesSettlement("SS-001", new SalesSettlementDescription(new Ref<>("ORD-001"),
                Amount.cny("1000.00"), new Ref<>("CASH-001"),
                new SalesSettlementDescription.Detail(Amount.cny("700.00")),
                new SalesSettlementDescription.Detail(Amount.cny("300.00"))
        ), Mockito.mock(SourceEvidence.Transactions.class));

        Map<String, List<TransactionDescription>> transactions = salesSettlement.toTransactions();

        assertTrue(transactions.containsKey("CASH-001"));
        assertEquals(1, transactions.size());

        List<TransactionDescription> descriptions = transactions.get("CASH-001");
        assertEquals(2, descriptions.size());
        assertEquals(Amount.cny("700.00"), descriptions.get(0).amount());
        assertEquals(Amount.cny("300.00"), descriptions.get(1).amount());
    }
}
