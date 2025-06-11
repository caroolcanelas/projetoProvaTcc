package com.projetoProvaTcc.mapper;

import com.projetoProvaTcc.dto.ProfessorDTO;
import com.projetoProvaTcc.entity.Disciplina;
import com.projetoProvaTcc.entity.Professor;
import com.projetoProvaTcc.exception.ModelException;

import java.util.List;
import java.util.stream.Collectors;

public class ProfessorMapper {

    public static ProfessorDTO toDTO(Professor professor) {
        ProfessorDTO dto = new ProfessorDTO();
        dto.setId(professor.getId());
        dto.setNome(professor.getNome());
        dto.setEmail(professor.getEmail());
        dto.setMatricula(professor.getMatricula());

        if (professor.getConjDisciplinas() != null) {
            List<Long> ids = professor.getConjDisciplinas().stream()
                    .map(Disciplina::getId)
                    .collect(Collectors.toList());
            dto.setConjDisciplinas(ids);
        }
        return dto;
    }


    public static Professor toEntity(ProfessorDTO dto, List<Disciplina> disciplinas) throws ModelException {
        Professor professor = new Professor();
        professor.setId(dto.getId());
        professor.setNome(dto.getNome());
        professor.setEmail(dto.getEmail());
        professor.setMatricula(dto.getMatricula());

        if (disciplinas != null) {
            professor.setConjDisciplinas(disciplinas);
            for (Disciplina d : disciplinas) {
                d.setProfessor(professor);
            }
        }

        return professor;
    }
}
