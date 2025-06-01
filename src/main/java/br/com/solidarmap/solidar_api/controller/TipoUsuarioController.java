package br.com.solidarmap.solidar_api.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping(value = "/tipoUsuario")
@Tag(name = "Tipo de Usuário", description = "Operações relacionadas aos tipos de usuários.")
public class TipoUsuarioController {
}
