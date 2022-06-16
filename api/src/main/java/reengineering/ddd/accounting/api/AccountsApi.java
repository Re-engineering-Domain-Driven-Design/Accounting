package reengineering.ddd.accounting.api;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import reengineering.ddd.accounting.api.representation.TransactionModel;
import reengineering.ddd.accounting.model.Account;
import reengineering.ddd.accounting.model.Customer;
import reengineering.ddd.accounting.model.Transaction;
import reengineering.ddd.archtype.Many;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static reengineering.ddd.accounting.api.ApiTemplates.accountTransactions;

public class AccountsApi {
    private Customer customer;

    public AccountsApi(Customer customer) {
        this.customer = customer;
    }

    @GET
    @Path("{account-id}/transactions")
    public CollectionModel<TransactionModel> findAll(@PathParam("account-id") String id, @Context UriInfo info, @DefaultValue("0") @QueryParam("page") int page) {
        Account account = customer.accounts().findByIdentity(id).orElseThrow(() -> new WebApplicationException(Response.Status.NOT_FOUND));
        return new Pagination<>(account.transactions().findAll(), 40).page(page,
                tx -> TransactionModel.withEvidenceLink(customer, tx, info),
                p -> accountTransactions(info).queryParam("page", p).build(customer.getIdentity(), account.getIdentity()));
    }
}
