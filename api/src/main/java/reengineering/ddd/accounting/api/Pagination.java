package reengineering.ddd.accounting.api;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import reengineering.ddd.archtype.Entity;
import reengineering.ddd.archtype.Many;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Pagination<E extends Entity<?, ?>> {
    private final int total;
    private Many<E> many;

    private int pageSize;

    public Pagination(Many<E> many, int pageSize) {
        this.many = many;
        this.total = many.size();
        this.pageSize = pageSize;
    }

    public <M> CollectionModel<M> page(int page, Function<E, M> toModel, Function<Integer, URI> toUri) {
        if (!withInRange(page)) throw new WebApplicationException(Response.Status.NOT_FOUND);

        PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(pageSize, page, total);
        Many<E> current = many.subCollection(page * pageSize, Math.min(total, (page + 1) * pageSize));

        Map<String, Integer> pages = Map.of("self", page, "prev", page - 1, "next", page + 1);

        return PagedModel.of(current.stream().map(toModel).toList(), metadata).add(
                pages.entrySet().stream().filter(e -> withInRange(e.getValue()))
                        .map(e -> Link.of(getFile(toUri.apply(e.getValue())), e.getKey())).toList());
    }

    private String getFile(URI uri) {
        try {
            return uri.toURL().getFile();
        } catch (MalformedURLException e) {
            throw new WebApplicationException(e);
        }
    }

    private boolean withInRange(int page) {
        return page >= 0 && page * pageSize <= total;
    }
}
