package reengineering.ddd.accounting.mybatis.model;

import org.springframework.stereotype.Component;
import reengineering.ddd.accounting.model.Customer;
import reengineering.ddd.accounting.mybatis.mapper.CustomersMapper;

import javax.inject.Inject;
import java.util.Optional;


@Component
public class Customers implements reengineering.ddd.accounting.model.Customers {
    private CustomersMapper mapper;

    @Inject
    public Customers(CustomersMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Optional<Customer> findById(String id) {
        return Optional.of(mapper.findById(id));
    }
}
