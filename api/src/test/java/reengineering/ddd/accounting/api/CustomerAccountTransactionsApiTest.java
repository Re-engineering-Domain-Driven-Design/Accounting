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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CustomerAccountTransactionsApiTest extends ApiTest {
    @MockBean
    private Customers customers;
    private Customer customer;

    @Mock
    private Customer.Accounts accounts;

    @BeforeEach
    public void before() {
        customer = new Customer("john.smith", new CustomerDescription("John Smith", "john.smith@email.com"), mock(Customer.SourceEvidences.class), accounts);
        when(customers.findById(eq(customer.identity()))).thenReturn(Optional.of(customer));
    }

    @Test
    public void should_return_404_if_account_not_found() {
        when(accounts.findByIdentity(eq("CASH-01"))).thenReturn(Optional.empty());

        given().accept(MediaTypes.HAL_JSON.toString())
                .when().get("/customers/" + customer.identity() + "/accounts/CASH-01/transactions")
                .then().statusCode(404);
    }

    @Test
    public void should_return_transactions_in_account() {
        Account.Transactions transactions = mock(Account.Transactions.class);
        Account account = new Account("CASH-01", new AccountDescription(new Amount(new BigDecimal("0"), Currency.CNY)), transactions);

        when(accounts.findByIdentity(eq("CASH-01"))).thenReturn(Optional.of(account));

        SourceEvidence evidence = mock(SourceEvidence.class);
        when(evidence.identity()).thenReturn("EV-001");

        Transaction transaction = new Transaction("TX-01", new TransactionDescription(Amount.cny("1000"), LocalDateTime.now()), () -> account,
                () -> evidence);

        when(transactions.findAll()).thenReturn(new EntityList<>(transaction));

        given().accept(MediaTypes.HAL_JSON.toString())
                .when().get("/customers/" + customer.identity() + "/accounts/CASH-01/transactions")
                .then().statusCode(200)
                .body("_links.self.href", is("/api/customers/" + customer.identity() + "/accounts/CASH-01/transactions"))
                .body("_embedded.transactions.size()", is(1))
                .body("_embedded.transactions[0].id", is(transaction.identity()))
                .body("_embedded.transactions[0].amount.value", is(transaction.description().amount().value().intValue()))
                .body("_embedded.transactions[0].amount.currency", is(transaction.description().amount().currency().toString()))
                .body("_embedded.transactions[0]._links.source-evidence.href", is("/api/customers/" + customer.identity() + "/source-evidences/EV-001"));
    }
}
