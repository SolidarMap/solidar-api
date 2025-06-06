package br.com.solidarmap.solidar_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class InserirAjudaRequestDTO {

    private Long idUsuario;
    private Long idRecurso;
    private String descricao;
    private String status;
    private LocalDateTime dataPublicacao;
}
