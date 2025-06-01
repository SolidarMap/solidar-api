package br.com.solidarmap.solidar_api.controller;

import br.com.solidarmap.solidar_api.dto.InserirUsuarioRequestDTO;
import br.com.solidarmap.solidar_api.dto.UsuarioDTO;
import br.com.solidarmap.solidar_api.model.TipoUsuario;
import br.com.solidarmap.solidar_api.model.Usuario;
import br.com.solidarmap.solidar_api.repository.TipoUsuarioRepository;
import br.com.solidarmap.solidar_api.repository.UsuarioRepository;
import br.com.solidarmap.solidar_api.service.UsuarioCachingService;
import br.com.solidarmap.solidar_api.service.UsuarioService;
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
@RequestMapping(value = "/usuario")
@Tag(name = "Usuário", description = "Operações relacionadas aos usuários.")
public class UsuarioController {

	@Autowired
	private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioCachingService usuarioCachingService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private TipoUsuarioRepository tipoUsuarioRepository;

    @Operation(summary = "Listar todos os usuários")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Nenhum usuário encontrado.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado.", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/todos")
    public List<UsuarioDTO> retornaTodosUsuarios() {
        List<UsuarioDTO> usuarios = usuarioRepository.findAllUsuarios();
        if (usuarios.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhum usuário encontrado.");
        }
        return usuarios;
    }

    @Operation(summary = "Buscar usuário por email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = "Email não pode ser nulo ou vazio.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado.", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/buscarPorEmail")
    public UsuarioDTO retornaUsuarioPorEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O email não pode ser nulo ou vazio.");
        }
        UsuarioDTO usuario = usuarioRepository.findByEmail(email);
        if (usuario == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado com o email: " + email);
        }
        return usuario;
    }

    @Operation(summary = "Buscar usuário por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = "ID do usuário não pode ser nulo ou menor que 1.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado.", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/buscarPorId")
    public UsuarioDTO retornaUsuarioPorId(Long id) {
        if (id == null || id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID do usuário não pode ser nulo ou menor que 1.");
        }
        UsuarioDTO usuario = usuarioRepository.findUsuarioById(id);
        if (usuario == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado com o ID: " + id);
        }
        return usuario;
    }

    @Operation(summary = "Listar todos os usuários em cache")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuários em cache retornada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Nenhum usuário encontrado no cache.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado.", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/cache/todos")
    public List<UsuarioDTO> retornaTodosUsuariosEmCache() {
        List<UsuarioDTO> usuarios = usuarioCachingService.findAllUsuarios();
        if (usuarios.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhum usuário encontrado no cache.");
        }
        return usuarios;
    }

    @Operation(summary = "Retorna os usuários paginados em cache")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuários paginados retornados com sucesso."),
            @ApiResponse(responseCode = "400", description = "Parâmetros de paginação inválidos. | Campo de ordenação inválido.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Nenhum usuário encontrado.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado.", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/cache/todos-paginados")
    public ResponseEntity<Page<UsuarioDTO>> paginarUsuariosCache(
            @RequestParam(value = "pagina", defaultValue = "0") Integer page,
            @RequestParam(value = "tamanho", defaultValue = "2") Integer size,
            @RequestParam(value = "ordenacao", defaultValue = "id,asc") String sort) {

        List<String> camposPermitidos = List.of("id", "dataCricacao", "nome", "email", "tipoUsuario");
        String[] partes = sort.split(",");
        String campo = partes[0];
        Sort.Direction direcao = partes.length > 1 && partes[1].equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        if (!camposPermitidos.contains(campo)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Campo de ordenação inválido.");
        }

        if(page < 0 || size < 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parâmetros de paginação inválidos.");
        }

        PageRequest pr = PageRequest.of(page, size, Sort.by(direcao, campo));
        Page<UsuarioDTO> paginas_usuarios_dto = usuarioService.paginarTodosOsUsuarios(pr);

        if (paginas_usuarios_dto.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhum usuário encontrado.");
        }

        return ResponseEntity.ok(paginas_usuarios_dto);
    }

    @Operation(summary = "Inserir um novo usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário inserido com sucesso."),
            @ApiResponse(responseCode = "400", description = "Campos obrigatórios não preenchidos ou inválidos.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "409", description = "Já existe um usuário cadastrado com o email informado. ", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Tipo de usuário não encontrado.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado.", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/inserir")
    public Usuario inserirUsuario(@RequestBody InserirUsuarioRequestDTO usuario) {
        if (usuario.getTipoUsuarioId() == null ||
                usuario.getNome() == null ||
                usuario.getEmail() == null ||
                usuario.getSenha() == null ||
                usuario.getDataCriacao() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Todos os campos de usuário são obrigatórios.");
        }

        Optional<TipoUsuario> TipoUsuarioOptional = tipoUsuarioRepository.findById(usuario.getTipoUsuarioId());
        if (TipoUsuarioOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tipo de usuário não encontrado.");
        }

        if (usuarioRepository.findByEmail(usuario.getEmail()) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um usuário cadastrado com o email: " + usuario.getEmail());
        }

        Usuario novoUsuario = new Usuario();
        novoUsuario.setTipoUsuario(TipoUsuarioOptional.get());
        novoUsuario.setNome(usuario.getNome());
        novoUsuario.setEmail(usuario.getEmail());
        novoUsuario.setSenha(usuario.getSenha());
        novoUsuario.setDataCriacao(usuario.getDataCriacao());

        Usuario usuarioSalvo = usuarioRepository.save(novoUsuario);
        usuarioCachingService.limparCache();
        return usuarioSalvo;
    }

}
