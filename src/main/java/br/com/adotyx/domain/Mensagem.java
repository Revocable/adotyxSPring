package br.com.adotyx.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "mensagens")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mensagem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne // Sem CascadeType.REMOVE
    @JoinColumn(name = "remetente_id", nullable = true)
    private Usuario remetente;
    
    @ManyToOne // Sem CascadeType.REMOVE
    @JoinColumn(name = "destinatario_id", nullable = true)
    private Usuario destinatario;
    @Column(nullable = false)
    private String mensagem;

    @Column(nullable = false)
    private LocalDateTime dataEnvio;

    @ManyToOne(cascade = CascadeType.ALL) // Cascateia a remoção de mensagens quando o animal associado for deletado
    @JoinColumn(name = "animal_id", nullable = true)
    private Animal animal; // Caso a mensagem esteja relacionada a um animal específico
}
