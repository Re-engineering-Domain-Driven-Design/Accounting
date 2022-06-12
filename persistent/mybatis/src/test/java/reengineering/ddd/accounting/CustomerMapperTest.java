package reengineering.ddd.accounting;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.context.annotation.Import;
import reengineering.ddd.accounting.model.Customer;
import reengineering.ddd.accounting.mybatis.mapper.CustomerMapper;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MybatisTest
public class CustomerMapperTest {
    @Inject
    private CustomerMapper mapper;
    @Inject
    private TestDataMapper testData;

    @Test
    public void should_find_customer_by_id() {
        testData.insertCustomer("1", "John Smith", "john.smith@email.com");

        Customer customer = mapper.findById("1");
        assertEquals("1", customer.identity());
        assertEquals("John Smith", customer.description().name());
        assertEquals("john.smith@email.com", customer.description().email());
    }
}
