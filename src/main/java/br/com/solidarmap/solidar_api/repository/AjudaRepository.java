package br.com.solidarmap.solidar_api.repository;

import br.com.solidarmap.solidar_api.dto.AjudaDTO;
import br.com.solidarmap.solidar_api.model.Ajuda;
import br.com.solidarmap.solidar_api.model.Mensagem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AjudaRepository extends JpaRepository<Ajuda, Long> {

    @Query(value = "SELECT * FROM T_SMP_AJUDAS", nativeQuery = true)
    List<AjudaDTO> findAllAjudas();

    @Query("SELECT a FROM Ajuda a WHERE a.usuario.id = :idUsuario")
    Page<Ajuda> findAjudasByIdUsuario(@Param("idUsuario") Long idUsuario, Pageable pageable);
}
