package com.projetoProvaTcc.service;

import com.projetoProvaTcc.dto.TopicoDTO;
import com.projetoProvaTcc.entity.Disciplina;
import com.projetoProvaTcc.entity.Tag;
import com.projetoProvaTcc.entity.Topico;
import com.projetoProvaTcc.exception.ModelException;
import com.projetoProvaTcc.mapper.TopicoMapper;
import com.projetoProvaTcc.repository.DisciplinaRepository;
import com.projetoProvaTcc.repository.TagRepository;
import com.projetoProvaTcc.repository.TopicoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;



@Service
public class TopicoService {

    @Autowired
    private TopicoRepository repository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private DisciplinaRepository disciplinaRepository;

    @Autowired
    private TopicoRepository topicoRepository;


    @Transactional
    public TopicoDTO salvar(TopicoDTO dto) throws ModelException {

        //busca id da disciplina
        Disciplina disciplina = disciplinaRepository.findById(Long.valueOf(dto.getDisciplina()))
                .orElseThrow(() -> new ModelException("Disciplina não encontrada"));

        //busca id do subtopico
        List<Topico> subTopicos = dto.getConjSubTopicos().stream()
                .map(id -> {
                    try {
                        return topicoRepository.findById(Long.valueOf(id))
                                .orElseThrow(() -> new ModelException("Subtópico " + id + " não encontrado"));
                    } catch (ModelException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());

        //busca pelo tagName
        List<Tag> tags = tagRepository.findAllByTagNameIn(dto.getConjTags()); //  método para buscar o nome das tags

        Topico topico = TopicoMapper.toEntity(dto, disciplina, subTopicos, tags);

        for (Tag tag : tags) {
            tag.addTopicoAderente(topico); // Isso vai garantir que a tag reconhece o topico
        }

        Topico salva = repository.save(topico);
        return TopicoMapper.toDTO(salva);
    }

    public List<TopicoDTO> buscarTodosTopicos() {
        return repository.findAll().stream()
                .map(TopicoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public  TopicoDTO buscarPorId(int id) {
        Topico topico = repository.findById((long) id).orElse(null);
        if (topico == null) return null;
        return TopicoMapper.toDTO(topico);
    }

    public boolean deletarTopicoPorId(long id) throws ModelException {
        Topico topico = repository.findById(id).orElse(null);
        if (topico == null) {
            return false;
        }

        // Remove o topico da lista de cada tag que o referencia
        List<Tag> tagsRelacionadas = topico.getConjTags();
        for (Tag tag : tagsRelacionadas) {
            tag.removeTopicoAderente(topico); // Garante que a tag pare de referenciar o topico
        }

        // Agora sim pode remover
        repository.delete(topico);
        return true;
    }

    public void adicionarSubTopicoEmTopico(int idTopico, Topico subTopico) throws ModelException {

        //encontra o topico pai pelo id
        Topico topicoPai = topicoRepository.findById((long) idTopico)
                .orElseThrow(() -> new ModelException("Tópico principal não encontrado."));

        // Se o subtópico já existe no banco, buscamos ele
        if (subTopico.getId() != 0) {
            Topico existente = topicoRepository.findById((long) subTopico.getId())
                    .orElseThrow(() -> new ModelException("Subtópico com ID " + subTopico.getId() + " não encontrado"));

            topicoPai.addSubTopico(existente);
        } else {
            // Ou criamos um novo
            topicoPai.addSubTopico(subTopico);
        }

        topicoRepository.save(topicoPai);
    }


    public void removerSubtopico(int idTopico, int idSubtopico) throws ModelException {
        Topico topicoPai = topicoRepository.findById((long) idTopico)
                .orElseThrow(() -> new ModelException("Tópico principal não encontrado."));

        Topico subTopico = topicoRepository.findById((long) idSubtopico)
                .orElseThrow(() -> new ModelException("Subtópico não encontrado."));

        boolean removido = topicoPai.removeSubTopico(subTopico);
        if (!removido) {
            throw new ModelException("O subtópico informado não está associado a esse tópico.");
        }

        topicoRepository.save(topicoPai);
    }
}
