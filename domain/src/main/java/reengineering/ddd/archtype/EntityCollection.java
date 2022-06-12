package reengineering.ddd.archtype;

public interface EntityCollection<E extends Entity<?, ?>> extends Iterable<E> {
    int size();

    EntityCollection<E> subCollection(int from, int to);
}
