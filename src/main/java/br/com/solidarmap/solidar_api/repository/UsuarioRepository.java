package br.com.solidarmap.solidar_api.repository;

import br.com.solidarmap.solidar_api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
