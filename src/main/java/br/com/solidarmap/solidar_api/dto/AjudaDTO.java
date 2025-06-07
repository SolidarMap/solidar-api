package br.com.solidarmap.solidar_api.dto;

import br.com.solidarmap.solidar_api.model.Ajuda;
import br.com.solidarmap.solidar_api.model.TipoRecurso;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AjudaDTO {

    private Long id;
    private Long idUsuario;
    private TipoRecurso tipoRecurso;
    private String descricao;
    private String status;
    private LocalDateTime dataPublicacao;

    public AjudaDTO(Ajuda ajuda) {
        this.id = ajuda.getId();
        this.idUsuario = ajuda.getUsuario().getId();
        this.tipoRecurso = ajuda.getRecurso();
        this.descricao = ajuda.getDescricao();
        this.status = ajuda.getStatus();
        this.dataPublicacao = ajuda.getDataPublicacao();
    }
}
