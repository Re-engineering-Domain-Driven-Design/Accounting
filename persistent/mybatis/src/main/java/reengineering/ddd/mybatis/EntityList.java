package reengineering.ddd.mybatis;

import reengineering.ddd.archtype.Entity;
import reengineering.ddd.archtype.EntityCollection;

import java.util.Iterator;
import java.util.List;

public class EntityList<E extends Entity<?, ?>> implements EntityCollection<E> {
    private List<E> list;

    public EntityList(List<E> list) {
        this.list = list;
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public EntityCollection<E> subCollection(int from, int to) {
        return new EntityList<>(list.subList(from, to));
    }

    @Override
    public Iterator<E> iterator() {
        return list.iterator();
    }
}
