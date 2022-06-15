package reengineering.ddd.accounting;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import reengineering.ddd.accounting.description.SalesSettlementDescription;
import reengineering.ddd.accounting.description.TransactionDescription;
import reengineering.ddd.accounting.description.basic.Amount;
import reengineering.ddd.accounting.description.basic.Ref;
import reengineering.ddd.accounting.model.*;
import reengineering.ddd.accounting.mybatis.ModelMapper;
import reengineering.ddd.archtype.Many;
import reengineering.ddd.mybatis.support.IdHolder;

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
        testData.insertAccount(accountId, customerId, 100.00, "CNY");
        testData.insertTransaction(transactionId, accountId, evidenceId, 100.00, "CNY", createdAt);
        testData.insertSourceEvidence(evidenceId, customerId, "sales-settlement");
        testData.insertSalesSettlement(evidenceId, orderId, accountId, 100.00, "CNY");
        testData.insertSalesSettlementDetail(detailId, evidenceId, 100.00, "CNY");
    }

    @Test
    public void should_find_customer_by_id() {
        Customer customer = mapper.findCustomerById(customerId);
        assertEquals(customerId, customer.getIdentity());
        assertEquals("John Smith", customer.getDescription().name());
        assertEquals("john.smith@email.com", customer.getDescription().email());
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
        assertEquals(Amount.cny("100.00"), customerAccount.getDescription().current());
    }

    @Test
    public void should_read_sales_settlements_as_source_evidences() {
        List<SourceEvidence<?>> evidences = mapper.findSourceEvidencesByCustomerId(customerId, 0, 100);
        assertEquals(1, evidences.size());

        SalesSettlement salesSettlement = (SalesSettlement) evidences.get(0);
        assertEquals(evidenceId, salesSettlement.getIdentity());
        assertEquals(orderId, salesSettlement.getDescription().getOrder().id());
        assertEquals(accountId, salesSettlement.getDescription().getAccount().id());
        assertEquals(Amount.cny("100.00"), salesSettlement.getDescription().getTotal());

        assertEquals(1, salesSettlement.getDescription().getDetails().size());

        SalesSettlementDescription.Detail detail = salesSettlement.getDescription().getDetails().get(0);
        assertEquals(Amount.cny("100.00"), detail.getAmount());
    }

    @Test
    public void should_read_sales_settlement_as_source_evidence() {
        SalesSettlement salesSettlement = (SalesSettlement) mapper.findSourceEvidenceByCustomerAndId(customerId, evidenceId);


        assertEquals(evidenceId, salesSettlement.getIdentity());
        assertEquals(orderId, salesSettlement.getDescription().getOrder().id());
        assertEquals(accountId, salesSettlement.getDescription().getAccount().id());
        assertEquals(Amount.cny("100.00"), salesSettlement.getDescription().getTotal());

        assertEquals(1, salesSettlement.getDescription().getDetails().size());

        SalesSettlementDescription.Detail detail = salesSettlement.getDescription().getDetails().get(0);
        assertEquals(Amount.cny("100.00"), detail.getAmount());
    }

    @Test
    public void should_find_transactions_by_account_id() {
        List<Transaction> transactions = mapper.findTransactionsByAccountId(accountId, 0, 100);

        assertEquals(1, transactions.size());
        Transaction transaction = transactions.get(0);

        assertEquals(Amount.cny("100.00"), transaction.getDescription().amount());
        assertEquals(createdAt, transaction.getDescription().createdAt());

        SourceEvidence evidence = transaction.sourceEvidence();
        assertEquals(evidenceId, evidence.getIdentity());
        assertTrue(evidence instanceof SalesSettlement);

        assertEquals(accountId, transaction.account().getIdentity());
    }

    @Test
    public void should_find_transactions_by_source_evidence_id() {
        List<Transaction> transactions = mapper.findTransactionsBySourceEvidenceId(evidenceId);

        assertEquals(1, transactions.size());
        Transaction transaction = transactions.get(0);

        assertEquals(Amount.cny("100.00"), transaction.getDescription().amount());
        assertEquals(createdAt, transaction.getDescription().createdAt());

        SourceEvidence evidence = transaction.sourceEvidence();
        assertEquals(evidenceId, evidence.getIdentity());
        assertTrue(evidence instanceof SalesSettlement);

        assertEquals(accountId, transaction.account().getIdentity());
    }

    @Test
    public void should_find_transaction_from_account() {
        Customer customer = mapper.findCustomerById(customerId);
        Account account = customer.accounts().findByIdentity(accountId).get();

        Many<Transaction> transactions = account.transactions().findAll();

        assertEquals(1, transactions.size());
        assertEquals(transactionId, transactions.stream().toList().get(0).getIdentity());
    }

    @Test
    public void should_find_transaction_from_source_evidences() {
        SourceEvidence<?> evidence = mapper.findSourceEvidencesByCustomerId(customerId, 0, 100).get(0);

        Many<Transaction> transactions = evidence.transactions().findAll();

        assertEquals(1, transactions.size());
        assertEquals(transactionId, transactions.stream().toList().get(0).getIdentity());
    }

    @Test
    public void should_find_account_transaction_by_account_and_transaction_id() {
        Transaction transaction = mapper.findTransactionByAccountAndId(accountId, transactionId);
        assertEquals(transactionId, transaction.getIdentity());
        assertEquals(Amount.cny("100.00"), transaction.getDescription().amount());
        assertEquals(createdAt, transaction.getDescription().createdAt());
    }

    @Test
    public void should_find_account_transaction_by_evidence_transaction_id() {
        Transaction transaction = mapper.findTransactionByEvidenceAndId(evidenceId, transactionId);
        assertEquals(transactionId, transaction.getIdentity());
        assertEquals(Amount.cny("100.00"), transaction.getDescription().amount());
        assertEquals(createdAt, transaction.getDescription().createdAt());
    }

    @Test
    public void should_add_transaction_to_database() {
        IdHolder holder = new IdHolder();
        LocalDateTime created = LocalDateTime.now();
        mapper.insertTransaction(holder, accountId, evidenceId, new TransactionDescription(Amount.cny("400.00"), created));

        Transaction transaction = mapper.findTransactionByAccountAndId(accountId, holder.id());
        assertEquals(Amount.cny("400.00"), transaction.getDescription().amount());
        assertEquals(created, transaction.getDescription().createdAt());
    }

    @Test
    public void should_insert_sales_settlement() {
        IdHolder holder = new IdHolder();
        SalesSettlementDescription description = new SalesSettlementDescription(new Ref<>(orderId),
                Amount.cny("1000.00"), new Ref<>(accountId), new SalesSettlementDescription.Detail(Amount.cny("1000.00")));

        mapper.insertSourceEvidence(holder, customerId, description);
        mapper.insertSourceEvidenceDescription(holder.id(), description);

        SalesSettlement evidence = (SalesSettlement) mapper.findSourceEvidenceByCustomerAndId(customerId, holder.id());
        assertEquals(Amount.cny("1000.00"), evidence.getDescription().getTotal());

        assertEquals(1, evidence.getDescription().getDetails().size());
        SalesSettlementDescription.Detail detail = evidence.getDescription().getDetails().get(0);
        assertEquals(Amount.cny("1000.00"), detail.getAmount());
    }

    @Test
    public void should_update_account_amount() {
        mapper.updateAccount(customerId, accountId, new Account.AccountChange(Amount.cny("100.00")));

        Account account = mapper.findCustomerById(customerId).accounts().findByIdentity(accountId).get();
        assertEquals(Amount.cny("200.00"), account.getDescription().current());
    }

    @Test
    public void should_count_transaction_in_accounts() {
        assertEquals(1, mapper.countTransactionsInAccount(accountId));
    }
}
