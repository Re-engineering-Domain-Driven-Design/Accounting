package reengineering.ddd.accounting.api.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import reengineering.ddd.accounting.api.CustomersApi;

import java.util.Map;

import static org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType.HAL;

@Configuration
@EnableHypermediaSupport(type = HAL)
public class Jersey extends ResourceConfig {
    public Jersey() {
        setProperties(Map.of(ServerProperties.RESPONSE_SET_STATUS_OVER_SEND_ERROR, true));
        register(CustomersApi.class);
    }
}
