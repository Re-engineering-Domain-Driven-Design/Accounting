package reengineering.ddd.mybatis;

import reengineering.ddd.archtype.Entity;
import reengineering.ddd.archtype.EntityReference;

public class Reference<E extends Entity<?, ?>> implements EntityReference<E> {
    private E entity;

    @Override
    public E get() {
        return entity;
    }
}
