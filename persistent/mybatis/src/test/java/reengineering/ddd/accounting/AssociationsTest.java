package reengineering.ddd.accounting;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Import;
import reengineering.ddd.accounting.description.basic.Amount;
import reengineering.ddd.accounting.model.*;
import reengineering.ddd.accounting.mybatis.associations.Customers;

import javax.inject.Inject;

import java.time.LocalDateTime;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MybatisTest
@Import(FlywayConfig.class)
public class AssociationsTest {

    @Inject
    private Customers customers;

    @Inject
    private TestDataMapper testData;

    @Inject
    private FlywayMigrationStrategy strategy;

    private String customerId = "1";
    private String accountId = "1";

    private Customer customer;

    @BeforeEach
    public void setup() {
        testData.insertCustomer(customerId, "John Smith", "john.smith@email.com");
        testData.insertAccount(accountId, customerId, 100.00, "CNY");
        for (var evidence = 0; evidence < 1000; evidence++) {
            String evidenceId = String.valueOf(evidence);
            testData.insertSourceEvidence(evidenceId, customerId, "sales-settlement");

            int num = 5;

            testData.insertSalesSettlement(evidenceId, "ORD-" + new Random().nextInt(),
                    accountId, 100 * num, "CNY");

            for (var i = 0; i < num; i++) {
                testData.insertSalesSettlementDetail(String.valueOf(100 * evidence + i), evidenceId, 100.00, "CNY");
                testData.insertTransaction(String.valueOf(100 * evidence + i), accountId, evidenceId, 100.00, "CNY", LocalDateTime.now());
            }
        }
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
        Transaction transaction = account.transactions().findByIdentity("1").get();

        assertEquals(Amount.cny("100.00"), transaction.getDescription().amount());
        SalesSettlement evidence = (SalesSettlement) transaction.sourceEvidence();
        assertEquals(Amount.cny("500.00"), evidence.getDescription().getTotal());
    }

    @Test
    public void should_not_find_transaction_from_account_if_not_exist() {
        Account account = customer.accounts().findByIdentity(accountId).get();
        assertTrue(account.transactions().findByIdentity("-1").isEmpty());
    }

}
