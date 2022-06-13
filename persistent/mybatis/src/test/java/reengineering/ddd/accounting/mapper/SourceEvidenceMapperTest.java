package reengineering.ddd.accounting.mapper;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import reengineering.ddd.accounting.TestDataMapper;
import reengineering.ddd.accounting.description.SalesSettlementDescription;
import reengineering.ddd.accounting.description.basic.Amount;
import reengineering.ddd.accounting.model.SalesSettlement;
import reengineering.ddd.accounting.model.SourceEvidence;
import reengineering.ddd.accounting.mybatis.mapper.SourceEvidenceMapper;

import javax.inject.Inject;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MybatisTest
public class SourceEvidenceMapperTest {
    @Inject
    private SourceEvidenceMapper mapper;
    @Inject
    private TestDataMapper testData;

    String id = "1";
    String detailId = "1";
    String customer = "1";
    String order = "ORD-001";
    String account = "CASH-001";

    @Test
    public void should_read_sales_settlement_as_source_evidence() {

        testData.insertSourceEvidence(id, customer, "sales-settlement");
        testData.insertSalesSettlement(id, order, account, 100.00, "CNY");
        testData.insertSalesSettlementDetail(detailId, id, 100.00, "CNY");

        List<SourceEvidence<?>> evidences = mapper.findByCustomerId(customer);
        assertEquals(1, evidences.size());

        SalesSettlement salesSettlement = (SalesSettlement) evidences.get(0);
        assertEquals(id, salesSettlement.identity());
        assertEquals(order, salesSettlement.description().getOrder().id());
        assertEquals(account, salesSettlement.description().getAccount().id());
        assertEquals(Amount.cny("100.00"), salesSettlement.description().getTotal());

        assertEquals(1, salesSettlement.description().getDetails().size());

        SalesSettlementDescription.Detail detail = salesSettlement.description().getDetails().get(0);
        assertEquals(Amount.cny("100.00"), detail.getAmount());
    }

}
