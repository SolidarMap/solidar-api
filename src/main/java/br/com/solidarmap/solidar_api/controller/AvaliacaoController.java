package br.com.solidarmap.solidar_api.controller;

import br.com.solidarmap.solidar_api.dto.AvaliacaoDTO;
import br.com.solidarmap.solidar_api.dto.InserirAvaliacaoRequestDTO;
import br.com.solidarmap.solidar_api.model.Ajuda;
import br.com.solidarmap.solidar_api.model.Avaliacao;
import br.com.solidarmap.solidar_api.model.Usuario;
import br.com.solidarmap.solidar_api.repository.AjudaRepository;
import br.com.solidarmap.solidar_api.repository.AvaliacaoRepository;
import br.com.solidarmap.solidar_api.repository.UsuarioRepository;
import br.com.solidarmap.solidar_api.security.JWTUtil;
import br.com.solidarmap.solidar_api.service.AvaliacaoCachingService;
import br.com.solidarmap.solidar_api.service.AvaliacaoService;
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
@RequestMapping("/avaliacao")
@Tag(name = "Avaliação", description = "Operações relacionadas às avaliações.")
public class AvaliacaoController {

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AjudaRepository ajudaRepository;

    @Autowired
    private AvaliacaoService avaliacaoService;

    @Autowired
    private AvaliacaoCachingService avaliacaoCachingService;

    @Autowired
    private JWTUtil jwtUtil;

    @Operation(summary = "Listar todas as avaliações em cache")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de avaliações retornada com sucesso."),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Nenhuma avaliação encontrada", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/listar/cache/todos")
    public List<AvaliacaoDTO> listarTodasAvaliacoes() {
        List<AvaliacaoDTO> avaliacoes = avaliacaoCachingService.listarTodasAvaliacoes();
        if (avaliacoes.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhuma avaliação encontrada.");
        }
        return avaliacoes;
    }

    @Operation(summary = "Retorna as avaliações paginas em cache")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de avaliações paginada retornada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Parâmetros de paginação inválidos. | Campo de ordenação inválido.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Nenhuma avaliação encontrada", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/paginar/cache/todos")
    public ResponseEntity<Page<AvaliacaoDTO>> paginarTodasAvaliacoes(
            @RequestParam(value = "pagina", defaultValue = "0") Integer page,
            @RequestParam(value = "tamanho", defaultValue = "2") Integer size,
            @RequestParam(value = "ordenacao", defaultValue = "id,asc") String sort) {

        List<String> camposPermitidos = List.of("id", "idUsuario", "idAjuda", "nota", "dataAvaliacao");
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
        Page<AvaliacaoDTO> avaliacaoPage = avaliacaoService.paginarTodasAvaliacoes(pr);

        if (avaliacaoPage.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhuma avaliação encontrada no cache.");
        }
        return ResponseEntity.ok(avaliacaoPage);
    }

    @Operation(summary = "Retorna as avaliações paginadas por ID de usuário em cache")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de avaliações paginadas por ID do usuário retornada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Parâmetros de paginação inválidos. | Campo de ordenação inválido.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Nenhuma avaliação encontrada no cache para o ID de usuário fornecido.", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/paginar/cache/usuario/{idUsuario}")
    public ResponseEntity<Page<AvaliacaoDTO>> paginarAvaliacoesPorIdUsuario(
            @RequestParam(value = "pagina", defaultValue = "0") Integer page,
            @RequestParam(value = "tamanho", defaultValue = "2") Integer size,
            @RequestParam(value = "ordenacao", defaultValue = "id,asc") String sort,
            @PathVariable Long idUsuario) {

        List<String> camposPermitidos = List.of("id", "idUsuario", "idAjuda", "nota", "dataAvaliacao");
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
        Page<AvaliacaoDTO> avaliacaoPage = avaliacaoService.paginarAvaliacoesPorIdUsuario(pr, idUsuario);

        if (avaliacaoPage.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhuma avaliação encontrada no cache para o ID de usuário fornecido.");
        }
        return ResponseEntity.ok(avaliacaoPage);
    }

    @Operation(summary = "Retorna as avaliações paginadas por ID de ajuda em cache")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de avaliações paginadas por ID da ajuda retornada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Parâmetros de paginação inválidos. | Campo de ordenação inválido.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Nenhuma avaliação encontrada no cache para o ID da ajuda fornecido.", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/paginar/cache/ajuda/{idAjuda}")
    public ResponseEntity<Page<AvaliacaoDTO>> paginarAvaliacoesPorIdAjuda(
            @RequestParam(value = "pagina", defaultValue = "0") Integer page,
            @RequestParam(value = "tamanho", defaultValue = "2") Integer size,
            @RequestParam(value = "ordenacao", defaultValue = "id,asc") String sort,
            @PathVariable Long idAjuda) {

        List<String> camposPermitidos = List.of("id", "idUsuario", "idAjuda", "nota", "dataAvaliacao");
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
        Page<AvaliacaoDTO> avaliacaoPage = avaliacaoService.paginarAvaliacoesPorIdAjuda(pr, idAjuda);

        if (avaliacaoPage.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhuma avaliação encontrada no cache para o ID da ajuda fornecido.");
        }
        return ResponseEntity.ok(avaliacaoPage);
    }

    @Operation(summary = "Insere uma nova avaliação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Avaliação criada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para criação da avaliação.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado.", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/inserir")
    public Avaliacao inserirAvaliacao(@RequestBody InserirAvaliacaoRequestDTO inserirAvaliacaoRequestDTO) {
        if (inserirAvaliacaoRequestDTO.getIdUsuario() == null ||
                inserirAvaliacaoRequestDTO.getIdAjuda() == null ||
                inserirAvaliacaoRequestDTO.getNota() == null ||
                inserirAvaliacaoRequestDTO.getComentario() == null ||
                inserirAvaliacaoRequestDTO.getDataAvaliacao() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados inválidos para criação da avaliação.");
        }

        Optional<Usuario> usuarioOptional = usuarioRepository.findById(inserirAvaliacaoRequestDTO.getIdUsuario());
        if (usuarioOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado com o ID fornecido.");
        }

        Optional<Ajuda> ajudaOptional = ajudaRepository.findById(inserirAvaliacaoRequestDTO.getIdAjuda());
        if (ajudaOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ajuda não encontrada com o ID fornecido.");
        }

        Avaliacao novaAvaliacao = new Avaliacao();
        novaAvaliacao.setUsuario(usuarioOptional.get());
        novaAvaliacao.setAjuda(ajudaOptional.get());
        novaAvaliacao.setNota(inserirAvaliacaoRequestDTO.getNota());
        novaAvaliacao.setComentario(inserirAvaliacaoRequestDTO.getComentario());
        novaAvaliacao.setDataAvaliacao(inserirAvaliacaoRequestDTO.getDataAvaliacao());

        Avaliacao avaliacaoSalva = avaliacaoRepository.save(novaAvaliacao);
        avaliacaoCachingService.limparCache();
        return avaliacaoSalva;
    }

    @Operation(summary = "Deletar uma avaliação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Ajuda deletada com sucesso."),
            @ApiResponse(responseCode = "400", description = "ID inválido ou não pode ser nulo.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Ajuda não encontrada com o ID fornecido.", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/deletar/{id}")
    public Avaliacao deletarAvaliacao(@PathVariable Long id, HttpServletRequest request) {
        Optional<Avaliacao> op = avaliacaoRepository.findById(id);

        String authHeader = request.getHeader("Authorization");

        String token = authHeader.substring(7);
        Long usuarioId = jwtUtil.extrairUsuariobyId(token);

        if (id == null || id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID da localização não pode ser nulo ou menor que 1.");
        }

        if (op.isPresent()) {
            Avaliacao avaliacao = op.get();
            Usuario usuario = avaliacao.getUsuario();
            if (usuario == null || !usuario.getId().equals(usuarioId)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuário autenticado não é o proprietário da avaliação.");
            } else {
                avaliacaoRepository.delete(avaliacao);
                avaliacaoCachingService.limparCache();
                return avaliacao;
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Avaliação não encontrada com o ID fornecido.");
        }
    }
}
