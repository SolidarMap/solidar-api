package br.com.solidarmap.solidar_api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SegurancaConfig {

    @Autowired
    private JWTAuthFilter jwtAuthFilter;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain filtrarRota(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(request ->
                        request.requestMatchers("/swagger-ui/**",
                                        "/v3/api-docs/**",
                                        "/v3/api-docs",
                                        "/swagger-resources/**",
                                        "/swagger-resources",
                                        "/configuration/ui",
                                        "/configuration/security",
                                        "/webjars/**",
                                        "/auth/**",
                                        "/usuario/inserir")
                                .permitAll().anyRequest().authenticated())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(servidor ->
                        servidor.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }
}
