package com.projetoProvaTcc.repository;

import com.projetoProvaTcc.model.Disciplina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DaoDisciplinaRepository extends JpaRepository<Disciplina, Long> {

}
