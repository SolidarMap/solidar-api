package br.com.solidarmap.solidar_api.controller;

import br.com.solidarmap.solidar_api.dto.JWTLoginRequestDTO;
import br.com.solidarmap.solidar_api.dto.JWTLoginResponseDTO;
import br.com.solidarmap.solidar_api.security.JWTUtil;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Operações relacionadas à autenticação.")
public class AuthController {

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;


    @Operation(summary = "Solicitar token de autenticação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token gerado com sucesso."),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas.", content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping("/login")
    public JWTLoginResponseDTO gerarToken(@RequestBody JWTLoginRequestDTO login) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(login.getEmail(), login.getSenha())
            );
            String token = jwtUtil.construirToken(login.getEmail());
            Claims claims = jwtUtil.extrairClaims(token);

            return new JWTLoginResponseDTO(
                    token,
                    claims.getSubject(),
                    claims.getIssuedAt(),
                    claims.getExpiration()
            );

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas");
        }
    }
}
