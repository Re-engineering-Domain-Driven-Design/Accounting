package reengineering.ddd.mybatis.memory;

import reengineering.ddd.archtype.Entity;
import reengineering.ddd.archtype.HasMany;
import reengineering.ddd.archtype.Many;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class EntityList<Id, E extends Entity<Id, ?>> implements Many<E>, HasMany<Id, E> {
    private List<E> list = new ArrayList<>();

    public EntityList() {
    }

    public EntityList(List<E> list) {
        this.list = list;
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

    @Override
    public Many<E> findAll() {
        return this;
    }

    @Override
    public Optional<E> findByIdentity(Id identifier) {
        return stream().filter(it -> it.getIdentity().equals(identifier)).findFirst();
    }
}
