package br.com.adotyx.model.dao;

import br.com.adotyx.domain.Mensagem;
import br.com.adotyx.domain.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MensagemDAO extends JpaRepository<Mensagem, Long> {

    // Método para buscar todas as mensagens de um determinado animal
    List<Mensagem> findByAnimalId(Long animalId);

    // Método para buscar mensagens entre remetente e destinatário, com animalId
    List<Mensagem> findByAnimalIdAndRemetenteOrDestinatario(Long animalId, Usuario remetente, Usuario destinatario);

    // Método para buscar mensagens entre dois usuários (remetente e destinatário)

    // Método para buscar mensagens entre dois usuários utilizando IDs
    List<Mensagem> findByRemetenteIdAndDestinatarioId(Long remetenteId, Long destinatarioId);

    @Query("SELECT m FROM Mensagem m WHERE (m.remetente = :remetente AND m.destinatario = :destinatario) " +
            "OR (m.remetente = :destinatario AND m.destinatario = :remetente) ORDER BY m.dataEnvio")
    List<Mensagem> findByRemetenteAndDestinatario(@Param("remetente") Usuario remetente,
            @Param("destinatario") Usuario destinatario);

    void deleteByDestinatarioId(Long destinatarioId);

    // Método para excluir mensagens onde o usuário é o remetente
    void deleteByRemetenteId(Long remetenteId); // Adicionando o método que faltava
}
