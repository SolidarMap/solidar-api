package br.com.solidarmap.solidar_api.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    OpenAPI configurarSwagger() {

        return new OpenAPI().info(new Info()
                .title("SolidarMap - Sistema de Ajuda Colaborativa em Crises")
                .description("O SolidarMap é um sistema que conecta pessoas em situações de emergência com recursos essenciais como comida, abrigo, remédios e água. "
                        + "A plataforma utiliza localização geográfica, comunicação direta e avaliações para promover a confiança e agilidade em momentos críticos.")
                .summary("SolidarMap: conectando comunidades para oferecer e receber ajuda em cenários de desastres naturais e crises humanitárias.")
                .version("v1.0.0")
                .contact(new Contact()
                        .email("solidar-map@pedroantonieti.dev")
                        .name("E-mail"))
        );
    }
}
