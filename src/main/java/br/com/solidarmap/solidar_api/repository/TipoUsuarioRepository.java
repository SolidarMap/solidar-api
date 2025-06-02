package br.com.solidarmap.solidar_api.repository;

import br.com.solidarmap.solidar_api.model.TipoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TipoUsuarioRepository extends JpaRepository<TipoUsuario, Long> {

    @Query("SELECT COUNT(t) > 0 FROM TipoUsuario t WHERE LOWER(t.nomeTipo) = LOWER(:nomeTipo)")
    boolean existsByNomeTipo(@Param("nomeTipo") String nomeTipo);
}
