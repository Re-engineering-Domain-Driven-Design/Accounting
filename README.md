[中文版](README.zh-cn.md)

# Business Background

This is a simple accounting system, which keeps transactions to different accounts based on business documents.
For example, sales settlement is such a business document. Based on what's in the details, it may require to keep
transactions in cash account, credit account and in transit account.

The model shown as below:

![Model](public/model.jpg?raw=true "Model")

# Smart Domain Architecture Pattern

A brief introduction to Smart Domain Architecture can be found [here](public/Smart%20Domain%20Pattern.pdf?raw=true)

To apply the Smart Domain architecture pattern as the implementation pattern of domain
driven design, first, you should explicitly model the associations
between entities. Remember, you have to provide a root association. Customers in this example:

![Association](public/association.jpg?raw=true "Associations")

All association objects are just plain interfaces. For my personal taste, I used inner interfaces. You can find detailed 
codes in module domain: 

```java
public class Customer implements Entity<String, CustomerDescription> {
    private SourceEvidences sourceEvidences;

    private Accounts accounts;

    public HasMany<String, SourceEvidence<?>> sourceEvidences() {
        return sourceEvidences;
    }

    public HasMany<String, Account> accounts() {
        return accounts;
    }

    public interface SourceEvidences extends HasMany<String, SourceEvidence<?>> {
        SourceEvidence<?> add(SourceEvidenceDescription description);
    }

    public interface Accounts extends HasMany<String, Account> {
        void update(Account account, Account.AccountChange change);
    }
}

public class Account implements Entity<String, AccountDescription> {
    private Transactions transactions;

    public HasMany<String, Transaction> transactions() {
        return transactions;
    }

    public interface Transactions extends HasMany<String, Transaction> {
        Transaction add(Account account, SourceEvidence<?> evidence, TransactionDescription description);
    }
}

public interface SourceEvidence<Description extends SourceEvidenceDescription> extends Entity<String, Description> {
    HasMany<String, Transaction> transactions();

    interface Transactions extends HasMany<String, Transaction> {
    }
}
```

Notice, I used a wider interface inside the entity, but a narrower interface for outside reader model. Thus, would allow 
me to encapsulate association manipulation logic within entity.  

## Expose API over model

After build the object model, now it is time to expose the model via REST API. It is really easy to design the API on top 
of the model:  

![API](public/api.jpg?raw=true "API")

Implement the root association(Customers) as JAX-RS root resource: 

```java
@Path("/customers")
public class CustomersApi {
    private Customers customers;

    @Inject
    public CustomersApi(Customers customers) {
        this.customers = customers;
    }

    @Path("{id}")
    public CustomerApi findById(@PathParam("id") String id) {
        return customers.findById(id).map(CustomerApi::new).orElse(null);
    }
}
```

Entity can be implemented as sub-resource:

```java
public class CustomerApi {
    private Customer customer;

    public CustomerApi(Customer customer) {
        this.customer = customer;
    }

    @GET
    public CustomerModel get(@Context UriInfo info) {
        return new CustomerModel(customer, info);
    }

    @Path("source-evidences")
    public SourceEvidencesApi sourceEvidences(@Context ResourceContext context) {
        return context.initResource(new SourceEvidencesApi(customer));
    }

    @Path("accounts")
    public AccountsApi accounts() {
        return new AccountsApi(customer);
    }
}
```

As well as association objects. As shown in the code below, it is a sub-resource for Customer.SourceEvidences interface:

```java
public class SourceEvidencesApi {
    private Customer customer;

    @Inject
    private SourceEvidenceReader reader;

    public SourceEvidencesApi(Customer customer) {
        this.customer = customer;
    }

    @GET
    @Path("{evidence-id}")
    public SourceEvidenceModel findById(@PathParam("evidence-id") String id,
                                        @Context UriInfo info) {
        return customer.sourceEvidences().findByIdentity(id).map(evidence -> SourceEvidenceModel.of(customer, evidence, info))
                .orElseThrow(() -> new WebApplicationException(Response.Status.NOT_FOUND));
    }

    @GET
    public CollectionModel<SourceEvidenceModel> findAll(@Context UriInfo info, @DefaultValue("0") @QueryParam("page") int page) {
        return new Pagination<>(customer.sourceEvidences().findAll(), 40).page(page,
                evidence -> SourceEvidenceModel.simple(customer, evidence, info),
                p -> sourceEvidences(info).queryParam("page", p).build(customer.getIdentity()));
    }

    @POST
    public Response create(String json, @Context UriInfo info) {
        SourceEvidence evidence = customer.add(reader.read(json)
                .orElseThrow(() -> new WebApplicationException(Response.Status.NOT_ACCEPTABLE)).description());
        return Response.created(ApiTemplates.sourceEvidence(info).build(customer.getIdentity(), evidence.getIdentity())).build();
    }
}
```
You can check out the api module for more information. Particularly, in api test, I didn't use any database. All the logic 
were tested over the abstraction of association objects.

### Implementing Association Objects

Last but not least, implementing association objects with proper lifecycle semantic. Take the association between source evidence
and transaction for example, it could be an in memory association, which means that every time source evidence read into 
memory, the associated transactions will be read as well. Or you can call it **aggregated** lifecycle. 

Meanwhile, the association between account and transactions may better be from database, since account may record tons of transactions. 
Or you can call it **reference** lifecycle:

![生命周期](public/lifecycle.jpg?raw=true "生命周期")

This may be counterintuitive. Many DDD practitioners may say account-transaction should be an aggregation, and source evidence-transaction
may be a reference. We represent the conceptual aggregation relationship by URI: the primary URI(self link) of account is
/customers/{cid}/accounts/{aid}/transactions/{tid}, not /customers/{cid}/source-evidences/{sid}/transactions/{tid}. 

**Confusing aggregation relationship with lifecycle is a persistent problem with domain-driven design**.

Aggregated lifecycle implemented by package reengineering.ddd.mybatis.memory:

```java
import reengineering.ddd.mybatis.memory.EntityList;

public class SourceEvidenceTransactions extends EntityList<String, Transaction> implements SourceEvidence.Transactions {
}
```

Reference lifecycle implemented by package reengineering.ddd.mybatis.database:

```java
import reengineering.ddd.mybatis.database.EntityList;

public class AccountTransactions extends EntityList<String, Transaction> implements Account.Transactions {
    
}
```

In this example, association object were implemented by MyBatis. You can find the code in module persistent/mybatis.
