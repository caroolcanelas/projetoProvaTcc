package com.projetoProvaTcc.service;

import com.projetoProvaTcc.entity.Disciplina;
import com.projetoProvaTcc.repository.DaoDisciplinaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DisciplinaService {


    @Autowired
    private DaoDisciplinaRepository repository;

    //service precisa pegar as informações vindas pelo endpoint e atraves do JpaRepository, salvar no banco
    @Transactional
    public Disciplina salvar(Disciplina disciplina) {
        return repository.save(disciplina);  // Salva a entidade no banco
    }
}
