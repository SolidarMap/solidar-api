package br.com.solidarmap.solidar_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class InserirMensagemRequestDTO {

    private Long idAjuda;
    private Long idUsuario;
    private String mensagem;
    private LocalDateTime dataEnvio;
}
