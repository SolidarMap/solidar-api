package br.com.solidarmap.solidar_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class JWTValidarTokenResponseDTO {

    private String token;
    private boolean statusToken;
    private String message;
}
