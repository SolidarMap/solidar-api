package br.com.solidarmap.solidar_api.repository;

import br.com.solidarmap.solidar_api.model.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {
}
