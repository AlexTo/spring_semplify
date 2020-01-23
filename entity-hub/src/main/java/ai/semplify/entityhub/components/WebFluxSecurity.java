package ai.semplify.entityhub.components;

import lombok.var;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.web.reactive.function.client.ServerBearerExchangeFilterFunction;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.reactive.function.client.WebClient;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@EnableWebFluxSecurity
public class WebFluxSecurity {

    @Value("${app.security.public-key}")
    private String publicKey;

    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.authorizeExchange()
                .anyExchange().authenticated()
                .and()
                .oauth2ResourceServer()
                .jwt();
        return http.build();
    }

    @Bean
    public ReactiveJwtDecoder jwtDecoder() throws NoSuchAlgorithmException, InvalidKeySpecException {
        var kf = KeyFactory.getInstance("RSA");
        var keySpecX509 = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey));
        var pubKey = (RSAPublicKey) kf.generatePublic(keySpecX509);
        return NimbusReactiveJwtDecoder.withPublicKey(pubKey).build();
    }


}
