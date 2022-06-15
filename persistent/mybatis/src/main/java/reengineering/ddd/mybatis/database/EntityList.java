package reengineering.ddd.mybatis.database;

import reengineering.ddd.archtype.Entity;
import reengineering.ddd.archtype.HasMany;
import reengineering.ddd.archtype.Many;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public abstract class EntityList<Id, E extends Entity<Id, ?>> implements Many<E>, HasMany<Id, E> {
    @Override
    public final Many<E> findAll() {
        return this;
    }

    @Override
    public final Optional<E> findByIdentity(Id identifier) {
        return Optional.ofNullable(findEntity(identifier));
    }

    @Override
    public final Many<E> subCollection(int from, int to) {
        return new reengineering.ddd.mybatis.memory.EntityList<>(findEntities(from, to));
    }

    @Override
    public final Iterator<E> iterator() {
        return findAllEntities().iterator();
    }

    protected abstract List<E> findAllEntities();

    protected abstract List<E> findEntities(int from, int to);

    protected abstract E findEntity(Id id);
}
