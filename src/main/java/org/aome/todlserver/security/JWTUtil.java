package org.aome.todlserver.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class JWTUtil {
    @Value("${tokenSecret}")
    private String tokenSecret;
    @Value("${expirationMinutes}")
    private Integer minutes;
    private final String subject = "User details";
    public String generateToken(String username){
        Date expirationDate = Date.from(ZonedDateTime.now().plusMinutes(minutes).toInstant());

        return JWT.create()
                .withSubject(subject)
                .withClaim("username", username)
                .withIssuer("TodL")
                .withIssuedAt(new Date())
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC256(tokenSecret));
    }

    public String validateTokenAndRetrievedClaim(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(tokenSecret))
                .withSubject(subject)
                .withIssuer("TodL")
                .build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("username").asString();
    }
}
