package reengineering.ddd.accounting.mybatis;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import reengineering.ddd.accounting.description.SourceEvidenceDescription;
import reengineering.ddd.accounting.description.TransactionDescription;
import reengineering.ddd.accounting.model.Account;
import reengineering.ddd.accounting.model.Customer;
import reengineering.ddd.accounting.model.SourceEvidence;
import reengineering.ddd.accounting.model.Transaction;
import reengineering.ddd.mybatis.support.IdHolder;

import java.util.List;

@Mapper
public interface ModelMapper {
    Customer findCustomerById(String id);

    List<SourceEvidence<?>> findSourceEvidencesByCustomerId(@Param("customer_id") String customerId,
                                                            @Param("from") int from,
                                                            @Param("size") int size);


    SourceEvidence<?> findSourceEvidenceByCustomerAndId(@Param("customer_id") String customerId, @Param("id") String id);

    List<Transaction> findTransactionsByAccountId(@Param("account_id") String accountId,
                                                  @Param("from") int from,
                                                  @Param("size") int size);

    List<Transaction> findTransactionsBySourceEvidenceId(String evidenceId);

    Transaction findTransactionByAccountAndId(@Param("account_id") String accountId, @Param("id") String transactionId);

    Transaction findTransactionByEvidenceAndId(@Param("evidence_id") String evidenceId, @Param("id") String transactionId);

    void insertTransaction(@Param("holder") IdHolder id,
                           @Param("account_id") String accountId,
                           @Param("evidence_id") String evidenceId,
                           @Param("description") TransactionDescription transactionDescription);

    void insertSourceEvidence(@Param("holder") IdHolder id, @Param("customer_id") String customerId, @Param("description") SourceEvidenceDescription description);

    void insertSourceEvidenceDescription(@Param("id") String id, @Param("description") SourceEvidenceDescription description);

    void updateAccount(@Param("customer_id") String customerId, @Param("id") String accountId, @Param("change") Account.AccountChange change);

    int countTransactionsInAccount(String accountId);

    int countSourceEvidencesByCustomer(String customerId);

    int countTransactionsBySourceEvidence(String sourceEvidenceId);
}
