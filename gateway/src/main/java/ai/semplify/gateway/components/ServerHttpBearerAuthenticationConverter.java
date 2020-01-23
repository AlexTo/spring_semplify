package ai.semplify.gateway.components;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class ServerHttpBearerAuthenticationConverter implements ServerAuthenticationConverter {

    private JWTCustomVerifier jwtVerifier;
    private String tokenPrefix;


    public ServerHttpBearerAuthenticationConverter(String publicKey, String tokenPrefix) {
        this.tokenPrefix = tokenPrefix;
        this.jwtVerifier = new JWTCustomVerifier(publicKey);
    }

    @Override
    public Mono<Authentication> convert(ServerWebExchange serverWebExchange) {
        return Mono.justOrEmpty(serverWebExchange)
                .flatMap(AuthorizationHeaderPayload::extract)
                .filter(authValue -> authValue.length() > tokenPrefix.length())
                .flatMap(authValue -> Mono.justOrEmpty(authValue.substring(tokenPrefix.length())))
                .flatMap(jwtVerifier::check)
                .flatMap(UsernamePasswordAuthenticationBearer::create);
    }
}
