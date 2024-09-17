package br.com.adotyx.model.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import br.com.adotyx.domain.Usuario;

public interface UsuarioDAO extends JpaRepository<Usuario, Long> {
    @Query("SELECT u FROM Usuario u WHERE u.nome LIKE %:nome%")
    List<Usuario> findByNome(@Param("nome") String nome);

    @Query("SELECT u FROM Usuario u WHERE u.email = :email")
    Usuario findByEmail(@Param("email") String email);

    @Query("SELECT u FROM Usuario u WHERE u.pathFoto LIKE %:pathFoto%")
    List<Usuario> findByPathFoto(@Param("pathFoto") String pathFoto);
}
