package reengineering.ddd.accounting.model;

import java.util.Optional;

public interface Customers {
    Optional<Customer> findById(String id);
}
