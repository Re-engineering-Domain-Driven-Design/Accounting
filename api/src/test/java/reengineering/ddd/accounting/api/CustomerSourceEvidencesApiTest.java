package reengineering.ddd.accounting.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import reengineering.ddd.accounting.description.CustomerDescription;
import reengineering.ddd.accounting.description.SourceEvidenceDescription;
import reengineering.ddd.accounting.model.Customer;
import reengineering.ddd.accounting.model.Customers;
import reengineering.ddd.accounting.model.SourceEvidence;
import reengineering.ddd.archtype.Entity;
import reengineering.ddd.archtype.EntityCollection;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CustomerSourceEvidencesApiTest extends ApiTest {
    @MockBean
    private Customers customers;
    private Customer customer;
    @Mock
    private Customer.SourceEvidences sourceEvidences;

    @BeforeEach
    public void before() {
        customer = new Customer("john.smith", new CustomerDescription("John Smith", "john.smith@email.com"), sourceEvidences);
        when(customers.findById(eq(customer.identity()))).thenReturn(Optional.of(customer));
    }

    @Test
    public void should_return_all_source_evidences() {
        SourceEvidence evidence = mock(SourceEvidence.class);
        when(evidence.identity()).thenReturn("EV-001");
        when(evidence.description()).thenReturn(new EvidenceDescription("ORD-001"));

        when(sourceEvidences.findAll()).thenReturn(new EntityList<>(evidence));

        given().accept(MediaTypes.HAL_JSON.toString())
                .when().get("/customers/" + customer.identity() + "/source-evidences")
                .then().statusCode(200)
                .body("_embedded.evidences.size()", is(1))
                .body("_embedded.evidences[0].id", is("EV-001"))
                .body("_embedded.evidences[0].orderId", is("ORD-001"));
    }
}

record EvidenceDescription(String orderId) implements SourceEvidenceDescription {
}

class EntityList<E extends Entity<?, ?>> implements EntityCollection<E> {
    private List<E> list;

    public EntityList(List<E> list) {
        this.list = list;
    }

    public EntityList(E... entities) {
        this(List.of(entities));
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public EntityCollection<E> subCollection(int from, int to) {
        return new EntityList<>(list.subList(from, to));
    }

    @Override
    public Iterator<E> iterator() {
        return list.iterator();
    }
}
