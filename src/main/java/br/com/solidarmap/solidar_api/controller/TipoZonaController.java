package br.com.solidarmap.solidar_api.controller;

import br.com.solidarmap.solidar_api.dto.InserirTipoZonaRequestDTO;
import br.com.solidarmap.solidar_api.dto.TipoZonaDTO;
import br.com.solidarmap.solidar_api.dto.TipoZonaRequestDTO;
import br.com.solidarmap.solidar_api.model.TipoZona;
import br.com.solidarmap.solidar_api.repository.TipoRecursoRepository;
import br.com.solidarmap.solidar_api.repository.TipoZonaRepository;
import br.com.solidarmap.solidar_api.service.TipoRecursoService;
import br.com.solidarmap.solidar_api.service.TipoZonaCachingService;
import br.com.solidarmap.solidar_api.service.TipoZonaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping(value = "/tipoZona")
@Tag(name = "Tipo de Zona", description = "Operações relacionadas aos tipos de zonas.")
public class TipoZonaController {

    @Autowired
    private TipoZonaRepository tipoZonaRepository;

    @Autowired
    private TipoZonaService tipoZonaService;

    @Autowired
    private TipoZonaCachingService tipoZonaCachingService;


    @Operation(summary = "Listar todos os tipos de zona em cache")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de tipos de zona retornada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Nenhum tipo de zona encontrado no cache", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/listar/cache/todos")
    public List<TipoZona> listarTodosTiposZonaEmCache() {
         List<TipoZona> tiposZona = tipoZonaCachingService.findAllTiposZona();
        if (tiposZona.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhum tipo de zona encontrado no cache.");
        }
        return tiposZona;
    }

    @Operation(summary = "Retorna os tipos de zonas paginados em cache")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de tipos de zonas paginada retornada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Parâmetros de paginação inválidos. | Campo de ordenação inválido.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Nenhum tipo de zonas encontrado no cache.", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/paginar/cache/todos")
    public ResponseEntity<Page<TipoZonaDTO>> paginarTiposZonaCache(
            @RequestParam(value = "pagina", defaultValue = "0") Integer page,
            @RequestParam(value = "tamanho", defaultValue = "2") Integer size,
            @RequestParam(value = "ordenacao", defaultValue = "id,asc") String sort) {

        List<String> camposPermitidos = List.of("id", "zona");
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
        Page<TipoZonaDTO> tipoZonaPage = tipoZonaService.paginarTodosOsTiposDeZona(pr);

        if (tipoZonaPage.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhum tipo de zona encontrado no cache.");
        }
        return ResponseEntity.ok(tipoZonaPage);
    }

    @Operation(summary = "Buscar tipo de zona por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tipo de zona encontrado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Tipo de zona não encontrado.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado.", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/buscar/cache/{id}")
    public TipoZona retornaTipoZonaPorId(@PathVariable Long id) {
        if (id == null || id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID do tipo de zona não pode ser nulo ou menor que 1.");
        }

        Optional<TipoZona> tipoZona = tipoZonaRepository.findById(id);
        if (tipoZona.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tipo de zona não encontrado com o ID: " + id);
        }
        return tipoZona.get();
    }

    @Operation(summary = "Inserir novo tipo de zona")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tipo de zo na criado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para criação do tipo de zona.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado.", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/inserir")
    public TipoZona inserirTipoZona(@RequestBody InserirTipoZonaRequestDTO tipoZona) {
        if (tipoZona == null || tipoZona.getZona() == null || tipoZona.getZona().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados inválidos para criação do tipo de zona.");
        }

        if (tipoZonaRepository.existsByZona(tipoZona.getZona())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de zona já existe.");
        }

        TipoZona novoTipoZona = new TipoZona();
        novoTipoZona.setZona(tipoZona.getZona());

        TipoZona zonaSalva = tipoZonaRepository.save(novoTipoZona);
        tipoZonaCachingService.limparCache();
        return zonaSalva;
    }

    @Operation(summary = "Atualizar tipo de zona")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tipo de zona atualizado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para atualização do tipo de zona.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "409", description = "Tipo de Zona já existe.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "ID do Tipo de Zona não encontrado.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado.", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping("/atualizar/{id}")
    public TipoZona atualizarTipoZona(@RequestBody TipoZonaRequestDTO tipoZonaRequestDTO, @PathVariable long id) {
        Optional<TipoZona> op = tipoZonaRepository.findById(id);

        if (op.isPresent()) {
            TipoZona tipoZonaAtual = op.get();

            if (tipoZonaRepository.existsByZona(tipoZonaRequestDTO.getZona())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Tipo de zona já existe.");
            }

            tipoZonaAtual.setZona(tipoZonaRequestDTO.getZona());

            tipoZonaRepository.save(tipoZonaAtual);
            tipoZonaCachingService.limparCache();
            return tipoZonaAtual;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tipo de Zona não encontrado com o ID: " + id);
        }
    }

    @Operation(summary = "Deletar tipo de zona")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tipo de zona excluído com sucesso."),
            @ApiResponse(responseCode = "404", description = "Tipo de zona não encontrado.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado.", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/excluir/{id}")
    public TipoZona removerTipoZona(@PathVariable long id) {
        Optional<TipoZona> op = tipoZonaRepository.findById(id);

        if (op.isPresent()) {
            TipoZona tipoZona = op.get();
            tipoZonaRepository.delete(tipoZona);
            tipoZonaCachingService.limparCache();
            return tipoZona;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tipo de zona não encontrado com o ID: " + id);
        }
    }
}
