package br.com.solidarmap.solidar_api.service;

import br.com.solidarmap.solidar_api.model.TipoUsuario;
import br.com.solidarmap.solidar_api.repository.TipoUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TipoUsuarioCachingService {

    @Autowired
    private TipoUsuarioRepository tipoUsuarioRepository;

    @Cacheable(value = "buscarTodosTiposUsuario")
    public List<TipoUsuario> findAllTiposUsuario() {
        return tipoUsuarioRepository.findAll();
    }

    @Cacheable(value = "buscarTipoUsuarioPorId", key = "#id")
    public Optional<TipoUsuario> findTipoUsuarioById(Long id) {
        return tipoUsuarioRepository.findById(id);
    }

    @Cacheable(value = "buscarPaginaTiposUsuario", key = "#req")
    public Page<TipoUsuario> findAll(PageRequest req) {
        return tipoUsuarioRepository.findAll(req);
    }

    @CacheEvict(value = {"buscarTodosTiposUsuario", "buscarTipoUsuarioPorId", "buscarPaginaTiposUsuario"}, allEntries = true)
    public void limparCache() {
        System.out.println("Cache de tipos de usuários limpo com sucesso.");
    }
}
