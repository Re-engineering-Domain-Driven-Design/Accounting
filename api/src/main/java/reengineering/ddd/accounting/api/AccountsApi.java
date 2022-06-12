package reengineering.ddd.accounting.api;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import reengineering.ddd.accounting.api.representation.TransactionModel;
import reengineering.ddd.accounting.model.Account;
import reengineering.ddd.accounting.model.Customer;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.stream.Collectors;

public class AccountsApi {
    private Customer customer;

    public AccountsApi(Customer customer) {
        this.customer = customer;
    }

    @GET
    @Path("{account-id}/transactions")
    public CollectionModel<TransactionModel> findAll(@PathParam("account-id") String id, @Context UriInfo info) {
        Account account = customer.accounts().findByIdentity(id).orElseThrow(() -> new WebApplicationException(Response.Status.NOT_FOUND));
        return CollectionModel.of(account.transactions().findAll().stream().map(tx -> new TransactionModel(customer, tx, info))
                        .collect(Collectors.toList()),
                Link.of(ApiTemplates.accountTransactions(info).build(customer.identity(), account.identity()).getPath(), "self"));
    }
}
