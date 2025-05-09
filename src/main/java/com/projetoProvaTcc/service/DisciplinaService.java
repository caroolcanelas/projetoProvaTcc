package com.projetoProvaTcc.service;

import com.projetoProvaTcc.model.Disciplina;
import com.projetoProvaTcc.repository.DaoDisciplinaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DisciplinaService {

//    @PersistenceContext
//    private EntityManager entityManager;

    @Autowired
    private DaoDisciplinaRepository daoDisciRepository;

    @Transactional
    public Disciplina salvarDisciplina(Disciplina disciplina) {
        return daoDisciRepository.save(disciplina);  // Salva a entidade no banco
    }
}
