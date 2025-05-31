package br.com.solidarmap.solidar_api.repository;

import br.com.solidarmap.solidar_api.model.Usuario;
import br.com.solidarmap.solidar_api.projection.UsuarioProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

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
    List<UsuarioProjection> findAllUsuarios();

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
    UsuarioProjection findByEmail(@Param("email") String email);

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
    UsuarioProjection findUsuarioById(@Param("id") Long id);
}
