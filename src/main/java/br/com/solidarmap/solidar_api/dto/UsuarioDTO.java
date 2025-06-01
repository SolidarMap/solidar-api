package br.com.solidarmap.solidar_api.dto;

import br.com.solidarmap.solidar_api.model.TipoUsuario;
import br.com.solidarmap.solidar_api.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {

    private Long id;
    private TipoUsuario tipoUsuario;
    private String nome;
    private String email;
    private LocalDateTime dataCriacao;

    public UsuarioDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.tipoUsuario = usuario.getTipoUsuario();
        this.nome = usuario.getNome();
        this.email = usuario.getEmail();
        this.dataCriacao = usuario.getDataCriacao();
    }
}
