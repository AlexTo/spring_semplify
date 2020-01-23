package ai.semplify.gateway.components;

import com.nimbusds.jwt.SignedJWT;
import lombok.var;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import reactor.core.publisher.Mono;

import java.text.ParseException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UsernamePasswordAuthenticationBearer {

    public static Mono<Authentication> create(SignedJWT signedJWT) {
        String subject;
        String auths;

        try {
            subject = signedJWT.getJWTClaimsSet().getSubject();
            auths = (String) signedJWT.getJWTClaimsSet().getClaim("roles");
        } catch (ParseException e) {
            return Mono.empty();
        }

        //TODO: Add roles
        /*
        var authorities = Stream.of(auths.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());*/

        return Mono.justOrEmpty(new UsernamePasswordAuthenticationToken(subject, null, null));

    }
}
