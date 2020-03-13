package ai.semplify.tasker.components;

import feign.Request;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class Feign {

    @Bean
    public static Request.Options requestOptions(ConfigurableEnvironment env) {
        int ribbonReadTimeout = env.getProperty("ribbon.ReadTimeout", int.class, 70000);
        int ribbonConnectionTimeout = env.getProperty("ribbon.ConnectTimeout", int.class, 60000);

        return new Request.Options(ribbonConnectionTimeout,
                TimeUnit.MILLISECONDS,
                ribbonReadTimeout,
                TimeUnit.MILLISECONDS, true);
    }
}
