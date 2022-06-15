package reengineering.ddd.mybatis.memory;

import reengineering.ddd.archtype.Entity;
import reengineering.ddd.archtype.HasOne;

public class Reference<E extends Entity<?, ?>> implements HasOne<E> {
    private E entity;

    private Reference() {
    }

    public Reference(E entity) {
        this.entity = entity;
    }

    @Override
    public E get() {
        return entity;
    }
}
