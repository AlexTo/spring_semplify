package ai.semplify.gateway.components;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.SignedJWT;
import lombok.var;
import reactor.core.publisher.Mono;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.text.ParseException;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.function.Predicate;

public class JWTCustomVerifier {

    private String publicKey;
    private JWSVerifier jwsVerifier;

    public JWTCustomVerifier(String publicKey) {
        this.publicKey = publicKey;
        this.jwsVerifier = this.buildJWSVerifier();
    }

    public Mono<SignedJWT> check(String token) {
        return Mono.justOrEmpty(createJWS(token))
                .filter(isNotExpired)
                .filter(validSignature);
    }

    private Predicate<SignedJWT> isNotExpired = token ->
            getExpirationDate(token).after(Date.from(Instant.now()));

    private Predicate<SignedJWT> validSignature = token -> {
        try {
            return token.verify(this.jwsVerifier);
        } catch (JOSEException e) {
            e.printStackTrace();
            return false;
        }
    };

    private RSASSAVerifier buildJWSVerifier() {
        try {
            var kf = KeyFactory.getInstance("RSA");
            var keySpecX509 = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey));
            var pubKey = (RSAPublicKey) kf.generatePublic(keySpecX509);
            return new RSASSAVerifier(pubKey);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            return null;
        }
    }

    private SignedJWT createJWS(String token) {
        try {
            return SignedJWT.parse(token);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Date getExpirationDate(SignedJWT token) {
        try {
            return token.getJWTClaimsSet()
                    .getExpirationTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
