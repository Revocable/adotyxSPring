package br.com.adotyx.model.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import br.com.adotyx.domain.Usuario;
import java.util.Optional;

public interface UsuarioDAO extends JpaRepository<Usuario, Long> {
    @Query("SELECT u FROM Usuario u WHERE u.nome LIKE %:nome%")
    /*List<Usuario> findByNome(@Param("nome") String nome);*/

    Usuario findByNome(String nome);  // Método para buscar o usuário pelo nome

    @Query("SELECT u FROM Usuario u WHERE u.pathFoto LIKE %:pathFoto%")
    List<Usuario> findByPathFoto(@Param("pathFoto") String pathFoto);
    
    Optional<Usuario> findByEmail(String email);
}
