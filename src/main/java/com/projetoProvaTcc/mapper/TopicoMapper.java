package com.projetoProvaTcc.mapper;

import com.projetoProvaTcc.dto.TagDTO;
import com.projetoProvaTcc.dto.TopicoDTO;
import com.projetoProvaTcc.entity.Tag;
import com.projetoProvaTcc.entity.Topico;
import com.projetoProvaTcc.exception.ModelException;

import java.util.List;
import java.util.stream.Collectors;

public class TopicoMapper {

    public static TopicoDTO toDTO(Topico topico) {
        TopicoDTO dto = new TopicoDTO();
        dto.setId(topico.getId());
        dto.setNumOrdem(topico.getNumOrdem());
        dto.setNome(topico.getNome());
        dto.setConteudo(topico.getConteudo());

        //relacionamento com tag
//        dto.setConjTags(topico.getConjTags()
//                .stream()
//                .map(tag -> {
//                    TagDTO o = new TagDTO();
//                    o.setId(tag.getId());
//                    o.setTagName(tag.getTagName());
//                    o.setAssunto(tag.getAssunto());
//                    return o;
//                })
//                .collect(Collectors.toList()));

        return dto;

    }

    public static Topico toEntity(TopicoDTO dto) throws ModelException {
        Topico topico = new Topico();
        topico.setId(dto.getId());
        topico.setNumOrdem(dto.getNumOrdem());
        topico.setNome(dto.getNome());
        topico.setConteudo(dto.getConteudo());

//        topico.setConjTags(tags);

        return topico;
    }

}
