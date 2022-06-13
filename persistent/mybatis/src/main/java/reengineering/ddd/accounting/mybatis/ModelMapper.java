package reengineering.ddd.accounting.mybatis;

import org.apache.ibatis.annotations.Mapper;
import reengineering.ddd.accounting.model.Customer;
import reengineering.ddd.accounting.model.SourceEvidence;
import reengineering.ddd.accounting.model.Transaction;

import java.util.List;

@Mapper
public interface ModelMapper {
    Customer findCustomerById(String id);

    List<SourceEvidence<?>> findSourceEvidencesByCustomerId(String customerId);

    List<Transaction> findTransactionsByAccountId(String accountId);
}
