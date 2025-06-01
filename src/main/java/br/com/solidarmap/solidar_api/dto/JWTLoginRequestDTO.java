package br.com.solidarmap.solidar_api.dto;

import lombok.Getter;

@Getter
public class JWTLoginRequestDTO {

    private String email;
    private String senha;

}
