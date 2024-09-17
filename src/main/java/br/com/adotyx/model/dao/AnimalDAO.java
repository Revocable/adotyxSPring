package br.com.adotyx.model.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import br.com.adotyx.domain.Animal;

public interface AnimalDAO extends JpaRepository<Animal, Long> {
    @Query("SELECT a FROM Animal a WHERE a.nome LIKE %:nome%")
    List<Animal> findByNome(@Param("nome") String nome);

    @Query("SELECT a FROM Animal a WHERE a.raca LIKE %:raca%")
    List<Animal> findByRaca(@Param("raca") String raca);
}
