package br.com.solidarmap.solidar_api.repository;

import br.com.solidarmap.solidar_api.dto.LocalizacaoDTO;
import br.com.solidarmap.solidar_api.model.Localizacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LocalizacaoRepository extends JpaRepository<Localizacao, Long> {

    @Query("SELECT l FROM Localizacao l WHERE l.ajuda.id = :ajudaId")
    LocalizacaoDTO findLocalizacaoByAjudaId(@Param("ajudaId") Long ajudaId);

    @Query("SELECT l FROM Localizacao l WHERE l.id = :id")
    LocalizacaoDTO findByIdLocalizacao(@Param("id") Long id);


}
