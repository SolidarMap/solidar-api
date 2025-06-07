package br.com.solidarmap.solidar_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InserirAvaliacaoRequestDTO {

    private Long idUsuario;
    private Long idAjuda;
    private Integer nota;
    private String comentario;
    private LocalDateTime dataAvaliacao;

}
