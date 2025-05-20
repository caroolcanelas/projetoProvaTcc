package com.projetoProvaTcc.mapper;

import com.projetoProvaTcc.dto.OpcaoDTO;
import com.projetoProvaTcc.dto.RecursoDTO;
import com.projetoProvaTcc.entity.Opcao;
import com.projetoProvaTcc.entity.Recurso;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class OpcaoMapper {

    public static OpcaoDTO toDTO(Opcao opcao) {
        List<RecursoDTO> recursoDTOs = opcao.getConjRecursos().stream()
                .map(r -> new RecursoDTO(r.getId()))
                .collect(Collectors.toList());

        return new OpcaoDTO(
                opcao.getId(),
                opcao.getConteudo(),
                opcao.isCorreta(),
                recursoDTOs
        );
    }

    public static Opcao toEntity(OpcaoDTO dto) {
        Opcao opcao = new Opcao();
        try {
            opcao.setId(dto.getId());
            opcao.setConteudo(dto.getConteudo());
            opcao.setCorreta(dto.isCorreta());

            if (dto.getConjRecursos() != null) {
                Set<Recurso> recursos = dto.getConjRecursos().stream().map(rdto -> {
                    Recurso r = new Recurso();
                    r.setId(rdto.getId());
                    return r;
                }).collect(Collectors.toSet());

                opcao.setConjRecursos(recursos);
            }

        } catch (Exception e) {
            // Logar ou relançar exceção conforme necessidade
        }

        return opcao;
    }
}

