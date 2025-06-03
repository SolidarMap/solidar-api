package br.com.solidarmap.solidar_api.controller;

import br.com.solidarmap.solidar_api.dto.InserirTipoRecursoRequestDTO;
import br.com.solidarmap.solidar_api.dto.TipoRecursoDTO;
import br.com.solidarmap.solidar_api.dto.TipoRecursoRequestDTO;
import br.com.solidarmap.solidar_api.model.TipoRecurso;
import br.com.solidarmap.solidar_api.repository.TipoRecursoRepository;
import br.com.solidarmap.solidar_api.repository.TipoUsuarioRepository;
import br.com.solidarmap.solidar_api.service.TipoRecursoCachingService;
import br.com.solidarmap.solidar_api.service.TipoRecursoService;
import br.com.solidarmap.solidar_api.service.TipoUsuarioCachingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/tipoRecurso")
@Tag(name = "Tipo de Recurso", description = "Operações relacionadas aos tipos de recursos.")
public class TipoRecursoController {

    @Autowired
    private TipoRecursoRepository tipoRecursoRepository;

    @Autowired
    private TipoRecursoService tipoRecursoService;

    @Autowired
    private TipoRecursoCachingService tipoRecursoCachingService;

    @Operation(summary = "Listar todos os tipos de recurso em cache")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de tipos de recurso retornada com sucesso."),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Nenhum tipo de recurso encontrado no cache.", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/listar/cache/todos")
    public List<TipoRecurso> listarTodosTiposRecursoEmCache() {
        List<TipoRecurso> tiposRecurso = tipoRecursoCachingService.findAllTiposRecurso();
        if (tiposRecurso.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhum tipo de recurso encontrado no cache.");
        }
        return tiposRecurso;
    }

    @Operation(summary = "Retorna os tipos de recursos paginados em cache")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de tipos de recurso paginada retornada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Parâmetros de paginação inválidos. | Campo de ordenação inválido.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Nenhum tipo de recurso encontrado no cache.", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/paginar/cache/todos")
    public ResponseEntity<Page<TipoRecursoDTO>> paginarTiposRecursoCache(
            @RequestParam(value = "pagina", defaultValue = "0") Integer page,
            @RequestParam(value = "tamanho", defaultValue = "2") Integer size,
            @RequestParam(value = "ordenacao", defaultValue = "id,asc") String sort) {

        List<String> camposPermitidos = List.of("id", "recurso");
        String[] partes = sort.split(",");
        String campo = partes[0];
        Sort.Direction direcao = partes.length > 1 && partes[1].equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        if (!camposPermitidos.contains(campo)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Campo de ordenação inválido. Campos permitidos: " + camposPermitidos);
        }

        if (page < 0 || size <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parâmetros de paginação inválidos. A página deve ser maior ou igual a 0 e o tamanho maior que 0.");
        }

        PageRequest pr = PageRequest.of(page, size, Sort.by(direcao, campo));
        Page<TipoRecursoDTO> tiposRecursoPage = tipoRecursoService.paginarTodosOsTiposDeRecurso(pr);

        if (tiposRecursoPage.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhum tipo de recurso encontrado no cache.");
        }
        return ResponseEntity.ok(tiposRecursoPage);
    }

    @Operation(summary = "Buscar tipo de recurso por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tipo de recurso encontrado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Tipo de recurso não encontrado.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado.", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/buscar/id/{id}")
    public TipoRecurso retornaTipoRecursoPorId(@PathVariable Long id) {
        if (id == null || id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID do tipo de recurso não pode ser nulo ou menor que 1.");
        }

        Optional<TipoRecurso> tipoRecurso = tipoRecursoRepository.findById(id);
        if (tipoRecurso.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tipo de recurso não encontrado com o ID: " + id);
        }
        return tipoRecurso.get();
    }

    @Operation(summary = "Inserir um novo tipo de recurso")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tipo de recurso criado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para criação do tipo de recurso.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado.", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/inserir")
    public TipoRecurso inserirTipoRecurso(@RequestBody InserirTipoRecursoRequestDTO tipoRecurso) {
        if (tipoRecurso == null || tipoRecurso.getRecurso() == null || tipoRecurso.getRecurso().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados inválidos para criação do tipo de recurso.");
        }

        if (tipoRecursoRepository.existsByRecurso(tipoRecurso.getRecurso())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Tipo de recurso já existe.");
        }

        TipoRecurso novoTipoRecurso = new TipoRecurso();
        novoTipoRecurso.setRecurso(tipoRecurso.getRecurso());

        TipoRecurso recursoSalvo = tipoRecursoRepository.save(novoTipoRecurso);
        tipoRecursoCachingService.limparCache();
        return recursoSalvo;
    }

    @Operation(summary = "Atualizar um tipo de recurso")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tipo de recurso atualizado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para atualização do tipo de recurso.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "409", description = "Tipo de recurso já existe.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "ID do Tipo de Recurso não encontrado.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado.", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping("/atualizar/{id}")
    public TipoRecurso atualizarTipoRecurso(@RequestBody TipoRecursoRequestDTO tipoRecursoRequestDTO, @PathVariable Long id) {
        Optional<TipoRecurso> op = tipoRecursoRepository.findById(id);

        if(op.isPresent()) {
            TipoRecurso tipoRecursoAtual = op.get();

            if(tipoRecursoRepository.existsByRecurso(tipoRecursoRequestDTO.getRecurso())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Tipo de recurso já existe.");
            }

            tipoRecursoAtual.setRecurso(tipoRecursoRequestDTO.getRecurso());

            tipoRecursoRepository.save(tipoRecursoAtual);
            tipoRecursoCachingService.limparCache();
            return tipoRecursoAtual;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tipo de Recurso não encontrado com o ID: " + id);
        }
    }

    @Operation(summary = "Excluir um tipo de recurso")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tipo de recurso excluído com sucesso."),
            @ApiResponse(responseCode = "404", description = "Tipo de recurso não encontrado.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado.", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/excluir/{id}")
    public TipoRecurso removerTipoRecurso(@PathVariable Long id) {
        Optional<TipoRecurso> op = tipoRecursoRepository.findById(id);

        if (op.isPresent()) {
            TipoRecurso tipoRecurso = op.get();
            tipoRecursoRepository.delete(tipoRecurso);
            tipoRecursoCachingService.limparCache();
            return tipoRecurso;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tipo de recurso não encontrado com o ID: " + id);
        }
    }

}
