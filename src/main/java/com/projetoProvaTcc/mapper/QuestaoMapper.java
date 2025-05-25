package com.projetoProvaTcc.mapper;

import com.projetoProvaTcc.dto.OpcaoDTO;
import com.projetoProvaTcc.dto.QuestaoDTO;
import com.projetoProvaTcc.dto.RecursoDTO;
import com.projetoProvaTcc.entity.Opcao;
import com.projetoProvaTcc.entity.Questao;
import com.projetoProvaTcc.entity.Recurso;
import com.projetoProvaTcc.exception.ModelException;

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
        dto.setConjOpcoes(questao.getConjOpcoes()
                .stream()
                .map(opcao -> {
                    OpcaoDTO o = new OpcaoDTO();
                    o.setId(opcao.getId());
                    o.setConteudo(opcao.getConteudo());
                    o.setCorreta(opcao.isCorreta());
                    return o;
                })
                .collect(Collectors.toList()));

        //relacionamento com recurso
        dto.setConjRecursos(questao.getConjRecursos()
                .stream()
                .map(recurso -> {
                    RecursoDTO o = new RecursoDTO();
                    o.setId(recurso.getId());
                    o.setConteudo(recurso.getConteudo());
                    return o;
                })
                .collect(Collectors.toList()));

        //relacionamento com questao
//        dto.setConjQuestoesDerivadas(
//                questao.getConjQuestoesDerivadas()
//                        .stream()
//                        .map(q -> {
//                            QuestaoDTO qdto = new QuestaoDTO();
//                            qdto.setId(q.getId());
//                            qdto.setTipo(q.getTipo());
//                            qdto.setSuporte(q.getSuporte());
//                            qdto.setComando(q.getComando());
//                            qdto.setNivel(q.getNivel());
//                            qdto.setValidada(q.isValidada());
//                            qdto.setInstrucaoInicial(q.getInstrucaoInicial());
//
//                            return qdto;
//                        })
//                        .collect(Collectors.toList())
//        );

        return dto;

    }

    public static Questao toEntity(QuestaoDTO dto, List<Opcao> opcoes, List<Recurso> recursos) throws ModelException {
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
        //questao.setConjQuestoesDerivadas(questoes);

        return questao;
    }

}
