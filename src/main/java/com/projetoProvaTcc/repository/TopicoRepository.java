package com.projetoProvaTcc.repository;

import com.projetoProvaTcc.entity.Topico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicoRepository extends JpaRepository<Topico, Long> {
    List<Topico> findAllByNomeIn(List<String> nome);
}
