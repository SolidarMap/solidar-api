package br.com.solidarmap.solidar_api.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "T_SMP_TIPO_ZONAS")
public class TipoZona {

    @Id
    @SequenceGenerator(name = "SEQ_TIPO_ZONAS_ID_ZONA", sequenceName = "SEQ_TIPO_ZONAS_ID_ZONA", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TIPO_ZONAS_ID_ZONA")
    @Column(name = "ID_ZONA")
    private Long id;

    @Column(name = "ZONA", length = 30, nullable = false)
    private String zona;
}
