package ai.semplify.commons.feignclients;

import org.springframework.cloud.security.oauth2.client.feign.OAuth2FeignRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;

public class DefaultFeignClientConfiguration {


    @Bean
    public OAuth2FeignRequestInterceptor oAuth2FeignRequestInterceptor(OAuth2ClientContext oAuth2ClientContext,
                                                                       OAuth2ProtectedResourceDetails resource) {
        return new OAuth2FeignRequestInterceptor(oAuth2ClientContext, resource);
    }

    @Bean
    public BearerHeaderFeignClientInterceptor bearerHeaderFeignClientInterceptor() {
        return new BearerHeaderFeignClientInterceptor();
    }
}
