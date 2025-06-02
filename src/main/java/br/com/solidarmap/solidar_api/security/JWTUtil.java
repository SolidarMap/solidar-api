package br.com.solidarmap.solidar_api.security;

import br.com.solidarmap.solidar_api.model.Usuario;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
public class JWTUtil {

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        // Define aqui sua chave secreta como uma string (mínimo recomendado: 256 bits codificados em base64)
        String secret = "/uRBJXtTB8eho34YKqrkn/U3EuRWcA+mX3bvQNQR90A=";
        byte[] secretBytes = Base64.getEncoder().encode(secret.getBytes());
        this.secretKey = Keys.hmacShaKeyFor(secretBytes);
    }

    public String construirToken(Usuario usuario) {

        Date data_atual = new Date();

        JwtBuilder builder = Jwts.builder()
                .subject(usuario.getId().toString())
                .claim("nome", usuario.getNome())
                .claim("email", usuario.getEmail())
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

    public Long extrairUsuariobyId(String token) {

        return Long.valueOf(
                Jwts.parser().verifyWith(secretKey).build()
                        .parseSignedClaims(token)
                        .getPayload()
                        .getSubject()
        );
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
