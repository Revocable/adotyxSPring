package br.com.adotyx.model.dao;

import org.hibernate.mapping.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import br.com.adotyx.domain.Animal;

public interface AnimalDAO extends JpaRepository <Animal, Long>  {

}
