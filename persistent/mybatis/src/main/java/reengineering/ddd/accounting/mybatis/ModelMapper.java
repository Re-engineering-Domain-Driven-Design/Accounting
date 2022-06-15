package reengineering.ddd.accounting.mybatis;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import reengineering.ddd.accounting.description.TransactionDescription;
import reengineering.ddd.accounting.model.Customer;
import reengineering.ddd.accounting.model.SourceEvidence;
import reengineering.ddd.accounting.model.Transaction;
import reengineering.ddd.mybatis.support.IdHolder;

import java.util.List;

@Mapper
public interface ModelMapper {
    Customer findCustomerById(String id);

    List<SourceEvidence<?>> findSourceEvidencesByCustomerId(String customerId);

    List<Transaction> findTransactionsByAccountId(String accountId);

    List<Transaction> findTransactionsBySourceEvidenceId(String evidenceId);

    Transaction findTransactionById(String transactionId);

    void insertTransaction(@Param("holder") IdHolder id,
                           @Param("account_id") String accountId,
                           @Param("evidence_id") String evidenceId,
                           @Param("description") TransactionDescription transactionDescription);
}
