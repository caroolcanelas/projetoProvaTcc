package com.projetoProvaTcc.service;

import com.projetoProvaTcc.dto.TagDTO;
import com.projetoProvaTcc.entity.Questao;
import com.projetoProvaTcc.entity.Tag;
import com.projetoProvaTcc.entity.Topico;
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

    @Autowired
    private TagRepository repository;

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private QuestaoRepository questaoRepository;

    @Transactional
    public TagDTO salvar(TagDTO dto) throws ModelException {

        List<Topico> topicos = topicoRepository.findAllByNomeIn(dto.getConjTopicosAderentes());

        List<Long> idsQuestoes = dto.getConjQuestoes() != null
                ? dto.getConjQuestoes().stream()
                .map(Integer::longValue)
                .collect(Collectors.toList())
                : List.of();

        List<Questao> questoes = questaoRepository.findAllById(idsQuestoes);

        Tag tag = TagMapper.toEntity(dto, topicos, questoes);
        Tag salva = repository.save(tag);
        return TagMapper.toDTO(salva);
    }

    public List<TagDTO> buscarTodasTags() {
        return repository.findAll().stream()
                .map(TagMapper::toDTO)
                .collect(Collectors.toList());
    }

    public TagDTO buscarPorId(int id) {
        return repository.findById((long) id)
                .map(TagMapper::toDTO)
                .orElse(null);
    }

    public boolean deletarTagPorId(long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
