package com.projetoProvaTcc.mapper;

import com.projetoProvaTcc.dto.OpcaoDTO;
import com.projetoProvaTcc.dto.RecursoDTO;
import com.projetoProvaTcc.entity.Opcao;
import com.projetoProvaTcc.entity.Recurso;
import com.projetoProvaTcc.exception.ModelException;

import java.util.List;
import java.util.stream.Collectors;

public class OpcaoMapper {

    public static OpcaoDTO toDTO(Opcao opcao) {
        OpcaoDTO dto = new OpcaoDTO();
        dto.setId(opcao.getId());
        dto.setConteudo(opcao.getConteudo());
        dto.setCorreta(opcao.isCorreta());
        dto.setConjRecursos(opcao.getConjRecursos()
                .stream()
                .map(recurso -> {
                    RecursoDTO o = new RecursoDTO();
                    o.setId(recurso.getId());
                    o.setConteudo(recurso.getConteudo());
                    return o;
                })
                .collect(Collectors.toList()));
        return dto;
    }

    public static Opcao toEntity(OpcaoDTO dto, List<Recurso> recursos) throws ModelException {
        Opcao opcao = new Opcao();
        opcao.setId(dto.getId());
        opcao.setConteudo(dto.getConteudo());
        opcao.setCorreta(dto.getCorreta());
        opcao.setConjRecursos(recursos);
        return opcao;
    }
}


