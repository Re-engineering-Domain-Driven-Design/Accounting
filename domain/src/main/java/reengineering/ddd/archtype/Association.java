package reengineering.ddd.archtype;

import java.util.Optional;

public interface Association<ID, E extends Entity<ID, ?>> {
    EntityCollection<E> findAll();

    Optional<E> findByIdentity(ID identifier);
}
