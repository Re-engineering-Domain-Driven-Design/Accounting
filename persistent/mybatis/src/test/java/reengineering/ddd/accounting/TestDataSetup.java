package reengineering.ddd.accounting;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reengineering.ddd.accounting.description.SalesSettlementDescription;
import reengineering.ddd.accounting.description.basic.Amount;
import reengineering.ddd.accounting.description.basic.Ref;
import reengineering.ddd.accounting.model.Customer;
import reengineering.ddd.accounting.model.Customers;

import java.util.Random;

public class TestDataSetup implements BeforeAllCallback {

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        ApplicationContext springContext = SpringExtension.getApplicationContext(context);
        TestDataMapper testData = springContext.getBean(TestDataMapper.class);
        Customers customers = springContext.getBean(Customers.class);

        String customerId = "1";
        String accountId = "1";

        testData.insertCustomer(customerId, "John Smith", "john.smith@email.com");
        testData.insertAccount(accountId, customerId, 100.00, "CNY");

        Customer customer = customers.findById(customerId).get();

        for (var evidence = 0; evidence < 1000; evidence++) {
            var description = new SalesSettlementDescription(new Ref<>("ORD-" + new Random().nextInt()),
                    Amount.cny("500.00"), new Ref<>(accountId),
                    new SalesSettlementDescription.Detail(Amount.cny("100.00")),
                    new SalesSettlementDescription.Detail(Amount.cny("100.00")),
                    new SalesSettlementDescription.Detail(Amount.cny("100.00")),
                    new SalesSettlementDescription.Detail(Amount.cny("100.00")),
                    new SalesSettlementDescription.Detail(Amount.cny("100.00"))
            );

            customer.add(description);
        }
    }
}
