package com.projetoProvaTcc.mapper;

import com.projetoProvaTcc.dto.QuestaoDTO;
import com.projetoProvaTcc.dto.TagDTO;
import com.projetoProvaTcc.entity.Opcao;
import com.projetoProvaTcc.entity.Questao;
import com.projetoProvaTcc.entity.Tag;
import com.projetoProvaTcc.exception.ModelException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TagMapper {

    public static TagDTO toDTO(Tag tag) {
        TagDTO dto = new TagDTO();
        dto.setId(tag.getId());
        dto.setTagName(tag.getTagName());
        dto.setAssunto(tag.getAssunto());
        dto.setConjQuestoes(tag.getConjQuestoes()
                .stream()
                .map(r-> new QuestaoDTO(r.getId()))
                .collect(Collectors.toList()));
        return dto;
    }

    public static Tag toEntity(TagDTO dto, List<Questao> questoes) throws  ModelException {
        Tag tag = new Tag();
        tag.setId(dto.getId());
        tag.setTagName(dto.getTagName());
        tag.setAssunto(dto.getAssunto());
        tag.setConjQuestoes(questoes);
        return tag;
    }

}
