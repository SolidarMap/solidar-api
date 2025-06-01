package br.com.solidarmap.solidar_api.controller;

import br.com.solidarmap.solidar_api.dto.JWTLoginRequestDTO;
import br.com.solidarmap.solidar_api.dto.JWTLoginResponseDTO;
import br.com.solidarmap.solidar_api.dto.JWTValidarTokenRequestDTO;
import br.com.solidarmap.solidar_api.dto.JWTValidarTokenResponseDTO;
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


    @Operation(summary = "Verificar se o token é válido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token válido."),
            @ApiResponse(responseCode = "401", description = "Token inválido ou expirado.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Erro inesperado ao validar token.", content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping("/validarToken")
    public JWTValidarTokenResponseDTO verificarToken(@RequestBody JWTValidarTokenRequestDTO token) {
        boolean statusToken;
        try {
            statusToken = jwtUtil.validarToken(token.getToken());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro inesperado ao validar token: " + e.getMessage());
        }

        if (!statusToken) {
            return new JWTValidarTokenResponseDTO(
                    token.getToken(),
                    false,
                    "Token inválido ou expirado."
            );
        }

        return new JWTValidarTokenResponseDTO(
                token.getToken(),
                true,
                "Token válido."
        );
    }
}
