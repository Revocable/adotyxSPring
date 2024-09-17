package br.com.adotyx.domain;

import java.io.Serializable;
import jakarta.persistence.*;


@Entity
@Table(name="animais")
public class Animal implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique= false)
    private String nome;

    @Column(nullable = false, unique = false)
    private Integer idade;

    @Column(nullable = false, unique = false)
    private String raca;

    @Column(nullable = false, unique=false)
    private char sexo;

    @Column(nullable = true, unique = false)
    private String pathFoto;

    @Column(nullable = true, unique = false)
    private Usuario tutor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getIdade() {
        return idade;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }

    public String getRaca() {
        return raca;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }

    public char getSexo() {
        return sexo;
    }

    public void setSexo(char sexo) {
        this.sexo = sexo;
    }

    public String getPathFoto() {
        return pathFoto;
    }

    public void setPathFoto(String pathFoto) {
        this.pathFoto = pathFoto;
    }

    public Usuario getTutor() {
        return tutor;
    }

    public void setTutor(Usuario tutor) {
        this.tutor = tutor;
    }

    public Animal() {
    }

    public Animal(String nome, Integer idade, String raca, char sexo, String pathFoto, Usuario tutor) {
        this.nome = nome;
        this.idade = idade;
        this.raca = raca;
        this.sexo = sexo;
        this.pathFoto = pathFoto;
        this.tutor = tutor;
    }

    
    
}
