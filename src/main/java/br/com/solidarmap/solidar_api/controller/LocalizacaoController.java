package br.com.solidarmap.solidar_api.controller;

import br.com.solidarmap.solidar_api.dto.InserirLocalizacaoRequestDTO;
import br.com.solidarmap.solidar_api.dto.LocalizacaoDTO;
import br.com.solidarmap.solidar_api.model.Ajuda;
import br.com.solidarmap.solidar_api.model.Localizacao;
import br.com.solidarmap.solidar_api.model.TipoZona;
import br.com.solidarmap.solidar_api.repository.AjudaRepository;
import br.com.solidarmap.solidar_api.repository.LocalizacaoRepository;
import br.com.solidarmap.solidar_api.repository.TipoZonaRepository;
import br.com.solidarmap.solidar_api.service.LocalizacaoCachingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping(value = "/localizacao")
@Tag(name = "Localização", description = "Operações relacionadas as localizações.")
public class LocalizacaoController {

    @Autowired
    LocalizacaoRepository localizacaoRepository;

    @Autowired
    AjudaRepository ajudaRepository;

    @Autowired
    TipoZonaRepository tipoZonaRepository;

    @Autowired
    LocalizacaoCachingService localizacaoCachingService;

    @Operation(summary = "Buscar localização por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Localização encontrado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Localização não encontrada.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = "ID não pode ser nulo ou vazio.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado.", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/buscar/id/{id}")
    public LocalizacaoDTO retornaLocalizacaoPorId(@PathVariable Long id) {
        if (id == null || id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID da localização não pode ser nulo ou menor que 1.");
        }

        LocalizacaoDTO localizacao = localizacaoRepository.findByIdLocalizacao(id);
        if (localizacao == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Localização não encontrada.");
        }
        return localizacao;
    }

    @Operation(summary = "Buscar localização por ID da Ajuda")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Localização encontrado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Localização não encontrada com o ID da Ajuda informado.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = "ID não pode ser nulo ou vazio.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado.", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/buscar/idAjuda/{id}")
    public LocalizacaoDTO retornnaLocalizacaoPorIdAjuda(@PathVariable Long id) {
        if (id == null || id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID da Ajuda não pode ser nulo ou menor que 1.");
        }

        LocalizacaoDTO localizacao = localizacaoRepository.findLocalizacaoByAjudaId(id);
        if (localizacao == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Localização não encontrada com o ID da Ajuda informado.");
        }
        return localizacao;
    }

    @Operation(summary = "Inserir uma nova localização")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Localização criada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para criação da localização.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado.", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/inserir")
    public Localizacao inserirLocalizacao(@RequestBody InserirLocalizacaoRequestDTO inserirLocalizacaoRequestDTO) {
        if (inserirLocalizacaoRequestDTO.getIdAjuda() == null ||
            inserirLocalizacaoRequestDTO.getIdTipoZona() == null ||
            inserirLocalizacaoRequestDTO.getLatitude() == null ||
            inserirLocalizacaoRequestDTO.getLongitude() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados inválidos para criação da localização.");
        }

        Optional<Ajuda> ajudaOptional = ajudaRepository.findById(inserirLocalizacaoRequestDTO.getIdAjuda());
        if (ajudaOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ajuda não encontrada com o ID informado.");
        }

        Optional<TipoZona> tipoZonaOptional = tipoZonaRepository.findById(inserirLocalizacaoRequestDTO.getIdTipoZona());
        if (tipoZonaOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tipo de Zona não encontrado com o ID informado.");
        }

        Localizacao novaLocalizacao = new Localizacao();
        novaLocalizacao.setAjuda(ajudaOptional.get());
        novaLocalizacao.setTipoZona(tipoZonaOptional.get());
        novaLocalizacao.setLatitude(inserirLocalizacaoRequestDTO.getLatitude());
        novaLocalizacao.setLongitude(inserirLocalizacaoRequestDTO.getLongitude());

        Localizacao localizacaoSalva = localizacaoRepository.save(novaLocalizacao);
        localizacaoCachingService.limparCache();
        return localizacaoSalva;
    }

    @Operation(summary = "Deletar uma localização")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Localização deletado com sucesso."),
        @ApiResponse(responseCode = "404", description = "Localização não encontrada com o ID informado.", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "403", description = "Usuário não autenticado.", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/deletar/{id}")
    public Localizacao removerLocalizacao(@PathVariable Long id) {
        Optional<Localizacao> op = localizacaoCachingService.findLocalizacaoByIdModel(id);

        if (op.isPresent()) {
            Localizacao localizacao = op.get();
            localizacaoRepository.delete(localizacao);
            localizacaoCachingService.limparCache();
            return localizacao;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Localização não encontrada com o ID informado.");
        }
    }
}
