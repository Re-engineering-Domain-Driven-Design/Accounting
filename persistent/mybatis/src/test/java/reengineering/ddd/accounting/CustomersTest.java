package reengineering.ddd.accounting;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Import;
import reengineering.ddd.accounting.model.Customer;
import reengineering.ddd.accounting.mybatis.model.Customers;

import javax.inject.Inject;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MybatisTest
@Import(FlywayConfig.class)
public class CustomersTest {

    @Inject
    private Customers customers;

    @Inject
    private TestDataMapper testData;

    @Inject
    private FlywayMigrationStrategy strategy;


    @Test
    public void should_find_customer_by_id() {
        testData.insertCustomer("1", "John Smith", "john.smith@email.com");

        Customer customer = customers.findById("1").get();
        assertEquals("1", customer.identity());
        assertEquals("John Smith", customer.description().name());
        assertEquals("john.smith@email.com", customer.description().email());
    }
}
