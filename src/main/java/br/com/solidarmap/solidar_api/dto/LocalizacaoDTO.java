package br.com.solidarmap.solidar_api.dto;

import br.com.solidarmap.solidar_api.model.Localizacao;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LocalizacaoDTO {

    private Long id;
    private Long idAjuda;
    private Long IdTipoZona;
    private BigDecimal latitude;
    private BigDecimal longitude;

    public LocalizacaoDTO(Localizacao localizacao) {
        this.id = localizacao.getId();
        this.idAjuda = localizacao.getAjuda().getId();
        this.IdTipoZona = localizacao.getTipoZona().getId();
        this.latitude = localizacao.getLatitude();
        this.longitude = localizacao.getLongitude();
    }

}
