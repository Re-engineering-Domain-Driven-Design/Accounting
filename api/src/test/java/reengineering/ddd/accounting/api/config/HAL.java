package reengineering.ddd.accounting.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.mediatype.MessageResolver;
import org.springframework.hateoas.mediatype.hal.CurieProvider;
import org.springframework.hateoas.mediatype.hal.Jackson2HalModule;
import org.springframework.hateoas.server.LinkRelationProvider;

import javax.inject.Inject;

@Configuration
public class HAL implements InitializingBean {

    private ObjectMapper mapper;
    private LinkRelationProvider provider;
    private MessageResolver resolver;

    @Inject
    public HAL(ObjectMapper mapper, LinkRelationProvider provider, MessageResolver resolver) {
        this.mapper = mapper;
        this.provider = provider;
        this.resolver = resolver;
    }

    @Override
    public void afterPropertiesSet() {
        mapper.registerModule(new Jackson2HalModule());
        mapper.setHandlerInstantiator(new Jackson2HalModule.HalHandlerInstantiator(provider, CurieProvider.NONE, resolver));
    }
}
