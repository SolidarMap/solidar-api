package br.com.solidarmap.solidar_api.service;

import br.com.solidarmap.solidar_api.dto.LocalizacaoDTO;
import br.com.solidarmap.solidar_api.model.Localizacao;
import br.com.solidarmap.solidar_api.repository.LocalizacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocalizacaoCachingService {

    @Autowired
    private LocalizacaoRepository localizacaoRepository;

    @Cacheable(value = "buscarLocalizacaoPorId", key = "#id")
    public LocalizacaoDTO findLocalizacaoById(Long id) {
        return localizacaoRepository.findByIdLocalizacao(id);
    }

    @Cacheable(value = "buscarLocalizacaoPorIdModel", key = "#id")
    public Optional<Localizacao> findLocalizacaoByIdModel(Long id) {
        return localizacaoRepository.findById(id);
    }

    @Cacheable(value = "buscarLocalizacaoPorIdAjuda", key = "#id")
    public LocalizacaoDTO findLocalizacaoByAjudaId(Long id) {
        return localizacaoRepository.findLocalizacaoByAjudaId(id);
    }

    @CacheEvict(value = {"buscarLocalizacaoPorId", "buscarLocalizacaoPorIdAjuda", "buscarLocalizacaoPorIdModel"}, allEntries = true)
    public void limparCache() {
        System.out.println("Cache de localizações limpo com sucesso.");
    }
}
