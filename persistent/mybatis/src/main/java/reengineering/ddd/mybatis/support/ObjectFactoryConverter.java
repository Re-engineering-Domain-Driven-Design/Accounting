package reengineering.ddd.mybatis.support;

import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.springframework.beans.BeansException;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@ConfigurationPropertiesBinding
public class ObjectFactoryConverter implements Converter<String, ObjectFactory>, ApplicationContextAware {

    private ApplicationContext context;

    @Override
    public ObjectFactory convert(String source) {
        try {
            ObjectFactory factory = (ObjectFactory) Class.forName(source).getDeclaredConstructor().newInstance();
            if (factory instanceof ApplicationContextAware applicationContextAware)
                applicationContextAware.setApplicationContext(context);
            return factory;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
