package com.projetoProvaTcc.mapper;

import com.projetoProvaTcc.dto.DisciplinaDTO;
import com.projetoProvaTcc.entity.Disciplina;
import com.projetoProvaTcc.exception.ModelException;

public class DisciplinaMapper {

    public static DisciplinaDTO toDTO(Disciplina disciplina) {
        DisciplinaDTO dto = new DisciplinaDTO();
        dto.setId(disciplina.getId());
        dto.setNome(disciplina.getNome());
        dto.setCodigo(disciplina.getCodigo());
        dto.setNumCreditos(disciplina.getNumCreditos());
        dto.setObjetivoGeral(disciplina.getObjetivoGeral());

        return dto;
    }

    public static Disciplina toEntity(DisciplinaDTO dto) throws ModelException {
        Disciplina disciplina = new Disciplina();

        disciplina.setId(dto.getId());
        disciplina.setCodigo(dto.getCodigo());
        disciplina.setNome(dto.getNome());
        disciplina.setNumCreditos(dto.getNumCreditos());
        disciplina.setObjetivoGeral(dto.getObjetivoGeral());

        return disciplina;

    }
}
