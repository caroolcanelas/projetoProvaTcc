package com.projetoProvaTcc.repository;

import com.projetoProvaTcc.entity.Opcao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OpcaoRepository extends JpaRepository<Opcao, Long> {
}
