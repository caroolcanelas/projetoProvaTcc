package com.projetoProvaTcc.service;

import com.projetoProvaTcc.dto.TopicoDTO;
import com.projetoProvaTcc.entity.Tag;
import com.projetoProvaTcc.entity.Topico;
import com.projetoProvaTcc.exception.ModelException;
import com.projetoProvaTcc.mapper.TopicoMapper;
import com.projetoProvaTcc.repository.TagRepository;
import com.projetoProvaTcc.repository.TopicoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class TopicoService {

    @Autowired
    private TopicoRepository repository;

    @Autowired
    private TagRepository tagRepository;

    @Transactional
    public TopicoDTO salvar(TopicoDTO dto) throws ModelException {

        //relacionamento com tags
//        List<Tag> tags = dto.getConjTags().stream()
//                .map(tagDTO ->
//                        tagRepository.findById(Long.valueOf(tagDTO.getId()))
//                                .orElseGet(() -> {
//                                    // Criar nova Tag se não existir
//                                    Tag novaTag = new Tag();
//                                    try {
//                                        novaTag.setTagName(tagDTO.getTagName());
//                                    } catch (ModelException e) {
//                                        throw new RuntimeException(e);
//                                    }
//                                    try {
//                                        novaTag.setAssunto(tagDTO.getAssunto());
//                                    } catch (ModelException e) {
//                                        throw new RuntimeException(e);
//                                    }
//                                    return tagRepository.save(novaTag);
//                                })
//                )
//                .collect(Collectors.toList());

        Topico topico = TopicoMapper.toEntity(dto);
        Topico salva = repository.save(topico);
        return TopicoMapper.toDTO(salva);
    }

    public List<TopicoDTO> buscarTodosTopicos() {
        return repository.findAll().stream()
                .map(TopicoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public boolean deletarOpcaoPorId(long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

}
