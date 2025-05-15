package com.projetoProvaTcc.service;

import com.projetoProvaTcc.entity.Disciplina;
import com.projetoProvaTcc.entity.Topico;
import com.projetoProvaTcc.exception.ModelException;
import com.projetoProvaTcc.repository.DaoDisciplinaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DisciplinaService {


    @Autowired
    private DaoDisciplinaRepository repository;

    //service precisa pegar as informações vindas pelo endpoint e atraves do JpaRepository, salvar no banco
    //anotação para se caso der certo, salva a info no banco, se falhar ao final, tudo que foi feito no metodo é desfeito
    @Transactional
    public Disciplina salvar(Disciplina disciplina) throws ModelException {

        //se conjTopicos vier vazio na requisição POST, nao vai entrar no for e vai salvar direto
        for (Topico topico : disciplina.getConjTopicos()) {
            topico.setDisciplina(disciplina);  // garante que cada topico da disciplina tenha o campo disciplina corretamente. evita null
        }
        return repository.save(disciplina);  // Salva a entidade no banco
    }
}
