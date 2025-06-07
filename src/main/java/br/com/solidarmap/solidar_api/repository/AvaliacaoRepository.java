package br.com.solidarmap.solidar_api.repository;

import br.com.solidarmap.solidar_api.dto.AvaliacaoDTO;
import br.com.solidarmap.solidar_api.model.Avaliacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {

    @Query("""
    SELECT new br.com.solidarmap.solidar_api.dto.AvaliacaoDTO(
        a.id,
        a.usuario.id,
        a.ajuda.id,
        a.nota,
        a.comentario,
        a.dataAvaliacao
    )
    FROM Avaliacao a
""")
    List<AvaliacaoDTO> findAllAvaliacoes();


    @Query("SELECT a FROM Avaliacao a WHERE a.usuario.id = :idUsuario")
    Page<Avaliacao> findAvaliacoesByIdUsuario(@Param("idUsuario") Long idUsuario, Pageable pageable);

    @Query("SELECT a FROM Avaliacao a WHERE a.ajuda.id = :idAjuda")
    Page<Avaliacao> findAvaliacoesByIdAjuda(@Param("idAjuda") Long idAjuda, Pageable pageable);
}
