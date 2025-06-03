package br.com.solidarmap.solidar_api.service;

import br.com.solidarmap.solidar_api.dto.TipoUsuarioDTO;
import br.com.solidarmap.solidar_api.model.TipoUsuario;
import br.com.solidarmap.solidar_api.repository.TipoUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TipoUsuarioService {

    @Autowired
    private TipoUsuarioRepository tipoUsuarioRepository;

    @Autowired
    private TipoUsuarioCachingService tipoUsuarioCachingService;

    @Transactional(readOnly = true)
    public Page<TipoUsuarioDTO> paginarTodosOsTiposDeUsuario(PageRequest req) {
        Page<TipoUsuario> paginas_tipo_usuario = tipoUsuarioCachingService.findAll(req);
        Page<TipoUsuarioDTO> paginar_tipos_usuarios_dto = paginas_tipo_usuario.map(TipoUsuarioDTO::new);
        return paginar_tipos_usuarios_dto;
    }
}
