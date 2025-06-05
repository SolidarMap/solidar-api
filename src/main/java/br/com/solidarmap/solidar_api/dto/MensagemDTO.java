package br.com.solidarmap.solidar_api.dto;

import br.com.solidarmap.solidar_api.model.Mensagem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MensagemDTO {

    private Long id;
    private Long idAjuda;
    private Long idUsuario;
    private String mensagem;
    private LocalDateTime dataEnvio;

    public MensagemDTO(Mensagem mensagem) {
        this.id = mensagem.getId();
        this.idAjuda = mensagem.getAjuda().getId();
        this.idUsuario = mensagem.getUsuario().getId();
        this.mensagem = mensagem.getMensagem();
        this.dataEnvio = mensagem.getDataEnvio();
    }
}
