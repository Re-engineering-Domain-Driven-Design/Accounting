package reengineering.ddd.accounting;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.context.annotation.Import;
import reengineering.ddd.accounting.description.basic.Amount;
import reengineering.ddd.accounting.model.*;
import reengineering.ddd.accounting.mybatis.associations.Customers;
import reengineering.ddd.archtype.Many;

import javax.inject.Inject;
import java.util.Iterator;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MybatisTest
@Import(FlywayConfig.class)
@ExtendWith(TestDataSetup.class)
public class AssociationsTest {

    @Inject
    private Customers customers;

    private String customerId = "1";
    private String accountId = "1";

    private Customer customer;

    @BeforeEach
    public void setup() {
        customer = customers.findById(customerId).get();
    }


    @Test
    public void should_find_customer_by_id() {
        assertEquals("1", customer.getIdentity());
        assertEquals("John Smith", customer.getDescription().name());
        assertEquals("john.smith@email.com", customer.getDescription().email());
    }

    @Test
    public void should_not_find_customer_if_not_exist() {
        assertTrue(customers.findById("-1").isEmpty());
    }

    @Test
    public void should_get_accounts() {
        assertEquals(1, customer.accounts().findAll().size());
    }

    @Test
    public void should_find_account_by_id() {
        assertTrue(customer.accounts().findByIdentity(accountId).isPresent());
    }

    @Test
    public void should_not_find_account_if_not_exist() {
        assertTrue(customer.accounts().findByIdentity("-1").isEmpty());
    }

    @Test
    public void should_get_transactions_in_account() {
        Account account = customer.accounts().findByIdentity(accountId).get();
        assertEquals(5000, account.transactions().findAll().size());
    }

    @Test
    public void should_find_transaction_from_account() {
        Account account = customer.accounts().findByIdentity(accountId).get();
        String transactionId = account.transactions().findAll().iterator().next().getIdentity();
        Transaction transaction = account.transactions().findByIdentity(transactionId).get();

        assertEquals(Amount.cny("100.00"), transaction.getDescription().amount());
        SalesSettlement evidence = (SalesSettlement) transaction.sourceEvidence();
        assertEquals(Amount.cny("500.00"), evidence.getDescription().getTotal());
    }

    @Test
    public void should_not_find_transaction_from_account_if_not_exist() {
        Account account = customer.accounts().findByIdentity(accountId).get();
        assertTrue(account.transactions().findByIdentity("-1").isEmpty());
    }

    @Test
    public void should_find_transactions_in_account() {
        Account account = customer.accounts().findByIdentity(accountId).get();

        Many<Transaction> transactions = account.transactions().findAll().subCollection(0, 100);
        assertEquals(100, transactions.size());
    }

    @Test
    public void should_iterate_over_all_transaction_in_account() {
        Account account = customer.accounts().findByIdentity(accountId).get();

        Iterator<Transaction> iterator = account.transactions().findAll().iterator();
        int count = 0;
        while (iterator.hasNext()) {
            iterator.next();
            count++;
        }

        assertEquals(5000, count);
    }

    @Test
    public void should_get_source_evidences_of_customer() {
        assertEquals(1000, customer.sourceEvidences().findAll().size());
    }

    @Test
    public void should_find_source_evidences_of_customer() {
        Many<SourceEvidence<?>> evidences = customer.sourceEvidences().findAll().subCollection(0, 100);
        assertEquals(100, evidences.size());
    }

    @Test
    public void should_find_source_evidence_of_customer() {
        String identity = customer.sourceEvidences().findAll().iterator().next().getIdentity();
        SourceEvidence<?> sourceEvidence = customer.sourceEvidences().findByIdentity(identity).get();

        assertEquals(5, sourceEvidence.transactions().findAll().size());
        assertEquals(3, sourceEvidence.transactions().findAll().subCollection(0, 3).size());
    }

    @Test
    public void should_not_find_source_evidence_of_customer_if_not_exist() {
        Optional<SourceEvidence<?>> sourceEvidence = customer.sourceEvidences().findByIdentity("-1");
        assertTrue(sourceEvidence.isEmpty());
    }
}
