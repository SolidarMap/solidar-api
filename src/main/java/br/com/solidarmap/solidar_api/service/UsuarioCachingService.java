package br.com.solidarmap.solidar_api.service;

import br.com.solidarmap.solidar_api.dto.UsuarioDTO;
import br.com.solidarmap.solidar_api.model.Usuario;
import br.com.solidarmap.solidar_api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioCachingService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Cacheable(value = "buscarTodosUsuarios")
    public List<UsuarioDTO> findAllUsuarios() {
        return usuarioRepository.findAllUsuarios();
    }

    @Cacheable(value = "buscarPaginasUsuarios", key = "#req")
    public Page<Usuario> findAll(PageRequest req) {
        return usuarioRepository.findAll(req);
    }

    @Cacheable(value = "buscarUsuarioPorId", key = "#id")
    public Optional<Usuario> findUsuarioById(Long id) {
        return usuarioRepository.findById(id);
    }

    @CacheEvict(value = {"buscarTodosUsuarios", "buscarPaginasUsuarios", "buscarUsuarioPorId"}, allEntries = true)
    public void limparCache() {
        System.out.println("Cache de usuários limpo com sucesso.");
    }
}
