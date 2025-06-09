package com.projetoProvaTcc.mapper;

import com.projetoProvaTcc.dto.DisciplinaDTO;
import com.projetoProvaTcc.entity.Disciplina;

import com.projetoProvaTcc.entity.Topico;
import com.projetoProvaTcc.exception.ModelException;

import java.util.List;
import java.util.stream.Collectors;

public class DisciplinaMapper {

    public static DisciplinaDTO toDTO(Disciplina disciplina) {
        DisciplinaDTO dto = new DisciplinaDTO();
        dto.setId(disciplina.getId());
        dto.setNome(disciplina.getNome());
        dto.setCodigo(disciplina.getCodigo());
        dto.setNumCreditos(disciplina.getNumCreditos());
        dto.setObjetivoGeral(disciplina.getObjetivoGeral());

        //s o id não for nulo ele adiciona o topico com id correspontente
        if (disciplina.getConjTopicos() != null) {
            List<Integer> ids = disciplina.getConjTopicos().stream()
                    .map(Topico::getId)
                    .collect(Collectors.toList());
            dto.setConjTopicos(ids);
        }
        return dto;
    }

    public static Disciplina toEntity(DisciplinaDTO dto, List<Topico> topicos) throws ModelException {
        Disciplina disciplina = new Disciplina();
        {

            disciplina.setId(dto.getId());
            disciplina.setCodigo(dto.getCodigo());
            disciplina.setNome(dto.getNome());
            disciplina.setNumCreditos(dto.getNumCreditos());
            disciplina.setObjetivoGeral(dto.getObjetivoGeral());
        };

        disciplina.setConjTopicos(topicos);

        return disciplina;

    }
}
