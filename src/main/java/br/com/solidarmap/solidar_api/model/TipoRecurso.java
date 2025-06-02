package br.com.solidarmap.solidar_api.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "T_SMP_TIPO_RECURSOS")
public class TipoRecurso {

    @Id
    @SequenceGenerator(name = "SEQ_TIPO_RECURSOS_ID_RECURSO", sequenceName = "SEQ_TIPO_RECURSOS_ID_RECURSO", allocationSize = 1)
@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TIPO_RECURSOS_ID_RECURSO")
    @Column(name = "ID_RECURSO")
    private Long id;

    @Column(name = "RECURSO", nullable = false, length = 50)
    private String recurso;
}
