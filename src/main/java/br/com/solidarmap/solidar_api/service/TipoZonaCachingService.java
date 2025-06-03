package br.com.solidarmap.solidar_api.service;

import br.com.solidarmap.solidar_api.model.TipoZona;
import br.com.solidarmap.solidar_api.repository.TipoZonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TipoZonaCachingService {

    @Autowired
    private TipoZonaRepository tipoZonaRepository;

    @Cacheable(value = "buscarTodosTiposZona")
    public List<TipoZona> findAllTiposZona() {
        return tipoZonaRepository.findAll();
    }

    @Cacheable(value = "buscarTipoZonaPorId", key = "#id")
    public Optional<TipoZona> findTipoZonaById(Long id) {
        return tipoZonaRepository.findById(id);
    }

    @Cacheable(value = "buscarPaginaTiposZona", key = "#req")
    public Page<TipoZona> findAll(PageRequest req) {
        return tipoZonaRepository.findAll(req);
    }

    @CacheEvict(value = {"buscarTodosTiposZona", "buscarTipoZonaPorId", "buscarPaginaTiposZona"}, allEntries = true)
    public void limparCache() {
        System.out.println("Cache de tipos de zonas limpo com sucesso.");
    }
}
