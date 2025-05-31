package br.com.solidarmap.solidar_api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "T_SMP_TIPO_USUARIOS")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TipoUsuario {

    @Id
    @SequenceGenerator(name = "SEQ_TIPO_USUARIOS_ID_TIPO_USUARIOS", sequenceName = "SEQ_TIPO_USUARIOS_ID_TIPO_USUARIOS", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TIPO_USUARIOS_ID_TIPO_USUARIOS")
    @Column(name = "ID_TIPO_USUARIO")
    private Long id;

    @Column(name = "NOME_TIPO", length = 50, nullable = false)
    private String nomeTipo;
}
