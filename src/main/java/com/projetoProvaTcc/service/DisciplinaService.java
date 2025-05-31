package com.projetoProvaTcc.service;

import com.projetoProvaTcc.dto.DisciplinaDTO;
import com.projetoProvaTcc.entity.Disciplina;
import com.projetoProvaTcc.entity.Topico;
import com.projetoProvaTcc.exception.ModelException;
import com.projetoProvaTcc.mapper.DisciplinaMapper;
import com.projetoProvaTcc.repository.DisciplinaRepository;
import com.projetoProvaTcc.repository.TopicoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DisciplinaService {


    @Autowired
    private DisciplinaRepository repository;

    @Autowired
    private TopicoRepository topicoRepository;

    //service precisa pegar as informações vindas pelo endpoint e atraves do JpaRepository, salvar no banco
    //anotação para se caso der certo, salva a info no banco, se falhar ao final, tudo que foi feito no metodo é desfeito
    @Transactional
    public DisciplinaDTO salvar(DisciplinaDTO dto) throws ModelException {

        //procura o id do topico
        List<Topico> topicos = dto.getConjTopicos().stream()
                .map(id -> {
                    try {
                        return topicoRepository.findById(Long.valueOf(id))
                                .orElseThrow(() -> new ModelException("Tópico com ID " + id + " não encontrado"));
                    } catch (ModelException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());


        Disciplina disciplina = DisciplinaMapper.toEntity(dto, topicos);

        for (Topico t : topicos) {
            t.setDisciplina(disciplina);
        }

        disciplina.setConjTopicos(topicos);

        Disciplina salva = repository.save(disciplina);
        return DisciplinaMapper.toDTO(salva);  // Salva a entidade no banco
    }

    public List<DisciplinaDTO> buscarTodasDisciplinas(){
        return repository.findAllWithTopicos().stream()
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
