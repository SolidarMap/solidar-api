package br.com.solidarmap.solidar_api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class InserirTipoUsuarioRequestDTO {

    private String nomeTipo;
}
