package ai.semplify.tasker.components;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ApplicationContextHolder implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public <T> T getBean(Class<T> beanClass) {
        return Objects.nonNull(context) ? context.getBean(beanClass) : null;
    }

    public <T> T getBean(String qualifier, Class<T> beanClass) {
        return Objects.nonNull(context) ? context.getBean(qualifier, beanClass) : null;
    }

}

