package com.projetoProvaTcc.mapper;

import com.projetoProvaTcc.dto.ProfessorDTO;
import com.projetoProvaTcc.entity.Professor;
import com.projetoProvaTcc.exception.ModelException;

public class ProfessorMapper {
    public static ProfessorDTO toDTO(Professor professor) {
        return new ProfessorDTO(
                professor.getId(),
                professor.getNome(),
                professor.getEmail(),
                professor.getSenha(),
                professor.getMatricula()
        );
    }

    public static Professor toEntity(ProfessorDTO dto) throws ModelException {
        return new Professor(
                dto.getNome(),
                dto.getEmail(),
                dto.getSenha(),
                dto.getMatricula()
        );
    }
}
