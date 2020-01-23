package ai.semplify.gateway.components;

import lombok.var;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;

@EnableWebFluxSecurity
public class WebFluxSecurity {

    @Value("${app.security.public-key}")
    private String publicKey;

    @Value("${app.security.token-prefix}")
    private String tokenPrefix;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {

        http.csrf().disable()
                .authorizeExchange()
                .pathMatchers("/**")
                .authenticated()
                .and()
                .addFilterAt(bearerAuthenticationFilter(), SecurityWebFiltersOrder.AUTHENTICATION);

        return http.build();
    }

    private AuthenticationWebFilter bearerAuthenticationFilter() {

        var authManager = new BearerTokenReactiveAuthenticationManager();
        var bearerAuthenticationFilter = new AuthenticationWebFilter(authManager);
        var bearerConverter = new ServerHttpBearerAuthenticationConverter(publicKey, tokenPrefix);

        bearerAuthenticationFilter.setServerAuthenticationConverter(bearerConverter);
        bearerAuthenticationFilter.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers("/**"));

        return bearerAuthenticationFilter;
    }
}
