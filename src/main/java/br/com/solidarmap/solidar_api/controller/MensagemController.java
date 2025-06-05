package br.com.solidarmap.solidar_api.controller;

import br.com.solidarmap.solidar_api.dto.InserirMensagemRequestDTO;
import br.com.solidarmap.solidar_api.dto.MensagemDTO;
import br.com.solidarmap.solidar_api.model.Ajuda;
import br.com.solidarmap.solidar_api.model.Mensagem;
import br.com.solidarmap.solidar_api.model.Usuario;
import br.com.solidarmap.solidar_api.repository.AjudaRepository;
import br.com.solidarmap.solidar_api.repository.MensagemRepository;
import br.com.solidarmap.solidar_api.repository.UsuarioRepository;
import br.com.solidarmap.solidar_api.service.MensagemCachingService;
import br.com.solidarmap.solidar_api.service.MensagemService;
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
@RequestMapping(value = "/mensagem")
@Tag(name = "Mensagem", description = "Operações relacionadas as mensagens.")
public class MensagemController {

    @Autowired
    private MensagemRepository mensagemRepository;

    @Autowired
    private AjudaRepository ajudaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MensagemCachingService mensagemCachingService;

    @Autowired
    private MensagemService mensagemService;

    @Operation(summary = "Listar todas as mensagens em cache")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de mensagens retornada com sucesso."),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Nenhuma mensagem encontrada no cache.", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/listar/cache/todos")
    public List<MensagemDTO> listarTodasMensagens() {
        List<MensagemDTO> mensagens = mensagemCachingService.buscarTodasMensagens();
        if (mensagens.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhuma mensagem encontrada no cache.");
        }
        return mensagens;
    }

    @Operation(summary = "Retorna as mensagens paginados em cache")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de tipos de recurso paginada retornada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Parâmetros de paginação inválidos. | Campo de ordenação inválido.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Nenhum tipo de recurso encontrado no cache.", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/paginar/cache/todos")
    public ResponseEntity<Page<MensagemDTO>> paginarTodasMensagens(
            @RequestParam(value = "pagina", defaultValue = "0") Integer page,
            @RequestParam(value = "tamanho", defaultValue = "2") Integer size,
            @RequestParam(value = "ordenacao", defaultValue = "id,asc") String sort) {

        List<String> camposPermitidos = List.of("id", "idAjuda", "idUsuario", "mensagem", "dataEnvio");
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
        Page<MensagemDTO> mensagemPage = mensagemService.paginarTodasAsMensagens(pr);

        if (mensagemPage.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhuma mensagem encontrada no cache.");
        }
        return ResponseEntity.ok(mensagemPage);
    }

    @Operation(summary = "Retorna as mensagens paginadas por ID de ajuda em cache")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de mensagens paginada por ID de ajuda retornada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Parâmetros de paginação inválidos. | Campo de ordenação inválido.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Nenhuma mensagem encontrada no cache para o ID de ajuda fornecido.", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/paginar/cache/{idAjuda}")
    public ResponseEntity<Page<MensagemDTO>> paginarMensagensPorIdAjuda(
            @PathVariable Long idAjuda,
            @RequestParam(value = "pagina", defaultValue = "0") Integer page,
            @RequestParam(value = "tamanho", defaultValue = "2") Integer size,
            @RequestParam(value = "ordenacao", defaultValue = "id,asc") String sort) {

        List<String> camposPermitidos = List.of("id", "idAjuda", "idUsuario", "mensagem", "dataEnvio");
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
        Page<MensagemDTO> mensagemPage = mensagemService.paginarMensagensPorIdAjuda(pr, idAjuda);

        if (mensagemPage.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhuma mensagem encontrada no cache para o ID de ajuda fornecido.");
        }
        return ResponseEntity.ok(mensagemPage);
    }

    @Operation(summary = "Retorna as mensagens paginadas por ID de ajuda e ID de usuário em cache")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de mensagens paginada por ID de ajuda e ID de usuário retornada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Parâmetros de paginação inválidos. | Campo de ordenação inválido.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Nenhuma mensagem encontrada no cache para ID do Usuário fornecido.", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/paginar/cache/{idAjuda}/{idUsuario}")
    public ResponseEntity<Page<MensagemDTO>> paginarMensagensPorAjudaEUsuario(
            @PathVariable Long idAjuda,
            @PathVariable Long idUsuario,
            @RequestParam(value = "pagina", defaultValue = "0") Integer page,
            @RequestParam(value = "tamanho", defaultValue = "2") Integer size,
            @RequestParam(value = "ordenacao", defaultValue = "id,asc") String sort) {

        List<String> camposPermitidos = List.of("id", "idAjuda", "idUsuario", "mensagem", "dataEnvio"); // campos que podem ordenar
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
        Page<MensagemDTO> mensagens = mensagemService.paginarMensagensPorAjudaEUsuario(pr, idAjuda, idUsuario);

        if (mensagens.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhuma mensagem encontrada para os filtros fornecidos.");
        }
        return ResponseEntity.ok(mensagens);
    }

    @Operation(summary = "Inserir uma nova mensagem")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Localização criada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para criação da localização.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado.", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/inserir")
    public Mensagem inserirMensagem(@RequestBody InserirMensagemRequestDTO inserirMensagemRequestDTO) {
        if (inserirMensagemRequestDTO.getIdAjuda() == null ||
            inserirMensagemRequestDTO.getIdUsuario() == null ||
            inserirMensagemRequestDTO.getMensagem() == null ||
            inserirMensagemRequestDTO.getDataEnvio() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados inválidos para criação da mensagem.");
        }

        Optional<Ajuda> ajudaOptional = ajudaRepository.findById(inserirMensagemRequestDTO.getIdAjuda());
        if (ajudaOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ajuda não encontrada com o ID fornecido.");
        }

        Optional<Usuario> usuarioOptional = usuarioRepository.findById(inserirMensagemRequestDTO.getIdUsuario());
        if (usuarioOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado com o ID fornecido.");
        }

        Mensagem novaMensagem = new Mensagem();
        novaMensagem.setAjuda(ajudaOptional.get());
        novaMensagem.setUsuario(usuarioOptional.get());
        novaMensagem.setMensagem(inserirMensagemRequestDTO.getMensagem());
        novaMensagem.setDataEnvio(inserirMensagemRequestDTO.getDataEnvio());

        Mensagem mensagemSalva = mensagemRepository.save(novaMensagem);
        mensagemCachingService.limparCache();
        return mensagemSalva;
    }

    @Operation(summary = "Deletar uma mensagem")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Mensagem deletada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Mensagem não encontrada com o ID informado.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado.", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/deletar/{id}")
    public Mensagem removerMensagem(@PathVariable Long id) {
        Optional<Mensagem> op = mensagemRepository.findById(id);

        if (op.isPresent()) {
            Mensagem mensagem = op.get();
            mensagemRepository.delete(mensagem);
            mensagemCachingService.limparCache();
            return mensagem;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Mensagem não encontrada com o ID informado.");
        }
    }
}
