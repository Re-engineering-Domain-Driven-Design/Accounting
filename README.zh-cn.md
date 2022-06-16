# 业务背景

这是一个简单的记账系统。系统会根据业务单据，按照不同的账目记录流水。比如，对于销售结算单，可能需要根据明细，
分别向现金账户、信用账户、在途账户等账户中，记录流水。

业务模型如下：
![模型](public/model.jpg?raw=true "模型")

# Smart Domain架构模式

关于Smart Domain架构模式[这里](public/Smart%20Domain%20Pattern.pdf?raw=true)有个简单的介绍。 

## 通过关联对象构建模型

应用Smart Domain架构模式，作为领域驱动设计的实现模式。首先将模型图中的关联关系建模为关联对象。
注意，需要额外引入一个根关联对象。在这个模型中是Customers：

![关联](public/association.jpg?raw=true "关联")

关联对象用接口表示。处于我个人的口味，我使用了内部接口。具体的代码在domain模块中：

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

注意，我是用了宽接口（可修改）而对外仅仅暴露窄接口（只读）。这样可以让我将修改逻辑封装到实体对象之内。

## 将模型映射为API

在构建了模型之后，可以将模型映射为API，通过关联关系，表示URI：

![API](public/api.jpg?raw=true "API")

然后使用JAX-RS将根关联对象转成root resource：

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

实体转成sub-resources：
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

关联对象可以转成sub-resource，如下代码所示，实际是对于Customer.SourceEvidences的API化：
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
API的具体代码都在api模块中。可以看到，在测试中，并没有使用数据库，而是依赖关联对象的抽象，完成api的测试。

### 实现关联对象

最后，根据需要提供关联对象的实现，并提供恰当的生命周期语义：Source Evidence和Transaction之间是内存直接关联。也就是说，每次读取Source Evidence时，
同时也会将关联的Transaction对象读入，也就是遵守聚合生命周期。而Account可能因为存在大量的Transaction，并不需要一起读入。也就是说，遵守引用
生命周期：

![生命周期](public/lifecycle.jpg?raw=true "生命周期")

Transaction的不同生命周期，可能极度违反直觉。因为从业务概念上讲，Account应该聚合Transaction才对。这种业务上的聚合关系，我们通过URI来表示。
也就是对于Transaction而言，它的URI是/customers/{cid}/accounts/{aid}/transactions/{tid}。 而不是/customers/{cid}/source-evidences/{sid}/transactions/{tid}。 
**将业务聚合与生命周期混为一谈，是领域驱动设计的顽疾。**

聚合的生命周期由reengineering.ddd.mybatis.memory包提供：

```java
import reengineering.ddd.mybatis.memory.EntityList;

public class SourceEvidenceTransactions extends EntityList<String, Transaction> implements SourceEvidence.Transactions {
}
```

引用的生命周期由reengineering.ddd.mybatis.database提供：

```java
import reengineering.ddd.mybatis.database.EntityList;

public class AccountTransactions extends EntityList<String, Transaction> implements Account.Transactions {
    
}
```

所有的关联对象都使用MyBatis实现，代码在persistent/mybatis模块中。






