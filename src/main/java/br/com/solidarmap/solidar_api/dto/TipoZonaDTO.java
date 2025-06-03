package br.com.solidarmap.solidar_api.dto;

import br.com.solidarmap.solidar_api.model.TipoZona;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TipoZonaDTO {

    private Long id;
    private String zona;

    public TipoZonaDTO(TipoZona tipoZona) {
        this.id = tipoZona.getId();
        this.zona = tipoZona.getZona();
    }
}
