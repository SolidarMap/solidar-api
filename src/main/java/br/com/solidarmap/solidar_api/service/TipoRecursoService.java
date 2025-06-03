package br.com.solidarmap.solidar_api.service;

import br.com.solidarmap.solidar_api.dto.TipoRecursoDTO;
import br.com.solidarmap.solidar_api.model.TipoRecurso;
import br.com.solidarmap.solidar_api.repository.TipoRecursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TipoRecursoService {

    @Autowired
    private TipoRecursoRepository tipoRecursoRepository;

    @Autowired
    private TipoRecursoCachingService tipoRecursoCachingService;

    @Transactional(readOnly = true)
    public Page<TipoRecursoDTO> paginarTodosOsTiposDeRecurso(PageRequest req) {
        Page<TipoRecurso> paginas_tipo_recurso = tipoRecursoCachingService.findAll(req);
        Page<TipoRecursoDTO> paginar_tipos_recursos_dto = paginas_tipo_recurso.map(TipoRecursoDTO::new);
        return paginar_tipos_recursos_dto;
    }
}
