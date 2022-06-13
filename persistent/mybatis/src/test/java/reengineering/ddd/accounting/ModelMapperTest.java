package reengineering.ddd.accounting;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import reengineering.ddd.accounting.description.SalesSettlementDescription;
import reengineering.ddd.accounting.description.basic.Amount;
import reengineering.ddd.accounting.model.*;
import reengineering.ddd.accounting.mybatis.ModelMapper;
import reengineering.ddd.archtype.Many;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MybatisTest
public class ModelMapperTest {
    @Inject
    private ModelMapper mapper;
    @Inject
    private TestDataMapper testData;

    private String detailId = id();
    private String customerId = id();
    private String orderId = "ORD-001";
    private String accountId = id();
    private String evidenceId = id();
    private String transactionId = id();
    private LocalDateTime createdAt = LocalDateTime.now();

    private static String id() {
        return String.valueOf(new Random().nextInt(100000));
    }

    @BeforeEach
    public void before() {
        testData.insertCustomer(customerId, "John Smith", "john.smith@email.com");
        testData.insertAccounts(accountId, customerId, 100.00, "CNY");
        testData.insertTransaction(transactionId, accountId, evidenceId, 100.00, "CNY", createdAt);
        testData.insertSourceEvidence(evidenceId, customerId, "sales-settlement");
        testData.insertSalesSettlement(evidenceId, orderId, accountId, 100.00, "CNY");
        testData.insertSalesSettlementDetail(detailId, evidenceId, 100.00, "CNY");
    }

    @Test
    public void should_find_customer_by_id() {
        Customer customer = mapper.findCustomerById(customerId);
        assertEquals(customerId, customer.identity());
        assertEquals("John Smith", customer.description().name());
        assertEquals("john.smith@email.com", customer.description().email());
    }

    @Test
    public void should_assign_source_evidences_association() {
        Customer customer = mapper.findCustomerById(customerId);

        Many<SourceEvidence<?>> evidences = customer.sourceEvidences().findAll();

        assertEquals(1, evidences.size());
    }

    @Test
    public void should_assign_accounts_association() {
        Customer customer = mapper.findCustomerById(customerId);
        assertEquals(1, customer.accounts().findAll().size());

        Account customerAccount = customer.accounts().findByIdentity(accountId).get();
        assertEquals(Amount.cny("100.00"), customerAccount.description().current());
    }

    @Test
    public void should_read_sales_settlement_as_source_evidence() {
        List<SourceEvidence<?>> evidences = mapper.findSourceEvidencesByCustomerId(customerId);
        assertEquals(1, evidences.size());

        SalesSettlement salesSettlement = (SalesSettlement) evidences.get(0);
        assertEquals(evidenceId, salesSettlement.identity());
        assertEquals(orderId, salesSettlement.description().getOrder().id());
        assertEquals(accountId, salesSettlement.description().getAccount().id());
        assertEquals(Amount.cny("100.00"), salesSettlement.description().getTotal());

        assertEquals(1, salesSettlement.description().getDetails().size());

        SalesSettlementDescription.Detail detail = salesSettlement.description().getDetails().get(0);
        assertEquals(Amount.cny("100.00"), detail.getAmount());
    }

    @Test
    public void should_find_transactions_by_account_id() {
        List<Transaction> transactions = mapper.findTransactionsByAccountId(accountId);

        assertEquals(1, transactions.size());
        Transaction transaction = transactions.get(0);

        assertEquals(Amount.cny("100.00"), transaction.description().amount());
        assertEquals(createdAt, transaction.description().createdAt());

        SourceEvidence evidence = transaction.sourceEvidence();
        assertEquals(evidenceId, evidence.identity());
        assertTrue(evidence instanceof SalesSettlement);

        assertEquals(accountId, transaction.account().identity());
    }

    @Test
    public void should_find_transactions_by_source_evidence_id() {
        List<Transaction> transactions = mapper.findTransactionsBySourceEvidenceId(evidenceId);

        assertEquals(1, transactions.size());
        Transaction transaction = transactions.get(0);

        assertEquals(Amount.cny("100.00"), transaction.description().amount());
        assertEquals(createdAt, transaction.description().createdAt());

        SourceEvidence evidence = transaction.sourceEvidence();
        assertEquals(evidenceId, evidence.identity());
        assertTrue(evidence instanceof SalesSettlement);

        assertEquals(accountId, transaction.account().identity());
    }

    @Test
    public void should_find_transaction_from_account() {
        Customer customer = mapper.findCustomerById(customerId);
        Account account = customer.accounts().findByIdentity(accountId).get();

        Many<Transaction> transactions = account.transactions().findAll();

        assertEquals(1, transactions.size());
        assertEquals(transactionId, transactions.stream().toList().get(0).identity());
    }

    @Test
    public void should_find_transaction_from_source_evidences() {
        SourceEvidence<?> evidence = mapper.findSourceEvidencesByCustomerId(customerId).get(0);

        Many<Transaction> transactions = evidence.transactions().findAll();

        assertEquals(1, transactions.size());
        assertEquals(transactionId, transactions.stream().toList().get(0).identity());
    }

}
