package br.com.solidarmap.solidar_api.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class UsuarioConfig {

    @Bean
    UserDetailsService gerarUsuario() {

        return new InMemoryUserDetailsManager(User
                .withUsername("pedro.antonieti@gmail.com")
                .password("{noop}pedro1971")
                .roles("Admin")
                .build());
    }
}
