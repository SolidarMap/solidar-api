package br.com.solidarmap.solidar_api.repository;

import br.com.solidarmap.solidar_api.dto.AjudaDTO;
import br.com.solidarmap.solidar_api.model.Ajuda;
import br.com.solidarmap.solidar_api.model.Mensagem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AjudaRepository extends JpaRepository<Ajuda, Long> {

    @Query("""
    SELECT new br.com.solidarmap.solidar_api.dto.AjudaDTO(
        a.id,
        a.usuario.id,
        a.recurso,
        a.descricao,
        a.status,
        a.dataPublicacao
    )
    FROM Ajuda a
""")
    List<AjudaDTO> findAllAjudas();

    @EntityGraph(attributePaths = {"mensagens", "avaliacoes"})
    @Query("SELECT a FROM Ajuda a")
    List<Ajuda> findAllComMensagensEAvaliacoes();

    @Query("SELECT a FROM Ajuda a WHERE a.usuario.id = :idUsuario")
    Page<Ajuda> findAjudasByIdUsuario(@Param("idUsuario") Long idUsuario, Pageable pageable);
}
