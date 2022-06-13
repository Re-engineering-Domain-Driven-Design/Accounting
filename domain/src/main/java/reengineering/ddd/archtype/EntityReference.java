package reengineering.ddd.archtype;

public interface EntityReference<E extends Entity<?, ?>> {
    E get();
}
