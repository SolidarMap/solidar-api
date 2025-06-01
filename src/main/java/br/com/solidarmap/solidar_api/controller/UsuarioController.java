package br.com.solidarmap.solidar_api.controller;

import br.com.solidarmap.solidar_api.dto.UsuarioDTO;
import br.com.solidarmap.solidar_api.repository.UsuarioRepository;
import br.com.solidarmap.solidar_api.service.UsuarioCachingService;
import br.com.solidarmap.solidar_api.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping(value = "/usuario")
public class UsuarioController {

	@Autowired
	private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioCachingService usuarioCachingService;

    @Autowired
    private UsuarioService usuarioService;

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
            @ApiResponse(responseCode = "404", description = "Nenhum usuário encontrado.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado.", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/cache/todos-paginados")
    public ResponseEntity<Page<UsuarioDTO>> paginarUsuariosCache(
            @RequestParam(value = "pagina", defaultValue = "0") Integer page,
            @RequestParam(value = "tamanho", defaultValue = "2") Integer size) {

        if(page < 0 || size < 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parâmetros de paginação inválidos.");
        }

        PageRequest pr = PageRequest.of(page, size);
        Page<UsuarioDTO> paginas_usuarios_dto = usuarioService.paginarTodosOsUsuarios(pr);

        if (paginas_usuarios_dto.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhum usuário encontrado.");
        }

        return ResponseEntity.ok(paginas_usuarios_dto);
    }

}
