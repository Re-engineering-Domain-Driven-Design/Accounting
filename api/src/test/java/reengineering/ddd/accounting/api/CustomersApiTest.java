package reengineering.ddd.accounting.api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import reengineering.ddd.accounting.domain.Customers;

import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.mockito.ArgumentMatchers.eq;
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
}
