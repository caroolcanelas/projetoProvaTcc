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

    @Transactional
    public DisciplinaDTO atualizar(int idAtual, DisciplinaDTO dto) throws ModelException {
        Disciplina disciplina = repository.findById((long) idAtual)
                .orElseThrow(() -> new ModelException("Disciplina com ID " + idAtual + " não encontrada"));

        // Atualiza o ID (raro e delicado)
        if (dto.getId() != null && dto.getId() != idAtual) {
            if (repository.existsById((long) dto.getId())) {
                throw new ModelException("Já existe uma disciplina com o ID " + dto.getId());
            }
            disciplina.setId(dto.getId()); // novo ID
        }

        // Atualiza o código
        if (dto.getCodigo() != null) {
            disciplina.setCodigo(dto.getCodigo());
        }

        if (dto.getNome() != null) {
            disciplina.setNome(dto.getNome());
        }

        if (dto.getNumCreditos() != null) {
            disciplina.setNumCreditos(dto.getNumCreditos());
        }

        if (dto.getObjetivoGeral() != null) {
            disciplina.setObjetivoGeral(dto.getObjetivoGeral());
        }

        if (dto.getConjTopicos() != null) {
            List<Topico> topicos = dto.getConjTopicos().stream()
                    .map(tid -> topicoRepository.findById(Long.valueOf(tid))
                            .orElseThrow(() -> new ModelException("Tópico com ID " + tid + " não encontrado")))
                    .collect(Collectors.toList());

            for (Topico t : topicos) {
                t.setDisciplina(disciplina);
            }

            disciplina.setConjTopicos(topicos);
        }

        Disciplina salva = repository.save(disciplina);
        return DisciplinaMapper.toDTO(salva);
    }


    public boolean deletarDisciplinaPorId(long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    public void adicionarTopicoNaDisciplina(int idDisciplina, Topico topico) throws ModelException {
        //encontra a disciplina pelo id
        Disciplina disciplina = repository.findById((long)idDisciplina)
                .orElseThrow(() -> new ModelException("Disciplina não encontrada"));

        //confere se o tópico já existe no banco
        if (topico.getId() != 0) {
            Topico topicoExistente = topicoRepository.findById((long) topico.getId())
                    .orElseThrow(() -> new ModelException("Tópico com ID " + topico.getId() + " não encontrado"));

            // Impede que a gente relacione o mesmo tópico duas vezes
            if (disciplina.getConjTopicos().contains(topicoExistente)) {
                throw new ModelException("Tópico já está associado a esta disciplina.");
            }

            //se o topico já existe adiciona na disciplina
            topicoExistente.setDisciplina(disciplina);
            disciplina.addTopico(topicoExistente);
        } else {
            // se não existe ele cria um tópico novo
            topico.setDisciplina(disciplina);
            disciplina.addTopico(topico);
        }

        //salva no banco
        repository.save(disciplina);
    }

    public void removerTopicoDaDisciplina(int idDisciplina, int idTopico) throws ModelException {
        Disciplina disciplina = repository.findById((long)idDisciplina)
                .orElseThrow(() -> new ModelException("Disciplina não encontrada"));

        Topico topico = topicoRepository.findById((long)idTopico)
                .orElseThrow(() -> new ModelException("Tópico não encontrado"));

        disciplina.removeTopico(topico); // remove topico

        repository.save(disciplina); // salva a alteracao
    }


    public DisciplinaDTO buscarPorId(int id) {
        Disciplina disciplina = repository.findById((long) id).orElse(null);
        if (disciplina == null) return null;
        return DisciplinaMapper.toDTO(disciplina);
    }
}
