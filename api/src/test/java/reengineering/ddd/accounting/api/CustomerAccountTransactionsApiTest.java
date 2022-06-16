package reengineering.ddd.accounting.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import reengineering.ddd.accounting.description.AccountDescription;
import reengineering.ddd.accounting.description.CustomerDescription;
import reengineering.ddd.accounting.description.TransactionDescription;
import reengineering.ddd.accounting.description.basic.Amount;
import reengineering.ddd.accounting.description.basic.Currency;
import reengineering.ddd.accounting.model.*;
import reengineering.ddd.archtype.Many;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CustomerAccountTransactionsApiTest extends ApiTest {
    @MockBean
    private Customers customers;
    private Customer customer;
    @Mock
    private Customer.Accounts accounts;

    private Account account;

    @Mock
    private Account.Transactions transactions;

    @Mock
    private SourceEvidence evidence;

    private Transaction transaction;

    @Mock
    private Many<Transaction> allTransactions;

    @BeforeEach
    public void before() {
        customer = new Customer("john.smith", new CustomerDescription("John Smith", "john.smith@email.com"), mock(Customer.SourceEvidences.class), accounts);
        when(customers.findById(eq(customer.getIdentity()))).thenReturn(Optional.of(customer));

        account = new Account("CASH-01", new AccountDescription(new Amount(new BigDecimal("0"), Currency.CNY)), transactions);
        when(evidence.getIdentity()).thenReturn("EV-001");
        when(transactions.findAll()).thenReturn(allTransactions);

        transaction = new Transaction("TX-01", new TransactionDescription(Amount.cny("1000"), LocalDateTime.now()), () -> account, () -> evidence);
    }

    @Test
    public void should_return_404_if_account_not_found() {
        when(accounts.findByIdentity(eq("CASH-01"))).thenReturn(Optional.empty());

        given().accept(MediaTypes.HAL_JSON.toString())
                .when().get("/customers/" + customer.getIdentity() + "/accounts/CASH-01/transactions")
                .then().statusCode(404);
    }

    @Test
    public void should_return_transactions_in_account() {
        when(accounts.findByIdentity(eq("CASH-01"))).thenReturn(Optional.of(account));
        when(allTransactions.size()).thenReturn(1);
        when(allTransactions.subCollection(eq(0), eq(1))).thenReturn(new EntityList<>(transaction));

        given().accept(MediaTypes.HAL_JSON.toString())
                .when().get("/customers/" + customer.getIdentity() + "/accounts/CASH-01/transactions")
                .then().statusCode(200)
                .body("_links.self.href", is("/api/customers/" + customer.getIdentity() + "/accounts/CASH-01/transactions?page=0"))
                .body("page.size", is(40))
                .body("page.totalElements", is(1))
                .body("page.totalPages", is(1))
                .body("page.number", is(0))
                .body("_embedded.transactions.size()", is(1))
                .body("_embedded.transactions[0].id", is(transaction.getIdentity()))
                .body("_embedded.transactions[0].amount.value", is(transaction.getDescription().amount().value().intValue()))
                .body("_embedded.transactions[0].amount.currency", is(transaction.getDescription().amount().currency().toString()))
                .body("_embedded.transactions[0]._links.source-evidence.href", is("/api/customers/" + customer.getIdentity() + "/source-evidences/EV-001"));
    }

    @Test
    public void should_return_page_links_if_more_than_one_page_exists() {
        when(accounts.findByIdentity(eq("CASH-01"))).thenReturn(Optional.of(account));
        when(allTransactions.size()).thenReturn(4000);
        when(allTransactions.subCollection(eq(0), eq(40))).thenReturn(new EntityList<>(transaction));

        given().accept(MediaTypes.HAL_JSON.toString())
                .when().get("/customers/" + customer.getIdentity() + "/accounts/CASH-01/transactions")
                .then().statusCode(200)
                .body("_links.next.href", is("/api/customers/" + customer.getIdentity() + "/accounts/CASH-01/transactions?page=1"))
                .body("_links.self.href", is("/api/customers/" + customer.getIdentity() + "/accounts/CASH-01/transactions?page=0"));
    }

    @Test
    public void should_return_404_if_not_in_page_range() {
        when(accounts.findByIdentity(eq("CASH-01"))).thenReturn(Optional.of(account));
        when(allTransactions.size()).thenReturn(0);

        given().accept(MediaTypes.HAL_JSON.toString())
                .when().get("/customers/" + customer.getIdentity() + "/accounts/CASH-01/transactions?page=1")
                .then().statusCode(404);

        given().accept(MediaTypes.HAL_JSON.toString())
                .when().get("/customers/" + customer.getIdentity() + "/accounts/CASH-01/transactions?page=-1")
                .then().statusCode(404);
    }
}
