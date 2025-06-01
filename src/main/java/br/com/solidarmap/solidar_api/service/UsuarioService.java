package br.com.solidarmap.solidar_api.service;

import br.com.solidarmap.solidar_api.dto.UsuarioDTO;
import br.com.solidarmap.solidar_api.model.Usuario;
import br.com.solidarmap.solidar_api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioCachingService usuarioCachingService;

    @Transactional(readOnly = true)
    public Page<UsuarioDTO> paginarTodosOsUsuarios(PageRequest req) {

        Page<Usuario> paginas_usuarios = usuarioCachingService.findAll(req);
        Page<UsuarioDTO> paginas_usuarios_dto = paginas_usuarios.map(UsuarioDTO::new);
        return paginas_usuarios_dto;
    }
}
