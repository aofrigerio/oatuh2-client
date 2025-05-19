package br.com.github.oauth2.service.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    private String issue = "Universal Client ID";

    public String gerarToken(OAuth2AuthenticationToken authenticationToken){

        OAuth2User user = authenticationToken.getPrincipal();
        String email = user.getAttribute("email");

        try {
            var algorithm = Algorithm.HMAC256("123456");
                return JWT.create()
                    .withIssuer(issue)
                        .withSubject(email)
                        .withExpiresAt(dataExpiracao())
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            throw new RuntimeException("erro ao gerar o token jwt", exception);
        }
    }

    public String getSubject(String token){
        try {
            var algorithm = Algorithm.HMAC256("123456");
            return JWT.require(algorithm)
                    .withIssuer(issue)
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception){
            throw new RuntimeException(exception.getMessage());
        }
    }

    private Instant dataExpiracao() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
