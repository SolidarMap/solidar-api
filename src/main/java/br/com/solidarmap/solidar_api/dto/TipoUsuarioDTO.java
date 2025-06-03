package br.com.solidarmap.solidar_api.dto;

import br.com.solidarmap.solidar_api.model.TipoUsuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TipoUsuarioDTO {

    private Long id;
    private String nomeTipo;

    public TipoUsuarioDTO(TipoUsuario tipoUsuario) {
        this.id = tipoUsuario.getId();
        this.nomeTipo = tipoUsuario.getNomeTipo();
    }
}
