package br.com.solidarmap.solidar_api.repository;

import br.com.solidarmap.solidar_api.model.Localizacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocalizacaoRepository extends JpaRepository<Localizacao, Long> {
}
