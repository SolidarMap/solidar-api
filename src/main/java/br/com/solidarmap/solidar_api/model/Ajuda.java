package br.com.solidarmap.solidar_api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "T_SMP_AJUDAS")
public class Ajuda {

    @Id
    @SequenceGenerator(name = "SEQ_AJUDAS_ID_AJUDA", sequenceName = "SEQ_AJUDAS_ID_AJUDA", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_AJUDAS_ID_AJUDA")
    @Column(name = "ID_AJUDA")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ID_USUARIO", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "ID_RECURSO", nullable = false)
    private TipoRecurso recurso;

    @Column(name = "DESCRICAO", length = 256)
    private String descricao;

    @Column(name = "STATUS", nullable = false, length = 1)
    private String status;

    @Column(name = "DATA_PUBLICACAO", nullable = false)
    private LocalDateTime dataPublicacao;

}
