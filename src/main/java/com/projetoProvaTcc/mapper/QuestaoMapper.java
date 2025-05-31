package com.projetoProvaTcc.mapper;

import com.projetoProvaTcc.dto.OpcaoDTO;
import com.projetoProvaTcc.dto.QuestaoDTO;
import com.projetoProvaTcc.dto.RecursoDTO;
import com.projetoProvaTcc.dto.TagDTO;
import com.projetoProvaTcc.entity.*;
import com.projetoProvaTcc.exception.ModelException;
import lombok.SneakyThrows;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class QuestaoMapper {

    public static QuestaoDTO toDTO(Questao questao) {

        QuestaoDTO dto = new QuestaoDTO();
        dto.setId(questao.getId());
        dto.setTipo(questao.getTipo());
        dto.setSuporte(questao.getSuporte());
        dto.setComando(questao.getComando());
        dto.setNivel(questao.getNivel());
        dto.setValidada(questao.isValidada());
        dto.setInstrucaoInicial(questao.getInstrucaoInicial());

        //relacionamento com opção
        dto.setConjOpcoes(
                questao.getConjOpcoes().stream()
                        .map(o -> new OpcaoDTO(o.getId(), o.getConteudo(), o.isCorreta(), null))
                        .collect(Collectors.toList())
        );

        //relacionamento com recurso
        dto.setConjRecursos(
                questao.getConjRecursos().stream()
                        .map(r -> new RecursoDTO(r.getId(), r.getConteudo()))
                        .collect(Collectors.toList())
        );

        //relacionamento tag
        dto.setConjTags(
                questao.getConjTags().stream()
                        .map(t -> new TagDTO(t.getId(), t.getTagName(), t.getAssunto(), null, null))
                        .collect(Collectors.toList())
        );


        //relacionar com o id de questao derivada
        if (questao.getConjQuestoesDerivadas() != null) {
            dto.setConjQuestoesDerivadas(
                    questao.getConjQuestoesDerivadas().stream()
                            .map(Questao::getId)
                            .collect(Collectors.toList())
            );
        }

        return dto;

    }

    public static Questao toEntity(QuestaoDTO dto, List<Opcao> opcoes, List<Recurso> recursos, List<Tag> tags, List<Questao> questoesDerivadas) throws ModelException{
        Questao questao = new Questao();
        questao.setId(dto.getId());
        questao.setTipo(dto.getTipo());
        questao.setSuporte(dto.getSuporte());
        questao.setComando(dto.getComando());
        questao.setNivel(dto.getNivel());
        questao.setValidada(dto.isValidada());
        questao.setInstrucaoInicial(dto.getInstrucaoInicial());
        questao.setConjOpcoes(opcoes);
        questao.setConjRecursos(recursos);
        questao.setConjTags(tags);

        if (questoesDerivadas != null) {
            questao.setConjQuestoesDerivadas(questoesDerivadas);
        }

        return questao;
    }

}
