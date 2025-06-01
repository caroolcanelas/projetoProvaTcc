package com.projetoProvaTcc.repository;

import com.projetoProvaTcc.entity.Topico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TopicoRepository extends JpaRepository<Topico, Long> {

    @Query("SELECT t FROM Topico t LEFT JOIN FETCH t.conjTags WHERE t.id = :id")
    Topico findByIdComTags(Long id);

    List<Topico> findAllByNomeIn(List<String> nome);
}
