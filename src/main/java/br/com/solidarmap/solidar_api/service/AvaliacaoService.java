package br.com.solidarmap.solidar_api.service;

import br.com.solidarmap.solidar_api.dto.AvaliacaoDTO;
import br.com.solidarmap.solidar_api.model.Avaliacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AvaliacaoService {

    @Autowired
    private AvaliacaoCachingService avaliacaoCachingService;

    @Transactional(readOnly = true)
    public Page<AvaliacaoDTO> paginarTodasAvaliacoes(PageRequest req){
        Page<Avaliacao> paginas_avaliacoes = avaliacaoCachingService.paginarAllAvaliacoesPaginadas(req);
        return paginas_avaliacoes.map(AvaliacaoDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<AvaliacaoDTO> paginarAvaliacoesPorIdUsuario(PageRequest req, Long idUsuario) {
        return avaliacaoCachingService.paginarAvaliacoesPorIdUsuario(req, idUsuario);
    }

    @Transactional(readOnly = true)
    public Page<AvaliacaoDTO> paginarAvaliacoesPorIdAjuda(PageRequest req, Long idAjuda) {
        return avaliacaoCachingService.paginarAvaliacoesPorIdAjuda(req, idAjuda);
    }
}
