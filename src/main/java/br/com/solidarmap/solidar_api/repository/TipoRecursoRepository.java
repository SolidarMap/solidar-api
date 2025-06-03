package br.com.solidarmap.solidar_api.repository;

import br.com.solidarmap.solidar_api.model.TipoRecurso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TipoRecursoRepository extends JpaRepository<TipoRecurso, Long> {

    @Query("SELECT COUNT(t) > 0 FROM TipoRecurso t WHERE LOWER(t.recurso) = LOWER(:recurso)")
    boolean existsByRecurso(@Param("recurso") String recurso);
}
