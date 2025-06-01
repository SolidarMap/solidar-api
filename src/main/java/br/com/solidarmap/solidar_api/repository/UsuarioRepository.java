package br.com.solidarmap.solidar_api.repository;

import br.com.solidarmap.solidar_api.dto.UsuarioDTO;
import br.com.solidarmap.solidar_api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query("""
    SELECT u.id AS id,
           u.nome AS nome,
           u.email AS email,
           u.dataCriacao AS dataCriacao,
           u.tipoUsuario AS tipoUsuario
    FROM Usuario u
    JOIN u.tipoUsuario
    """)
    List<UsuarioDTO> findAllUsuarios();

    @Query("""
    SELECT u.id AS id,
           u.nome AS nome,
           u.email AS email,
           u.dataCriacao AS dataCriacao,
           u.tipoUsuario AS tipoUsuario
    FROM Usuario u
    JOIN u.tipoUsuario
    WHERE u.email = :email
    """)
    UsuarioDTO findByEmail(@Param("email") String email);

    @Query("""
    SELECT u.id AS id,
           u.nome AS nome,
           u.email AS email,
           u.dataCriacao AS dataCriacao,
           u.tipoUsuario AS tipoUsuario
    FROM Usuario u
    JOIN u.tipoUsuario
    WHERE u.id = :id
    """)
    UsuarioDTO findUsuarioById(@Param("id") Long id);

    @Query("SELECT u FROM Usuario u WHERE u.email = :email")
    Optional<Usuario> findByEmailforAuth(String email);
}
