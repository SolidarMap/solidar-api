package br.com.solidarmap.solidar_api.service;

import br.com.solidarmap.solidar_api.dto.MensagemDTO;
import br.com.solidarmap.solidar_api.model.Mensagem;
import br.com.solidarmap.solidar_api.repository.MensagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MensagemService {

    @Autowired
    private MensagemRepository mensagemRepository;

    @Autowired
    private MensagemCachingService mensagemCachingService;

    @Transactional(readOnly = true)
    public Page<MensagemDTO> paginarTodasAsMensagens(PageRequest req) {
        Page<Mensagem> paginas_mensagens = mensagemCachingService.paginarAllMensagensPaginadas(req);
        return paginas_mensagens.map(MensagemDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<MensagemDTO> paginarMensagensPorIdAjuda(PageRequest req, Long idAjuda) {
        return mensagemCachingService.paginarMensagensPorIdAjuda(req, idAjuda);
    }

    @Transactional(readOnly = true)
    public Page<MensagemDTO> paginarMensagensPorAjudaEUsuario(PageRequest req, Long idAjuda, Long idUsuario) {
        return mensagemCachingService.paginarMensagensPorAjudaEUsuario(req, idAjuda, idUsuario);
    }

}
