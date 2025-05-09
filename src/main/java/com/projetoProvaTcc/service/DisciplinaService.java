package com.projetoProvaTcc.service;

import com.projetoProvaTcc.model.Disciplina;
import com.projetoProvaTcc.repository.DaoDisciplinaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DisciplinaService {


    @Autowired
    private DaoDisciplinaRepository daoDisciplinaRepository;

    //service precisa pegar as informações vindas pelo endpoint e atraves do JpaRepository, salvar no banco
    @Transactional
    public Disciplina salvarDisciplina(Disciplina disciplina) {
        return daoDisciplinaRepository.save(disciplina);  // Salva a entidade no banco
    }
}
