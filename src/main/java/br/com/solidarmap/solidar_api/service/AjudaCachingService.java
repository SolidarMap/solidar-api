package br.com.solidarmap.solidar_api.service;

import br.com.solidarmap.solidar_api.dto.AjudaDTO;
import br.com.solidarmap.solidar_api.model.Ajuda;
import br.com.solidarmap.solidar_api.repository.AjudaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AjudaCachingService {

    @Autowired
    private AjudaRepository ajudaRepository;

    @Cacheable(value = "buscarTodasAjudas")
    public List<AjudaDTO> listarTodasAjudas() {
        List<Ajuda> ajudas = ajudaRepository.findAll(); // sem fetch
        return ajudas.stream().map(AjudaDTO::new).toList();
    }

    @Cacheable(value = "buscarPaginaAjudas", key = "#req")
    public Page<Ajuda> paginarAllAjudasPaginadas(PageRequest req) {
        return ajudaRepository.findAll(req);
    }

    @Cacheable(value = "paginarAjudasPorIdUsuario", key = "#req.pageNumber + '-' + #req.pageSize + '-' + #req.sort.toString() + '-' + #id")
    public Page<AjudaDTO> paginarAjudasPorIdUsuario(PageRequest req, Long id) {
        Page<Ajuda> ajudas = ajudaRepository.findAjudasByIdUsuario(id, req);
        return ajudas.map(AjudaDTO::new);
    }

    @CacheEvict(value = {"buscarTodasAjudas", "buscarPaginaAjudas", "paginarAjudasPorIdUsuario"}, allEntries = true)
    public void limparCache() {
        System.out.println("Cache de ajudas limpo com sucesso.");
    }
}
