package reengineering.ddd.accounting.api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import reengineering.ddd.accounting.description.AccountDescription;
import reengineering.ddd.accounting.description.CustomerDescription;
import reengineering.ddd.accounting.description.basic.Amount;
import reengineering.ddd.accounting.model.Account;
import reengineering.ddd.accounting.model.Customer;
import reengineering.ddd.accounting.model.Customers;

import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CustomersApiTest extends ApiTest {
    @MockBean
    private Customers customers;

    @Test
    public void should_return_404_if_customer_not_exist() {
        when(customers.findById(eq("inexist"))).thenReturn(Optional.empty());

        given().accept(MediaTypes.HAL_JSON.toString())
                .when().get("/customers/inexist").then().statusCode(404);
    }

    @Test
    public void should_return_customer_if_customer_exists() {
        Customer.Accounts accounts = mock(Customer.Accounts.class);
        Account.Transactions transactions = mock(Account.Transactions.class);

        Customer customer = new Customer("john.smith",
                new CustomerDescription("John Smith", "john.smith@email.com"),
                mock(Customer.SourceEvidences.class), accounts);

        when(customers.findById(eq(customer.getIdentity()))).thenReturn(Optional.of(customer));
        when(accounts.findAll()).thenReturn(new EntityList<>(
                new Account("1", new AccountDescription(Amount.cny("100.00")), transactions),
                new Account("2", new AccountDescription(Amount.cny("100.00")), transactions)
        ));

        given().accept(MediaTypes.HAL_JSON.toString())
                .when().get("/customers/" + customer.getIdentity())
                .then().statusCode(200)
                .body("id", is(customer.getIdentity()))
                .body("name", is(customer.getDescription().name()))
                .body("email", is(customer.getDescription().email()))
                .body("_links.self.href", is("/api/customers/" + customer.getIdentity()))
                .body("_links.account-1-transactions.href", is("/api/customers/" + customer.getIdentity() + "/accounts/1/transactions"))
                .body("_links.account-2-transactions.href", is("/api/customers/" + customer.getIdentity() + "/accounts/2/transactions"))
                .body("_links.source-evidences.href", is("/api/customers/" + customer.getIdentity() + "/source-evidences"));
    }
}
