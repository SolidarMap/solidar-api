package br.com.solidarmap.solidar_api.service;

import br.com.solidarmap.solidar_api.dto.AvaliacaoDTO;
import br.com.solidarmap.solidar_api.model.Avaliacao;
import br.com.solidarmap.solidar_api.repository.AvaliacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AvaliacaoCachingService {

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    @Cacheable(value = "buscarTodasAvaliacoes")
    public List<AvaliacaoDTO> listarTodasAvaliacoes() {
        return avaliacaoRepository.findAllAvaliacoes();
    }

    @Cacheable(value = "buscarPaginaAvaliacoes", key = "#req")
    public Page<Avaliacao> paginarAllAvaliacoesPaginadas(PageRequest req) {
        return avaliacaoRepository.findAll(req);
    }

    @Cacheable(value = "paginarAvaliacoesPorIdUsuario", key = "#req.pageNumber + '-' + #req.pageSize + '-' + #req.sort.toString() + '-' + #id")
    public Page<AvaliacaoDTO> paginarAvaliacoesPorIdUsuario(PageRequest req, Long id) {
        Page<Avaliacao> avaliacoes = avaliacaoRepository.findAvaliacoesByIdUsuario(id, req);
        return avaliacoes.map(AvaliacaoDTO::new);
    }

    @Cacheable(value = "paginarAvaliacoesPorIdAjuda", key = "#req.pageNumber + '-' + #req.pageSize + '-' + #req.sort.toString() + '-' + #id")
    public Page<AvaliacaoDTO> paginarAvaliacoesPorIdAjuda(PageRequest req, Long id) {
        Page<Avaliacao> avaliacoes = avaliacaoRepository.findAvaliacoesByIdAjuda(id, req);
        return avaliacoes.map(AvaliacaoDTO::new);
    }

    @CacheEvict(value = {"buscarTodasAvaliacoes", "buscarPaginaAvaliacoes", "paginarAvaliacoesPorIdUsuario", "paginarAvaliacoesPorIdAjuda"}, allEntries = true)
    public void limparCache() {
        System.out.println("Cache de avaliacoes limpo com sucesso.");
    }
}
