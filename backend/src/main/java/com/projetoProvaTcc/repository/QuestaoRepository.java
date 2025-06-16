package com.projetoProvaTcc.repository;

import com.projetoProvaTcc.entity.Questao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface QuestaoRepository extends JpaRepository<Questao, Long> {
    List<Questao> findAllByProfessorValidadorId(Long professorId);

}
