package br.com.solidarmap.solidar_api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "T_SMP_MENSAGENS")
public class Mensagem {

    @Id
    @SequenceGenerator(name = "SEQ_MENSAGENS_ID_MENSAGEM", sequenceName = "SEQ_MENSAGENS_ID_MENSAGEM", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_MENSAGENS_ID_MENSAGEM")
    @Column(name = "ID_MENSAGEM")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ID_AJUDA", nullable = false)
    private Ajuda ajuda;

    @ManyToOne
    @JoinColumn(name = "ID_USUARIO", nullable = false)
    private Usuario usuario;

    @Column(name = "MENSAGEM", length = 500, nullable = false)
    private String mensagem;

    @Column(name = "DATA_ENVIO", nullable = false)
    private LocalDateTime dataEnvio;
}
