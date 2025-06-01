package br.com.solidarmap.solidar_api.security;

import br.com.solidarmap.solidar_api.model.Usuario;
import br.com.solidarmap.solidar_api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UsuarioConfig {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Bean
    UserDetailsService gerarUsuario() {

       return email -> {
           Usuario usuario = usuarioRepository.findByEmailforAuth(email)
                   .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));

           return User.builder()
                   .username(usuario.getEmail())
                   .password(usuario.getSenha())
                   .roles(usuario.getTipoUsuario().getNomeTipo())
                   .build();
       };
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
