package br.com.solidarmap.solidar_api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "T_SMP_USUARIOS")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_USUARIO", precision = 10, scale = 0)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ID_TIPO_USUARIO", nullable = false, foreignKey = @ForeignKey(name = "FK_USUARIO_TIPO_USUARIO"))
    private TipoUsuario tipoUsuario;

    @Column(name = "NOME", length = 100, nullable = false)
    private String nome;

    @Column(name = "EMAIL", length = 100, unique = true, nullable = false)
    private String email;

    @Column(name = "SENHA", length = 128, nullable = false)
    private String senha;

    @Column(name = "DATA_CRIACAO", nullable = false)
    private LocalDateTime dataCriacao;

}
