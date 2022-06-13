package reengineering.ddd.accounting.mapper;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import reengineering.ddd.accounting.TestDataMapper;
import reengineering.ddd.accounting.description.basic.Amount;
import reengineering.ddd.accounting.model.Account;
import reengineering.ddd.accounting.model.Customer;
import reengineering.ddd.accounting.model.SourceEvidence;
import reengineering.ddd.accounting.mybatis.mapper.CustomerMapper;
import reengineering.ddd.archtype.EntityCollection;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MybatisTest
public class CustomerMapperTest {
    @Inject
    private CustomerMapper mapper;
    @Inject
    private TestDataMapper testData;

    String id = "1";
    String detailId = "1";
    String customer = "1";
    String order = "ORD-001";
    String account = "1";


    @Test
    public void should_find_customer_by_id() {
        testData.insertCustomer("1", "John Smith", "john.smith@email.com");

        Customer customer = mapper.findById("1");
        assertEquals("1", customer.identity());
        assertEquals("John Smith", customer.description().name());
        assertEquals("john.smith@email.com", customer.description().email());
    }

    @Test
    public void should_assign_source_evidences_association() {
        testData.insertCustomer(id, "John Smith", "john.smith@email.com");

        testData.insertSourceEvidence(id, customer, "sales-settlement");
        testData.insertSalesSettlement(id, order, account, 100.00, "CNY");
        testData.insertSalesSettlementDetail(detailId, id, 100.00, "CNY");

        Customer customer = mapper.findById("1");

        EntityCollection<SourceEvidence<?>> evidences = customer.sourceEvidences().findAll();

        assertEquals(1, evidences.size());
    }

    @Test
    public void should_assign_accounts_association() {
        testData.insertCustomer(id, "John Smith", "john.smith@email.com");

        testData.insertAccounts(account, id, 100.00, "CNY");

        Customer customer = mapper.findById("1");
        assertEquals(1, customer.accounts().findAll().size());

        Account customerAccount = customer.accounts().findByIdentity(account).get();
        assertEquals(Amount.cny("100.00"), customerAccount.description().current());
    }
}
