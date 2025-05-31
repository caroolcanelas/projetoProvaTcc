package com.projetoProvaTcc.service;

import com.projetoProvaTcc.dto.DisciplinaDTO;
import com.projetoProvaTcc.entity.Disciplina;
import com.projetoProvaTcc.exception.ModelException;
import com.projetoProvaTcc.mapper.DisciplinaMapper;
import com.projetoProvaTcc.repository.DisciplinaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DisciplinaService {


    @Autowired
    private DisciplinaRepository repository;

    //service precisa pegar as informações vindas pelo endpoint e atraves do JpaRepository, salvar no banco
    //anotação para se caso der certo, salva a info no banco, se falhar ao final, tudo que foi feito no metodo é desfeito
    @Transactional
    public DisciplinaDTO salvar(DisciplinaDTO dto) throws ModelException {

        //se conjTopicos vier vazio na requisição POST, nao vai entrar no for e vai salvar direto
        //for (Topico topico : dto.getConjTopicos()) {
         //   topico.setDisciplina(dto);  // garante que cada topico da disciplina tenha o campo disciplina corretamente. evita null
       // }
        Disciplina disciplina = DisciplinaMapper.toEntity(dto);
        Disciplina salva = repository.save(disciplina);
        return DisciplinaMapper.toDTO(salva);  // Salva a entidade no banco
    }

    public List<DisciplinaDTO> buscarTodasDisciplinas(){
        return repository.findAll().stream()
                .map(DisciplinaMapper::toDTO)
                .collect(Collectors.toList());
    }

    public boolean deletarDisciplinaPorId(long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
