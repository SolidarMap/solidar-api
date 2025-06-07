package br.com.solidarmap.solidar_api.dto;

import br.com.solidarmap.solidar_api.model.Avaliacao;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AvaliacaoDTO {

    private Long id;
    private Long idUsuario;
    private Long idAjuda;
    private Integer nota;
    private String comentario;
    private LocalDateTime dataAvaliacao;

    // CONSTRUTOR PARA JPQL
    public AvaliacaoDTO(Long id, Long idUsuario, Long idAjuda, Integer nota, String comentario, LocalDateTime dataAvaliacao) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idAjuda = idAjuda;
        this.nota = nota;
        this.comentario = comentario;
        this.dataAvaliacao = dataAvaliacao;
    }

    // CONSTRUTOR PARA map(AvaliacaoDTO::new)
    public AvaliacaoDTO(Avaliacao avaliacao) {
        this.id = avaliacao.getId();
        this.idUsuario = avaliacao.getUsuario().getId();
        this.idAjuda = avaliacao.getAjuda().getId();
        this.nota = avaliacao.getNota();
        this.comentario = avaliacao.getComentario();
        this.dataAvaliacao = avaliacao.getDataAvaliacao();
    }
}
