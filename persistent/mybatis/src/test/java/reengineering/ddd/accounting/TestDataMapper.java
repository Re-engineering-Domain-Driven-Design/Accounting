package reengineering.ddd.accounting;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface TestDataMapper {
    @Insert("INSERT INTO customers(id, name, email) VALUES(#{id}, #{name}, #{email})")
    void insertCustomer(@Param("id") String id, @Param("name") String name, @Param("email") String email);

    @Insert("INSERT INTO source_evidences(id, customer_id, type) VALUES(#{id}, #{customer_id}, #{type})")
    void insertSourceEvidence(@Param("id") String id, @Param("customer_id") String customerId, @Param("type") String type);

    @Insert("INSERT INTO sales_settlements(id, order_id, account_id, total_amount, total_currency) VALUES(#{id}, #{order_id}, #{account_id}, #{total_amount}, #{total_currency})")
    void insertSalesSettlement(@Param("id") String id, @Param("order_id") String orderId, @Param("account_id") String accountId, @Param("total_amount") double amount, @Param("total_currency") String currency);

    @Insert("INSERT INTO sales_settlement_details(id, sales_settlement_id, amount, currency) VALUES(#{id}, #{sales_settlement_id}, #{amount}, #{currency})")
    void insertSalesSettlementDetail(@Param("id") String id, @Param("sales_settlement_id") String salesSettlementId, @Param("amount") double amount, @Param("currency") String currency);

    @Insert("INSERT INTO accounts(id, customer_id, current_amount, current_currency) VALUES(#{id}, #{customer_id}, #{current_amount}, #{current_currency})")
    void insertAccount(@Param("id") String id, @Param("customer_id") String customerId, @Param("current_amount") double amount, @Param("current_currency") String currency);

    @Insert("INSERT INTO transactions(id, account_id, source_evidence_id, amount, currency, created_at) VALUES(#{id}, #{account_id}, #{source_evidence_id}, #{amount}, #{currency}, #{created_at})")
    void insertTransaction(@Param("id") String id, @Param("account_id") String accountId, @Param("source_evidence_id") String evidenceId, @Param("amount") double amount, @Param("currency") String currency, @Param("created_at") LocalDateTime createdAt);
}
