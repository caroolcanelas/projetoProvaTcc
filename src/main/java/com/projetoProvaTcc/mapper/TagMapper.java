package com.projetoProvaTcc.mapper;

import com.projetoProvaTcc.dto.TagDTO;
import com.projetoProvaTcc.dto.TopicoDTO;
import com.projetoProvaTcc.entity.Questao;
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

        //relaciona com o nome dos topicos
        if (tag.getConjTopicosAderentes() != null) {
            dto.setConjTopicosAderentes(
                    tag.getConjTopicosAderentes().stream()
                            .map(Topico::getNome)
                            .collect(Collectors.toList())
            );
        }

        //confere o id de conjde questoes
        if (tag.getConjQuestoes() != null) {
            dto.setConjQuestoes(
                    tag.getConjQuestoes().stream()
                            .map(Questao::getId)
                            .collect(Collectors.toList())
            );
        }

        return dto;
    }

    public static Tag toEntity(TagDTO dto, List<Topico> topicosAderentes, List<Questao> questoes) throws ModelException {
        Tag tag = new Tag();
        tag.setId(dto.getId());
        tag.setAssunto(dto.getAssunto());
        tag.setTagName(dto.getTagName());

        if (topicosAderentes != null) {
            tag.setConjTopicosAderentes(topicosAderentes);
        };

        tag.setConjQuestoes(questoes != null ? questoes : List.of());


        return tag;
    }
}
