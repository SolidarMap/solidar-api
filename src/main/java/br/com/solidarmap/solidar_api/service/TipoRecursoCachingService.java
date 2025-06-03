package br.com.solidarmap.solidar_api.service;

import br.com.solidarmap.solidar_api.model.TipoRecurso;
import br.com.solidarmap.solidar_api.repository.TipoRecursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TipoRecursoCachingService {

    @Autowired
    private TipoRecursoRepository tipoRecursoRepository;

    @Cacheable(value = "buscarTodosTiposRecurso")
    public List<TipoRecurso> findAllTiposRecurso() {
        return tipoRecursoRepository.findAll();
    }

    @Cacheable(value = "buscarTipoRecursoPorId", key = "#id")
    public Optional<TipoRecurso> findTipoRecursoById(Long id) {
        return tipoRecursoRepository.findById(id);
    }

    @Cacheable(value = "buscarPaginaTiposRecurso", key = "#req")
    public Page<TipoRecurso> findAll(PageRequest req) {
        return tipoRecursoRepository.findAll(req);
    }

    @CacheEvict(value = {"buscarTodosTiposRecurso", "buscarTipoRecursoPorId", "buscarPaginaTiposRecurso"}, allEntries = true)
    public void limparCache() {
        System.out.println("Cache de tipos de recursos limpo com sucesso.");
    }


}
