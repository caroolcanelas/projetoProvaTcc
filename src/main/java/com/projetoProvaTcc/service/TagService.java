package com.projetoProvaTcc.service;

import com.projetoProvaTcc.dto.TagDTO;
import com.projetoProvaTcc.entity.Tag;
import com.projetoProvaTcc.entity.Topico;
import com.projetoProvaTcc.exception.ModelException;
import com.projetoProvaTcc.mapper.TagMapper;
import com.projetoProvaTcc.repository.TagRepository;
import com.projetoProvaTcc.repository.TopicoRepository;
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

    public TagDTO salvar(TagDTO dto) throws ModelException {

//        List<Topico> topicos = dto.getConjTopicosAderentes().stream()
//                .map(topicoDTO ->
//                        topicoRepository.findById(Long.valueOf(topicoDTO.getId()))
//                                .orElseGet(() -> {
//                                    // Criar nova topico se não existir
//                                    Topico novoTopico = new Topico();
//                                    try {
//                                        novoTopico.setNome(topicoDTO.getNome());
//                                    } catch (ModelException e) {
//                                        throw new RuntimeException(e);
//                                    }
//                                    try {
//                                        novoTopico.setNumOrdem(topicoDTO.getNumOrdem());
//                                    } catch (ModelException e) {
//                                        throw new RuntimeException(e);
//                                    }
//                                    try {
//                                        novoTopico.setConteudo(topicoDTO.getConteudo());
//                                    } catch (ModelException e) {
//                                        throw new RuntimeException(e);
//                                    }
//                                    return topicoRepository.save(novoTopico);
//                                })
//                )
//                .collect(Collectors.toList());


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
