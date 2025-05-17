package com.projetoProvaTcc.service;

import com.projetoProvaTcc.entity.Disciplina;
import com.projetoProvaTcc.entity.Opcao;
import com.projetoProvaTcc.entity.Recurso;
import com.projetoProvaTcc.entity.Topico;
import com.projetoProvaTcc.exception.ModelException;
import com.projetoProvaTcc.repository.OpcaoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OpcaoService {

@Autowired
private OpcaoRepository repository;

@Transactional
public Opcao salvar(Opcao opcao) throws ModelException{
    return repository.save(opcao);  // aqui preciso fazer o mesmo for de conjTopicos com conjRecursos?
}

public List<Opcao> buscarTodasOpcoes(){
        return repository.findAll();
    }

public boolean deletarOpcaoPorId(long id){
    if(repository.existsById(id)){
        repository.deleteById(id);
        return true;
    }
    return false;
}


}
