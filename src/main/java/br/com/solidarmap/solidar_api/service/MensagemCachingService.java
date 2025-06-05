package br.com.solidarmap.solidar_api.service;

import br.com.solidarmap.solidar_api.dto.MensagemDTO;
import br.com.solidarmap.solidar_api.model.Mensagem;
import br.com.solidarmap.solidar_api.repository.MensagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MensagemCachingService {

    @Autowired
    private MensagemRepository mensagemRepository;

    // Precisa passar o req e idAjuda
    @Cacheable(value = "paginarMensagensPorIdAjuda", key = "#req.pageNumber + '-' + #req.pageSize + '-' + #req.sort.toString() + '-' + #id")
    public Page<MensagemDTO> paginarMensagensPorIdAjuda(PageRequest req, Long id) {
        Page<Mensagem> mensagens = mensagemRepository.findMensagensByIdAjuda(id, req);
        return mensagens.map(MensagemDTO::new);
    }

    @Cacheable(value = "paginarMensagensPorAjudaEUsuario", key = "#idAjuda + '-' + #idUsuario + '-' + #req.pageNumber + '-' + #req.pageSize + '-' + #req.sort.toString()")
    public Page<MensagemDTO> paginarMensagensPorAjudaEUsuario(PageRequest req, Long idAjuda, Long idUsuario) {
        Page<Mensagem> mensagens = mensagemRepository.findByAjudaIdAndUsuarioId(idAjuda, idUsuario, req);
        return mensagens.map(MensagemDTO::new);
    }

    @Cacheable(value = "buscarTodasMensagens")
    public List<MensagemDTO> buscarTodasMensagens() {
        return mensagemRepository.findAllMensagens();
    }

    @Cacheable(value = "buscarPaginaMensagens", key = "#req")
    public Page<Mensagem> paginarAllMensagensPaginadas(PageRequest req) {
        return mensagemRepository.findAll(req);
    }

    @CacheEvict(value = { "buscarTodasMensagens", "buscarPaginaMensagens", "paginarMensagensPorIdAjuda", "paginarMensagensPorAjudaEUsuario"} , allEntries = true)
    public void limparCache() {
        System.out.println("Cache de mensagens limpo com sucesso.");
    }
}
