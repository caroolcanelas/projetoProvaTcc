package com.projetoProvaTcc.service;

import com.projetoProvaTcc.dto.TagDTO;
import com.projetoProvaTcc.entity.Questao;
import com.projetoProvaTcc.entity.Tag;
import com.projetoProvaTcc.exception.ModelException;
import com.projetoProvaTcc.mapper.TagMapper;
import com.projetoProvaTcc.repository.QuestaoRepository;
import com.projetoProvaTcc.repository.TagRepository;

import com.projetoProvaTcc.repository.TopicoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagService {

    //cria instancia automatica e coloca na variavel
    @Autowired
    private TagRepository repository;

    @Autowired
    private QuestaoRepository questaoRepository;

    @Autowired
    private TopicoRepository topicoRepository;

    @Transactional
    public TagDTO salvar(TagDTO dto) throws  ModelException{
        List<Questao> questoes = dto.getConjQuestoes().stream()
                .map(id -> questaoRepository.findById(id.getId())
                        .orElseThrow(() -> new RuntimeException("Questão não encontrada: " + id)))
                .collect(Collectors.toList());

        Tag tag = TagMapper.toEntity(dto, questao);
        questao.forEach(r -> r.setTag(tag));

        Tag salva = repository.save(tag);
        return TagMapper.toDTO(salva);

    }

    public List<TagDTO> buscarTodasTags() {
        return repository.findAll().stream()
                .map(TagMapper::toDTO)
                .collect(Collectors.toList());
    }

    public boolean deletarTagPorId(long id) {
        if(repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

}
