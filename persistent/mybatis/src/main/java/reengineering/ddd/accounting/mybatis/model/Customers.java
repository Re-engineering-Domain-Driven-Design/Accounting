package reengineering.ddd.accounting.mybatis.model;

import org.springframework.stereotype.Component;
import reengineering.ddd.accounting.model.Customer;
import reengineering.ddd.accounting.mybatis.mapper.CustomerMapper;

import javax.inject.Inject;
import java.util.Optional;


@Component
public class Customers implements reengineering.ddd.accounting.model.Customers {
    private CustomerMapper mapper;

    @Inject
    public Customers(CustomerMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Optional<Customer> findById(String id) {
        return Optional.of(mapper.findById(id));
    }
}
