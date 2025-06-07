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
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "T_SMP_AVALIACOES")
public class Avaliacao {

    @Id
    @SequenceGenerator(name = "SEQ_AVALIACOES_ID_AVALIACOES", sequenceName = "SEQ_AVALIACOES_ID_AVALIACOES", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_AVALIACOES_ID_AVALIACOES")
    @Column(name = "ID_AVALIACAO")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_USUARIO", nullable = false, foreignKey = @ForeignKey(name = "FK_AVALIACAO_USUARIO"))
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_AJUDA", nullable = false, foreignKey = @ForeignKey(name = "FK_AVALIACAO_AJUDA"))
    private Ajuda ajuda;

    @Column(name = "NOTA", nullable = false, columnDefinition = "integer check (nota >= 0 and nota <= 5)")
    private Integer nota;

    @Column(name = "COMENTARIO", length = 150)
    private String comentario;

    @Column(name = "DATA_AVALIACAO", nullable = false)
    private LocalDateTime dataAvaliacao;

}
