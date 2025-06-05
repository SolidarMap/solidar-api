package br.com.solidarmap.solidar_api.repository;

import br.com.solidarmap.solidar_api.dto.MensagemDTO;
import br.com.solidarmap.solidar_api.model.Mensagem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MensagemRepository extends JpaRepository<Mensagem, Long> {

    @Query(value = "SELECT * FROM T_SMP_MENSAGENS", nativeQuery = true)
    List<MensagemDTO> findAllMensagens();

    @Query("SELECT m FROM Mensagem m WHERE m.ajuda.id = :idAjuda")
    Page<Mensagem> findMensagensByIdAjuda(@Param("idAjuda") Long idAjuda, Pageable pageable);

    @Query("SELECT m FROM Mensagem m WHERE m.ajuda.id = :idAjuda AND m.usuario.id = :idUsuario")
    Page<Mensagem> findByAjudaIdAndUsuarioId(@Param("idAjuda") Long idAjuda, @Param("idUsuario") Long idUsuario, Pageable pageable);

}
