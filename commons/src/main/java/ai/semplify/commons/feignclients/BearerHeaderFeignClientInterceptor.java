package ai.semplify.commons.feignclients;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.cloud.security.oauth2.client.feign.OAuth2FeignRequestInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public class BearerHeaderFeignClientInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication instanceof JwtAuthenticationToken) {
            template.header(OAuth2FeignRequestInterceptor.AUTHORIZATION, String.format("%s %s",
                    OAuth2FeignRequestInterceptor.BEARER, ((JwtAuthenticationToken) authentication).getToken().getTokenValue()));
        }
    }
}
