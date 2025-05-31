package com.projetoProvaTcc.mapper;

import com.projetoProvaTcc.dto.TagDTO;
import com.projetoProvaTcc.dto.TopicoDTO;
import com.projetoProvaTcc.entity.Disciplina;
import com.projetoProvaTcc.entity.Tag;
import com.projetoProvaTcc.entity.Topico;
import com.projetoProvaTcc.exception.ModelException;
import io.swagger.v3.oas.annotations.tags.Tags;

import java.util.List;
import java.util.stream.Collectors;

public class TopicoMapper {

    public static TopicoDTO toDTO(Topico topico) {
        TopicoDTO dto = new TopicoDTO();
        dto.setId(topico.getId());
        dto.setNumOrdem(topico.getNumOrdem());
        dto.setNome(topico.getNome());
        dto.setConteudo(topico.getConteudo());


        //confere se o id da discplina não é nulo e relaciona pelo id
        if (topico.getDisciplina() != null) {
            dto.setDisciplina(topico.getDisciplina().getId());
        }

        //confere se o id de sub topico não é nulo e relaciona pelo id
        if (topico.getConjSubTopicos() != null) {
            dto.setConjSubTopicos(
                    topico.getConjSubTopicos().stream()
                            .map(Topico::getId)
                            .collect(Collectors.toList())
            );
        }

        //confere se o nome da tag nao é nulo e relaciona pelo nome
        if (topico.getConjTags() != null) {
            dto.setConjTags(
                    topico.getConjTags().stream()
                            .map(Tag::getTagName)
                            .collect(Collectors.toList())
            );
        }


        return dto;

    }

    public static Topico toEntity(TopicoDTO dto, Disciplina disciplina, List<Topico> subTopicos, List<Tag> tags) throws ModelException {
        Topico topico = new Topico();
        topico.setId(dto.getId());
        topico.setNumOrdem(dto.getNumOrdem());
        topico.setNome(dto.getNome());
        topico.setConteudo(dto.getConteudo());

        if (disciplina != null) {
            topico.setDisciplina(disciplina);
        }

        if (subTopicos != null) {
            topico.setConjSubTopicos(subTopicos);
        }

        if (tags != null) {
            topico.setConjTags(tags);
        }

        return topico;
    }

    public static Topico toEntity(TopicoDTO dto) throws ModelException {
        return toEntity(dto, null, null, null);
    }

}
