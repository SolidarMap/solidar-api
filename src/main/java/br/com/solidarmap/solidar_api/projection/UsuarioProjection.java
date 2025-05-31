package br.com.solidarmap.solidar_api.projection;

import br.com.solidarmap.solidar_api.model.TipoUsuario;

import java.time.LocalDateTime;

public interface UsuarioProjection {

    public Long getId();
    public TipoUsuario getTipoUsuario();
    public String getNome();
    public String getEmail();
    public LocalDateTime getDataCriacao();

}
