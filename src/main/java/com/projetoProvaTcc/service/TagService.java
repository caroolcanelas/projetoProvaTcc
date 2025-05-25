package com.projetoProvaTcc.service;

import com.projetoProvaTcc.dto.TagDTO;
import com.projetoProvaTcc.entity.Tag;
import com.projetoProvaTcc.exception.ModelException;
import com.projetoProvaTcc.mapper.TagMapper;
import com.projetoProvaTcc.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagService {

    @Autowired
    private TagRepository repository;

    public TagDTO salvar(TagDTO dto) throws ModelException {
        Tag tag = TagMapper.toEntity(dto);
        Tag salva = repository.save(tag);
        return TagMapper.toDTO(salva);
    }

    public List<TagDTO> buscarTodasTags() {
        return repository.findAll().stream()
                .map(TagMapper::toDTO)
                .collect(Collectors.toList());
    }

    public boolean deletarTagPorId(long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
