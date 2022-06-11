package reengineering.ddd.accounting.domain;

import java.util.Optional;

public interface Customers {
    Optional<Customer> findById(String id);
}
