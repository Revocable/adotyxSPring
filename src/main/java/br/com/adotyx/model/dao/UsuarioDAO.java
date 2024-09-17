package br.com.adotyx.model.dao;

import org.hibernate.mapping.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import br.com.adotyx.domain.Usuario;

public interface UsuarioDAO extends JpaRepository <Usuario, Long>  {

}
