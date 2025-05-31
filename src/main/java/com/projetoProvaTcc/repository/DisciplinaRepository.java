package com.projetoProvaTcc.repository;

import com.projetoProvaTcc.entity.Disciplina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DisciplinaRepository extends JpaRepository<Disciplina, Long> {

    //métodod pra buscar todas as disciplinas relacionadas com topicos
    @Query("SELECT d FROM Disciplina d LEFT JOIN FETCH d.conjTopicos")
    List<Disciplina> findAllWithTopicos();

}
