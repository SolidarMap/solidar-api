package br.com.solidarmap.solidar_api.controller;

import br.com.solidarmap.solidar_api.model.TipoUsuario;
import br.com.solidarmap.solidar_api.repository.TipoUsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController()
@RequestMapping(value = "/tipoUsuario")
@Tag(name = "Tipo de Usuário", description = "Operações relacionadas aos tipos de usuários.")
public class TipoUsuarioController {

    @Autowired
    private TipoUsuarioRepository tipoUsuarioRepository;

    @Operation(summary = "Listar todos os tipos de usuários")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de tipos de usuários retornada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Nenhum tipo de usuário encontrado.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado.", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/todos")
    public List<TipoUsuario> findAllTiposUsuarios() {
        List<TipoUsuario> tiposUsuarios = tipoUsuarioRepository.findAll();
        if (tiposUsuarios.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhum tipo de usuário encontrado.");
        }
        return tiposUsuarios;
    }
}
