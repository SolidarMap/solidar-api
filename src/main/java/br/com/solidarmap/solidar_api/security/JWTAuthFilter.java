package br.com.solidarmap.solidar_api.security;

import br.com.solidarmap.solidar_api.model.Usuario;
import br.com.solidarmap.solidar_api.repository.UsuarioRepository;
import io.swagger.v3.oas.models.OpenAPI;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class JWTAuthFilter extends OncePerRequestFilter {

    private final OpenAPI configurarSwagger;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    JWTAuthFilter(OpenAPI configurarSwagger) {
        this.configurarSwagger = configurarSwagger;
    }

    private static final List<String> PUBLIC_PATHS = List.of(
            "/swagger-ui", "/v3/api-docs", "/swagger-resources", "/webjars",
            "/configuration/ui", "/configuration/security", "/auth", "/auth/login", "/auth/validarToken", "/usuario/inserir"
    );

    private boolean isPublicPath(String path) {
        return PUBLIC_PATHS.stream().anyMatch(path::startsWith);
    }

    private void escreverErro(HttpServletResponse response, String mensagem, int statusCode, String path) throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(String.format("""
                {
                    "timestamp": "%s",
                    "status": %d,
                    "message": "%s",
                    "path": "%s"
                }
                """, LocalDateTime.now(), statusCode, mensagem, path));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        if (isPublicPath(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = request.getHeader("Authorization");

        try {
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);

                Long usuarioId = jwtUtil.extrairUsuariobyId(token);

                if (usuarioId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);

                    if (usuario == null) {
                        escreverErro(response, "Usuário não encontrado.", HttpStatus.FORBIDDEN.value(), path);
                        return;
                    }

                    UserDetails userDetails = userDetailsService.loadUserByUsername(usuario.getEmail());

                    if (jwtUtil.validarToken(token)) {
                        Authentication auth = new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    } else {
                        escreverErro(response, "Token JWT inválido.", HttpStatus.FORBIDDEN.value(), path);
                        return;
                    }
                }
            } else {
                escreverErro(response, "Token JWT ausente.", HttpStatus.FORBIDDEN.value(), path);
                return;
            }
        } catch (Exception e) {
            escreverErro(response, "Token inválido ou expirado.", HttpStatus.FORBIDDEN.value(), path);
            return;
        }


        filterChain.doFilter(request, response);
    }
}