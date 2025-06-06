package br.com.solidarmap.solidar_api.controller;

import br.com.solidarmap.solidar_api.dto.AjudaDTO;
import br.com.solidarmap.solidar_api.dto.InserirAjudaRequestDTO;
import br.com.solidarmap.solidar_api.model.Ajuda;
import br.com.solidarmap.solidar_api.model.TipoRecurso;
import br.com.solidarmap.solidar_api.model.Usuario;
import br.com.solidarmap.solidar_api.repository.AjudaRepository;
import br.com.solidarmap.solidar_api.repository.TipoRecursoRepository;
import br.com.solidarmap.solidar_api.repository.UsuarioRepository;
import br.com.solidarmap.solidar_api.security.JWTUtil;
import br.com.solidarmap.solidar_api.service.AjudaCachingService;
import br.com.solidarmap.solidar_api.service.AjudaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
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
@RequestMapping(value = "/ajuda")
@Tag(name = "Ajuda", description = "Operações relacionadas às ajudas.")
public class AjudaController {

    @Autowired
    private AjudaRepository ajudaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TipoRecursoRepository tipoRecursoRepository;

    @Autowired
    private AjudaCachingService ajudaCachingService;

    @Autowired
    private AjudaService ajudaService;

    @Autowired
    private JWTUtil jwtUtil;

    @Operation(summary = "Listar todas as ajudas em cache")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de ajudas retornada com sucesso."),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Nenhuma ajuda encontrada", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/listar/cache/todos")
    public List<AjudaDTO> listarTodasAjudas() {
        List<AjudaDTO> ajudas = ajudaCachingService.listarTodasAjudas();
        if (ajudas.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhuma ajuda encontrada.");
        }
        return ajudas;
    }

    @Operation(summary = "Retorna as ajudas paginadas em cache")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de ajudas paginada retornada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Parâmetros de paginação inválidos. | Campo de ordenação inválido.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Nenhuma ajuda encontrada", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/paginar/cache/todos")
    public ResponseEntity<Page<AjudaDTO>> paginarTodasAjudas(
            @RequestParam(value = "pagina", defaultValue = "0") Integer page,
            @RequestParam(value = "tamanho", defaultValue = "2") Integer size,
            @RequestParam(value = "ordenacao", defaultValue = "id,asc") String sort) {

        List<String> camposPermitidos = List.of("id", "idUsuario", "idRecurso", "status", "dataPublicacao");
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
        Page<AjudaDTO> ajudaPage = ajudaService.paginarTodasAjudas(pr);

        if (ajudaPage.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhuma ajuda encontrada no cache.");
        }

        return ResponseEntity.ok(ajudaPage);
    }

    @Operation (summary = "Retorna as ajudas paginadas por ID do usuário em cache")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de ajudas paginadas por ID do usuário retornada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Parâmetros de paginação inválidos. | Campo de ordenação inválido.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Nenhuma ajuda encontrada no cache para o ID de usuário fornecido.", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/paginar/cache/{idUsuario}")
    public ResponseEntity<Page<AjudaDTO>> paginarAjudasPorIdUsuario(
            @PathVariable Long idUsuario,
            @RequestParam(value = "pagina", defaultValue = "0") Integer page,
            @RequestParam(value = "tamanho", defaultValue = "2") Integer size,
            @RequestParam(value = "ordenacao", defaultValue = "id,asc") String sort) {

        List<String> camposPermitidos = List.of("id", "idUsuario", "tipoRecurso", "status", "dataPublicacao", "idRecurso.id");
        String[] partes = sort.split(",");
        String campo = partes[0];
        Sort.Direction direcao = partes.length > 1 && partes[1].equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        if (!camposPermitidos.contains(campo)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Campo de ordenação inválido. Campos permitidos: " + camposPermitidos);
        }

        if (page < 0 || size <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parâmetros de paginação inválidos.");
        }

        PageRequest pr = PageRequest.of(page, size, Sort.by(direcao, campo));
        Page<AjudaDTO> ajudaPage = ajudaService.paginarAjudaPorIdUsuario(pr, idUsuario);

        if (ajudaPage.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhuma ajuda encontrada no cache para o ID de usuário fornecido.");
        }
        return ResponseEntity.ok(ajudaPage);
    }

    @Operation(summary = "Inserir uma nova ajuda")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Localização criada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para criação da localização.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado.", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/inserir")
    public Ajuda inserirAjuda(@RequestBody InserirAjudaRequestDTO inserirAjudaRequestDTO) {
        if (inserirAjudaRequestDTO.getIdUsuario() == null ||
                inserirAjudaRequestDTO.getIdRecurso() == null ||
                inserirAjudaRequestDTO.getDescricao() == null ||
                inserirAjudaRequestDTO.getDescricao().isEmpty() ||
                inserirAjudaRequestDTO.getDataPublicacao() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados inválidos para criação da ajuda.");
        }

        Optional<Usuario> usuarioOptional = usuarioRepository.findById(inserirAjudaRequestDTO.getIdUsuario());
        if (usuarioOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário não encontrado com o ID fornecido.");
        }

        Optional<TipoRecurso> tipoRecursoOptional = tipoRecursoRepository.findById(inserirAjudaRequestDTO.getIdRecurso());
        if (tipoRecursoOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de recurso não encontrado com o ID fornecido.");
        }

        Ajuda novaAjuda = new Ajuda();
        novaAjuda.setUsuario(usuarioOptional.get());
        novaAjuda.setRecurso(tipoRecursoOptional.get());
        novaAjuda.setDescricao(inserirAjudaRequestDTO.getDescricao());
        novaAjuda.setStatus(inserirAjudaRequestDTO.getStatus());
        novaAjuda.setDataPublicacao(inserirAjudaRequestDTO.getDataPublicacao());

        Ajuda ajudaSalva = ajudaRepository.save(novaAjuda);
        ajudaCachingService.limparCache();
        return ajudaSalva;
    }

    @Operation(summary = "Deletar uma ajuda")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Ajuda deletada com sucesso."),
            @ApiResponse(responseCode = "400", description = "ID inválido ou não pode ser nulo.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Ajuda não encontrada com o ID fornecido.", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/deletar/{id}")
    public Ajuda deletarAjuda(@PathVariable Long id, HttpServletRequest request) {
        Optional<Ajuda> op = ajudaRepository.findById(id);

        String authHeader = request.getHeader("Authorization");

        String token = authHeader.substring(7);
        Long usuarioId = jwtUtil.extrairUsuariobyId(token);

        if (id == null || id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID da localização não pode ser nulo ou menor que 1.");
        }

        if (op.isPresent()) {
            Ajuda ajuda = op.get();
            Usuario usuario = ajuda.getUsuario();
            if (usuario == null || !usuario.getId().equals(usuarioId)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuário autenticado não é o proprietário da ajuda.");
            } else  {
                ajudaRepository.delete(ajuda);
                ajudaCachingService.limparCache();
                return ajuda;
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Localização não encontrada com o ID informado.");
        }
    }


}
