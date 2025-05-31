package com.projetoProvaTcc.mapper;

import com.projetoProvaTcc.dto.TagDTO;
import com.projetoProvaTcc.dto.TopicoDTO;
import com.projetoProvaTcc.entity.Tag;
import com.projetoProvaTcc.entity.Topico;
import com.projetoProvaTcc.exception.ModelException;

import java.util.List;
import java.util.stream.Collectors;

public class TagMapper {


    public static TagDTO toDTO(Tag tag) {
        TagDTO dto = new TagDTO();
        dto.setId(tag.getId());
        dto.setAssunto(tag.getAssunto());
        dto.setTagName(tag.getTagName());

        //relacionamento topico
//        dto.setConjTopicosAderentes(tag.getConjTopicosAderentes()
//                .stream()
//                .map(topico -> {
//                    TopicoDTO o = new TopicoDTO();
//                    o.setId(topico.getId());
//                    o.setNome(topico.getNome());
//                    o.setConteudo(topico.getConteudo());
//                    o.setNumOrdem(topico.getNumOrdem());
//                    return o;
//                })
//                .collect(Collectors.toList()));
        return dto;
    }

    public static Tag toEntity(TagDTO dto) throws ModelException {
        Tag tag = new Tag();
        tag.setId(dto.getId());
        tag.setAssunto(dto.getAssunto());
        tag.setTagName(dto.getTagName());

        //tag.setConjTopicosAderentes(topicos);
        return tag;
    }
}
