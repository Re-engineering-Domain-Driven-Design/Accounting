package reengineering.ddd.mybatis.support;

import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InjectableObjectFactory extends DefaultObjectFactory implements ApplicationContextAware {

    private ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    @Override
    public <T> T create(Class<T> type) {
        T object = super.create(type);
        this.context.getAutowireCapableBeanFactory().autowireBean(object);
        return object;
    }

    @Override
    public <T> T create(Class<T> type, List<Class<?>> constructorArgTypes, List<Object> constructorArgs) {
        T object = super.create(type, constructorArgTypes, constructorArgs);
        this.context.getAutowireCapableBeanFactory().autowireBean(object);
        return object;
    }
}
