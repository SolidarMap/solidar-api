package br.com.solidarmap.solidar_api.repository;

import br.com.solidarmap.solidar_api.model.Mensagem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MensagemRepository extends JpaRepository<Mensagem, Long> {
}
