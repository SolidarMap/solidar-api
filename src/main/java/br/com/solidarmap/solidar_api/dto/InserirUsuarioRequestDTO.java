package br.com.solidarmap.solidar_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class InserirUsuarioRequestDTO {

    @NotNull(message = "ID do tipo de usuário é obrigatório.")
    private Long tipoUsuarioId;

    @NotBlank(message = "Nome é obrigatório.")
    private String nome;

    @NotBlank(message = "E-mail é obrigatório.")
    @Email(message = "E-mail inválido.")
    private String email;

    @NotBlank(message = "Senha é obrigatória.")
    private String senha;

    @NotNull(message = "Data de criação é obrigatória.")
    private LocalDateTime dataCriacao;
}
