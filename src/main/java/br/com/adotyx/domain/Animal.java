package br.com.adotyx.domain;

import java.io.Serializable;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "animais")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Animal implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private Integer idade;

    @Column(nullable = false)
    private String raca;

    @Column(nullable = false)
    private String sexo;

    @Column(nullable = true)
    private String pathFoto;

    @ManyToOne
    @JoinColumn(name = "tutor_id")
    private Usuario tutor;
}