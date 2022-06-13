package reengineering.ddd.accounting.api;

import reengineering.ddd.archtype.Entity;
import reengineering.ddd.archtype.Many;

import java.util.Iterator;
import java.util.List;

class EntityList<E extends Entity<?, ?>> implements Many<E> {
    private List<E> list;

    public EntityList(List<E> list) {
        this.list = list;
    }

    public EntityList(E... entities) {
        this(List.of(entities));
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public Many<E> subCollection(int from, int to) {
        return new EntityList<>(list.subList(from, to));
    }

    @Override
    public Iterator<E> iterator() {
        return list.iterator();
    }
}
