package reengineering.ddd.accounting.api.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import reengineering.ddd.accounting.api.CustomersApi;

import static org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType.HAL;

@Configuration
@EnableHypermediaSupport(type = HAL)
public class Jersey extends ResourceConfig {
    public Jersey() {
        register(CustomersApi.class);
    }
}
