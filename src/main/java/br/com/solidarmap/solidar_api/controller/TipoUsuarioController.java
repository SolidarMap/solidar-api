package br.com.solidarmap.solidar_api.controller;

import br.com.solidarmap.solidar_api.dto.InserirTipoUsuarioRequestDTO;
import br.com.solidarmap.solidar_api.dto.TipoUsuarioDTO;
import br.com.solidarmap.solidar_api.dto.TipoUsuarioRequestDTO;
import br.com.solidarmap.solidar_api.model.TipoUsuario;
import br.com.solidarmap.solidar_api.repository.TipoUsuarioRepository;
import br.com.solidarmap.solidar_api.service.TipoUsuarioCachingService;
import br.com.solidarmap.solidar_api.service.TipoUsuarioService;
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

@RestController()
@RequestMapping(value = "/tipoUsuario")
@Tag(name = "Tipo de Usuário", description = "Operações relacionadas aos tipos de usuários.")
public class TipoUsuarioController {

    @Autowired
    private TipoUsuarioRepository tipoUsuarioRepository;

    @Autowired
    private TipoUsuarioCachingService tipoUsuarioCachingService;

    @Autowired
    private TipoUsuarioService tipoUsuarioService;

    @Operation(summary = "Listar todos os tipos de usuários em cacher")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de tipos de usuários retornada com sucesso."),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Nenhum tipo de usuário encontrado no cache.", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/listar/cache/todos")
    public List<TipoUsuario> findAllTiposUsuarioEmCache() {
        List<TipoUsuario> tiposUsuario = tipoUsuarioCachingService.findAllTiposUsuario();
        if (tiposUsuario.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhum tipo de usuário encontrado no cache.");
        }
        return tiposUsuario;
    }

    @Operation(summary = "Retorna os tipos de usuários paginados em cache")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de tipos de usuários paginada retornada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Parâmetros de paginação inválidos. | Campo de ordenação inválido.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Nenhum tipo de usuário encontrado no cache.", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/paginar/cache/todos")
    public ResponseEntity<Page<TipoUsuarioDTO>> paginarTiposUsuarioCache(
            @RequestParam(value = "pagina", defaultValue = "0") Integer page,
            @RequestParam(value = "tamanho", defaultValue = "10") Integer size,
            @RequestParam(value = "ordenacao", defaultValue = "id,asc") String sort) {

        List<String> camposPermitidos = List.of("id", "nomeTipo");
        String[] partes = sort.split(",");
        String campo = partes[0];
        Sort.Direction direcao = partes.length > 1 && partes[1].equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        if (!camposPermitidos.contains(campo)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Campo de ordenação inválido. Campos permitidos: " + camposPermitidos);
        }

        if (page < 0 || size <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parâmetros de paginação inválidos. A página deve ser >= 0 e o tamanho > 0.");
        }

        PageRequest pr = PageRequest.of(page, size, Sort.by(direcao, campo));
        Page<TipoUsuarioDTO> paginas_tipos_usuarios_dto = tipoUsuarioService.paginarTodosOsTiposDeUsuario(pr);

        if (paginas_tipos_usuarios_dto.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhum tipo de usuário encontrado no cache.");
        }

        return ResponseEntity.ok(paginas_tipos_usuarios_dto);
    }

    @Operation(summary = "Buscar tipo de usuário por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tipo de usuário encontrado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Tipo de usuário não encontrado.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado.", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/buscar/id/{id}")
    public TipoUsuario retornaTipoUsuarioPorId(@PathVariable Long id) {
        if (id == null || id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID do usuário não pode ser nulo ou menor que 1.");
        }

        Optional <TipoUsuario> tipoUsuario = tipoUsuarioCachingService.findTipoUsuarioById(id);
        if (tipoUsuario.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tipo de usuário não encontrado.");
        }
        return tipoUsuario.get();
    }

    @Operation(summary = "Inserir novo tipo de usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tipo de usuário inserido com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para inserção.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado.", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/inserir")
    public TipoUsuario inserirTipoUsuario(@RequestBody @Valid InserirTipoUsuarioRequestDTO tipoUsuario) {
        if (tipoUsuario == null || tipoUsuario.getNomeTipo() == null || tipoUsuario.getNomeTipo().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome do Tipo de Usuário é obrigatório.");
        }

        if (tipoUsuarioRepository.existsByNomeTipo(tipoUsuario.getNomeTipo())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Tipo de usuário já existe.");
        }

        TipoUsuario novoTipoUsuario = new TipoUsuario();
        novoTipoUsuario.setNomeTipo(tipoUsuario.getNomeTipo());
        return tipoUsuarioRepository.save(novoTipoUsuario);
    }

    @Operation(summary = "Atualizar tipo de usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tipo de usuário atualizado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para atualização.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "409", description = "Nome do Tipo de Usuário já existe.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "ID do Tipo de usuário não encontrado.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado.", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping("/atualizar/{id}")
    public TipoUsuario atualizarTipoUsuario(@RequestBody TipoUsuarioRequestDTO tipoUsuarioRequestDTO, @PathVariable Long id) {
        Optional<TipoUsuario> op = tipoUsuarioRepository.findById(id);

        if(op.isPresent()) {
            TipoUsuario tipoUsuarioAtual = op.get();

            if (tipoUsuarioRepository.existsByNomeTipo(tipoUsuarioRequestDTO.getNomeTipo())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Tipo de usuário já existe.");
            }

            tipoUsuarioAtual.setNomeTipo(tipoUsuarioRequestDTO.getNomeTipo());

            tipoUsuarioRepository.save(tipoUsuarioAtual);
            return tipoUsuarioAtual;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tipo de Usuário não encontrado com o ID: " + id);
        }
    }

    @Operation(summary = "Excluir tipo de usuário")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tipo de usuário excluído com sucesso."),
            @ApiResponse(responseCode = "404", description = "Tipo de usuário não encontrado.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado.", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/excluir/{id}")
    public TipoUsuario removerTipoUsuario(@PathVariable Long id) {
        Optional<TipoUsuario> op = tipoUsuarioRepository.findById(id);

        if(op.isPresent()) {
            TipoUsuario tipoUsuario = op.get();
            tipoUsuarioRepository.delete(tipoUsuario);
            return tipoUsuario;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tipo de Usuário não encontrado com o ID: " + id);
        }
    }
}
