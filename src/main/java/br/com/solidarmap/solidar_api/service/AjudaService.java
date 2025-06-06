package br.com.solidarmap.solidar_api.service;

import br.com.solidarmap.solidar_api.dto.AjudaDTO;
import br.com.solidarmap.solidar_api.model.Ajuda;
import br.com.solidarmap.solidar_api.repository.AjudaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AjudaService {

    @Autowired
    private AjudaRepository ajudaRepository;

    @Autowired
    private AjudaCachingService ajudaCachingService;

    @Transactional(readOnly = true)
    public Page<AjudaDTO> paginarTodasAjudas(PageRequest req) {
        Page<Ajuda> paginas_ajudas = ajudaCachingService.paginarAllAjudasPaginadas(req);
        return paginas_ajudas.map(AjudaDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<AjudaDTO> paginarAjudaPorIdUsuario(PageRequest req, Long idUsuario) {
        return ajudaCachingService.paginarAjudasPorIdUsuario(req, idUsuario);
    }
}
