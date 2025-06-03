package br.com.solidarmap.solidar_api.dto;

import br.com.solidarmap.solidar_api.model.TipoRecurso;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TipoRecursoDTO {

    private Long id;
    private String recurso;

    public TipoRecursoDTO(TipoRecurso tipoRecurso) {
        this.id = tipoRecurso.getId();
        this.recurso = tipoRecurso.getRecurso();
    }
}
