package reengineering.ddd.accounting.mybatis.associations;

import org.springframework.stereotype.Component;
import reengineering.ddd.accounting.model.Customer;
import reengineering.ddd.accounting.mybatis.ModelMapper;

import javax.inject.Inject;
import java.util.Optional;


@Component
public class Customers implements reengineering.ddd.accounting.model.Customers {
    private ModelMapper mapper;

    @Inject
    public Customers(ModelMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Optional<Customer> findById(String id) {
        return Optional.ofNullable(mapper.findCustomerById(id));
    }
}
