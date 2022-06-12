package reengineering.ddd.archtype;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface EntityCollection<E extends Entity<?, ?>> extends Iterable<E> {
    int size();

    EntityCollection<E> subCollection(int from, int to);

    default Stream<E> stream() {
        return StreamSupport.stream(spliterator(), false);
    }
}
