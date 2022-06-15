package reengineering.ddd.accounting.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reengineering.ddd.accounting.description.AccountDescription;
import reengineering.ddd.accounting.description.CustomerDescription;
import reengineering.ddd.accounting.description.SourceEvidenceDescription;
import reengineering.ddd.accounting.description.TransactionDescription;
import reengineering.ddd.accounting.description.basic.Amount;
import reengineering.ddd.archtype.Many;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

public class CustomerTest {

    private Customer.SourceEvidences evidences;

    private Customer.Accounts accounts;

    private SourceEvidenceDescription description;

    private SourceEvidence evidence;

    @BeforeEach
    public void before() {
        evidences = mock(Customer.SourceEvidences.class);
        accounts = mock(Customer.Accounts.class);
        description = mock(SourceEvidenceDescription.class);
        evidence = mock(SourceEvidence.class);
    }

    @Test
    public void should_write_transactions_to_account_from_source_evidence() {
        TransactionDescription cash1Tx1 = new TransactionDescription(Amount.cny("1000.00"), LocalDateTime.now());
        TransactionDescription cash1Tx2 = new TransactionDescription(Amount.cny("2000.00"), LocalDateTime.now());

        TransactionDescription cash2Tx1 = new TransactionDescription(Amount.cny("3000.00"), LocalDateTime.now());
        TransactionDescription cash2Tx2 = new TransactionDescription(Amount.cny("4000.00"), LocalDateTime.now());

        TransactionList cash1Tx = new TransactionList();
        TransactionList cash2Tx = new TransactionList();
        Account cash01 = new Account("CASH-01", new AccountDescription(Amount.cny("0.00")), cash1Tx);
        Account cash02 = new Account("CASH-02", new AccountDescription(Amount.cny("0.00")), cash2Tx);

        when(evidences.add(same(description))).thenReturn(evidence);
        when(evidence.toTransactions()).thenReturn(Map.of("CASH-01", List.of(cash1Tx1, cash1Tx2), "CASH-02", List.of(cash2Tx1, cash2Tx2)));
        when(accounts.findByIdentity(eq("CASH-01"))).thenReturn(Optional.of(cash01));
        when(accounts.findByIdentity(eq("CASH-02"))).thenReturn(Optional.of(cash02));

        Customer customer = new Customer("id", new CustomerDescription("John Smith", "john.smith@email.com"), evidences, accounts);
        assertSame(evidence, customer.add(description));
        assertEquals(2, cash1Tx.descriptions.size());
        assertEquals(2, cash2Tx.descriptions.size());

        assertEquals(Amount.cny("3000.00"), cash01.description().current());
        assertEquals(Amount.cny("7000.00"), cash02.description().current());

        verify(accounts, times(1)).update(eq(cash01), eq(new Account.AccountChange(Amount.cny("3000.00"))));
        verify(accounts, times(1)).update(eq(cash02), eq(new Account.AccountChange(Amount.cny("7000.00"))));
    }

    @Test
    public void should_throw_exception_if_account_not_found() {
        TransactionDescription cash1Tx1 = new TransactionDescription(Amount.cny("1000.00"), LocalDateTime.now());

        when(evidences.add(same(description))).thenReturn(evidence);
        when(evidence.toTransactions()).thenReturn(Map.of("CASH-01", List.of(cash1Tx1)));
        when(accounts.findByIdentity(eq("CASH-01"))).thenReturn(Optional.empty());

        Customer customer = new Customer("id", new CustomerDescription("John Smith", "john.smith@email.com"), evidences, accounts);
        assertThrows(AccountNotFoundException.class, () -> customer.add(description));
    }

    static class TransactionList implements Account.Transactions {
        List<TransactionDescription> descriptions = new ArrayList<>();

        @Override
        public Many<Transaction> findAll() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Optional<Transaction> findByIdentity(String identifier) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Transaction add(Account account, SourceEvidence<?> evidence, TransactionDescription description) {
            descriptions.add(description);
            return new Transaction(String.valueOf(new Random().nextInt(100000)), description, null, null);
        }
    }
}
