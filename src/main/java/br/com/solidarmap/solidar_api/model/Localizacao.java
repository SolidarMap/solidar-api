package br.com.solidarmap.solidar_api.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "T_SMP_LOCALIZACOES")
public class Localizacao {

    @Id
    @SequenceGenerator(name = "SEQ_LOCALIZACOES_ID_LOCALIZACAO", sequenceName = "SEQ_LOCALIZACOES_ID_LOCALIZACAO", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_LOCALIZACOES_ID_LOCALIZACAO")
    @Column(name = "ID_LOCALIZACAO")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ID_AJUDA", nullable = false)
    private Ajuda ajuda;

    @ManyToOne
    @JoinColumn(name = "ID_ZONA", nullable = false)
    private TipoZona tipoZona;

    @Column(name = "LATITUDE", precision = 12, scale = 8, nullable = false)
    private BigDecimal latitude;

    @Column(name = "LONGITUDE", precision = 12, scale = 8, nullable = false)
    private BigDecimal longitude;
}
