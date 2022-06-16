package reengineering.ddd.accounting.test;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TestDataMapper {
    @Insert("INSERT INTO customers(id, name, email) VALUES(#{id}, #{name}, #{email})")
    void insertCustomer(@Param("id") String id, @Param("name") String name, @Param("email") String email);

    @Insert("INSERT INTO accounts(id, customer_id, current_amount, current_currency) VALUES(#{id}, #{customer_id}, #{current_amount}, #{current_currency})")
    void insertAccount(@Param("id") String id, @Param("customer_id") String customerId, @Param("current_amount") double amount, @Param("current_currency") String currency);
}
