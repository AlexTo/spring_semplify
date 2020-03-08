package ai.semplify.feignclients.config;

import ai.semplify.feignclients.interceptors.BearerHeaderFeignClientInterceptor;
import org.springframework.context.annotation.Bean;

public class DefaultFeignClientConfiguration {

    @Bean
    public BearerHeaderFeignClientInterceptor bearerHeaderFeignClientInterceptor() {
        return new BearerHeaderFeignClientInterceptor();
    }
}
