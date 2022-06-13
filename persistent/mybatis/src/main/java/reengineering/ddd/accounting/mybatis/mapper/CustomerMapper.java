package reengineering.ddd.accounting.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import reengineering.ddd.accounting.model.Customer;

@Mapper
@Component
public interface CustomerMapper {
    Customer findById(String id);
}
