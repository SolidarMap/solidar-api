package br.com.solidarmap.solidar_api.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JWTUtil {

    private final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String construirToken(String email) {

        Date data_atual = new Date();

        JwtBuilder builder = Jwts.builder()
                .subject(email)
                .issuedAt(data_atual)
                .expiration(new Date(data_atual.getTime() + (3600000)))
                .signWith(secretKey);
        return builder.compact();
    }

    public Claims extrairClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extrairEmail(String token) {

        JwtParser parser = Jwts.parser().verifyWith(secretKey).build();

        return parser.parseSignedClaims(token).getPayload().getSubject();
    }

    public boolean validarToken(String token) {
        try {
            JwtParser parser = Jwts.parser().verifyWith(secretKey).build();
            parser.parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
