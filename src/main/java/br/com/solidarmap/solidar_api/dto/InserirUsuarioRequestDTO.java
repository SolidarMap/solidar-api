package br.com.solidarmap.solidar_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class InserirUsuarioRequestDTO {

    private Long tipoUsuarioId;
    private String nome;
    private String email;
    private String senha;
    private LocalDateTime dataCriacao;
}
