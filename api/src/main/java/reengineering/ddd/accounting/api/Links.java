package reengineering.ddd.accounting.api;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

public class Links {
    public static UriBuilder customer(UriInfo info) {
        return info.getBaseUriBuilder().path(CustomersApi.class).path(CustomersApi.class, "findById");

    }

    public static UriBuilder sourceEvidence(UriInfo info) {
        return customer(info)
                .path(CustomerApi.class, "sourceEvidences")
                .path(SourceEvidencesApi.class, "findById");
    }
}
