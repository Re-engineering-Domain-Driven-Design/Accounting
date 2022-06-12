package reengineering.ddd.accounting.api;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

public class ApiTemplates {
    public static UriBuilder customer(UriInfo info) {
        return info.getBaseUriBuilder().path(CustomersApi.class).path(CustomersApi.class, "findById");

    }

    public static UriBuilder sourceEvidences(UriInfo info) {
        return customer(info).path(CustomerApi.class, "sourceEvidences");
    }

    public static UriBuilder sourceEvidence(UriInfo info) {
        return sourceEvidences(info).path(SourceEvidencesApi.class, "findById");
    }
}
