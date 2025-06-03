package br.com.solidarmap.solidar_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class InserirLocalizacaoRequestDTO {

    private Long idAjuda;
    private Long IdTipoZona;
    private BigDecimal latitude;
    private BigDecimal longitude;
}
