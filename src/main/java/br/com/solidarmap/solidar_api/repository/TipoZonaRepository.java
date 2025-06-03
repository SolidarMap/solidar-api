package br.com.solidarmap.solidar_api.repository;

import br.com.solidarmap.solidar_api.model.TipoZona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TipoZonaRepository extends JpaRepository<TipoZona, Long> {

    @Query("SELECT COUNT(t) > 0 FROM TipoZona t WHERE LOWER(t.zona) = LOWER(:zona)")
    boolean existsByZona(@Param("zona") String zona);
}
