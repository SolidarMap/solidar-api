package br.com.solidarmap.solidar_api.service;

import br.com.solidarmap.solidar_api.dto.TipoZonaDTO;
import br.com.solidarmap.solidar_api.repository.TipoZonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TipoZonaService {

    @Autowired
    private TipoZonaRepository tipoZonaRepository;

    @Autowired
    private TipoZonaCachingService tipoZonaCachingService;

    @Transactional(readOnly = true)
    public Page<TipoZonaDTO> paginarTodosOsTiposDeZona(PageRequest req) {
        Page<TipoZonaDTO> paginas_tipo_zona_dto = tipoZonaCachingService.findAll(req)
                .map(TipoZonaDTO::new);
        return paginas_tipo_zona_dto;
    }
}
