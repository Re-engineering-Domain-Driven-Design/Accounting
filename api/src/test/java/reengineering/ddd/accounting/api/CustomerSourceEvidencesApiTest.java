package reengineering.ddd.accounting.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import reengineering.ddd.accounting.api.representation.SourceEvidenceReader;
import reengineering.ddd.accounting.api.representation.SourceEvidenceRequest;
import reengineering.ddd.accounting.description.AccountDescription;
import reengineering.ddd.accounting.description.CustomerDescription;
import reengineering.ddd.accounting.description.SourceEvidenceDescription;
import reengineering.ddd.accounting.description.TransactionDescription;
import reengineering.ddd.accounting.description.basic.Amount;
import reengineering.ddd.accounting.description.basic.Currency;
import reengineering.ddd.accounting.model.*;

import javax.ws.rs.core.HttpHeaders;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CustomerSourceEvidencesApiTest extends ApiTest {
    @MockBean
    private Customers customers;
    private Customer customer;
    @Mock
    private Customer.SourceEvidences sourceEvidences;

    @MockBean
    private SourceEvidenceReader reader;

    @BeforeEach
    public void before() {
        customer = new Customer("john.smith", new CustomerDescription("John Smith", "john.smith@email.com"), sourceEvidences, mock(Customer.Accounts.class));
        when(customers.findById(eq(customer.identity()))).thenReturn(Optional.of(customer));
    }

    @Test
    public void should_return_all_source_evidences_with_only_links() {
        SourceEvidence evidence = mock(SourceEvidence.class);
        when(evidence.identity()).thenReturn("EV-001");
        when(evidence.description()).thenThrow(new NullPointerException());

        when(sourceEvidences.findAll()).thenReturn(new EntityList<>(evidence));

        given().accept(MediaTypes.HAL_JSON.toString())
                .when().get("/customers/" + customer.identity() + "/source-evidences")
                .then().statusCode(200)
                .body("_links.self.href", is("/api/customers/" + customer.identity() + "/source-evidences"))
                .body("_embedded.evidences.size()", is(1))
                .body("_embedded.evidences[0].id", is("EV-001"))
                .body("_embedded.evidences[0]._links.self.href", is("/api/customers/" + customer.identity() + "/source-evidences/EV-001"));
    }

    @Test
    public void should_return_404_if_no_source_evidence_matched_by_id() {
        when(sourceEvidences.findByIdentity("EV-001")).thenReturn(Optional.empty());

        given().accept(MediaTypes.HAL_JSON.toString())
                .when().get("/customers/" + customer.identity() + "/source-evidences/EV-001")
                .then().statusCode(404);
    }

    @Test
    public void should_return_source_evidence_matched_by_id() {
        SourceEvidence evidence = mock(SourceEvidence.class);
        SourceEvidence.Transactions transactions = mock(SourceEvidence.Transactions.class);

        when(evidence.identity()).thenReturn("EV-001");
        when(evidence.description()).thenReturn(new EvidenceDescription("ORD-001"));
        when(evidence.transactions()).thenReturn(transactions);

        Account.Transactions accountTransactions = mock(Account.Transactions.class);
        Account account = new Account("CASH-01", new AccountDescription(new Amount(new BigDecimal("0"), Currency.CNY)), accountTransactions);
        Transaction transaction = new Transaction("TX-01", new TransactionDescription(Amount.cny("1000"), LocalDateTime.now()), () -> account,
                () -> evidence);

        when(transactions.findAll()).thenReturn(new EntityList<>(transaction));

        when(sourceEvidences.findByIdentity("EV-001")).thenReturn(Optional.of(evidence));

        given().accept(MediaTypes.HAL_JSON.toString())
                .when().get("/customers/" + customer.identity() + "/source-evidences/EV-001")
                .then().statusCode(200)
                .body("id", is("EV-001"))
                .body("orderId", is("ORD-001"))
                .body("transactions.size()", is(1))
                .body("transactions[0].id", is("TX-01"))
                .body("transactions[0].amount.value", is(1000))
                .body("transactions[0].amount.currency", is("CNY"))
                .body("_links.self.href", is("/api/customers/" + customer.identity() + "/source-evidences/EV-001"));
    }

    @Test
    public void should_return_406_if_source_evidence_not_allowed() {
        String unsupported = "{\"type\": \"unsupported\"}";
        when(reader.read(eq(unsupported))).thenReturn(Optional.empty());
        given().accept(MediaTypes.HAL_JSON.toString())
                .body(unsupported)
                .when().post("/customers/" + customer.identity() + "/source-evidences")
                .then().statusCode(406);
    }

    @Test
    public void should_return_201_if_source_evidence_created() {
        String supported = "{\"type\": \"supported\"}";
        EvidenceDescription description = new EvidenceDescription("ORD-001");

        SourceEvidenceRequest request = mock(SourceEvidenceRequest.class);
        SourceEvidence evidence = mock(SourceEvidence.class);

        when(request.description()).thenReturn(description);
        when(evidence.identity()).thenReturn("EV-001");
        when(sourceEvidences.add(same(description))).thenReturn(evidence);

        when(reader.read(eq(supported))).thenReturn(Optional.of(request));
        given().accept(MediaTypes.HAL_JSON.toString())
                .body(supported)
                .when().post("/customers/" + customer.identity() + "/source-evidences")
                .then().statusCode(201)
                .header(HttpHeaders.LOCATION, is(uri("/api/customers/" + customer.identity() + "/source-evidences/EV-001")));
    }

    record EvidenceDescription(String orderId) implements SourceEvidenceDescription {
    }
}


