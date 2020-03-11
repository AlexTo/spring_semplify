package ai.semplify.tasker.components;

import ai.semplify.tasker.listeners.RootAwareEventListenerIntegrator;
import org.hibernate.jpa.boot.spi.IntegratorProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.Optional;
import java.util.Properties;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class Jpa {


    @Bean
    public AuditorAware<String> auditorProvider() {

        return () -> Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication() != null
                ? SecurityContextHolder.getContext().getAuthentication().getName()
                : null);
    }

    public Jpa(LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        Properties properties = new Properties();

        properties.put(
                "hibernate.integrator_provider",
                (IntegratorProvider) () -> Collections.singletonList(RootAwareEventListenerIntegrator.INSTANCE));

        entityManagerFactory.setJpaProperties(properties);
    }


}
