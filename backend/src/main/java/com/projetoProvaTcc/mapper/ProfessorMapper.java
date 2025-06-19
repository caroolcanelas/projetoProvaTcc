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
        dto.setSenha(professor.getSenha());
        dto.setMatricula(professor.getMatricula());

        // Mapeia apenas os IDs das disciplinas
        if (professor.getConjDisciplinas() != null) {
            List<Integer> disciplinaIds = professor.getConjDisciplinas().stream()
                    .map(Disciplina::getId)
                    .collect(Collectors.toList());
            dto.setConjDisciplinas(disciplinaIds);
        }

        // Questões validadas não são incluídas no DTO básico
        // (são gerenciadas pelos endpoints específicos)

        return dto;
    }

    public static Professor toEntity(ProfessorDTO dto, List<Disciplina> disciplinas) throws ModelException {
        Professor professor = new Professor();
        professor.setId(dto.getId());
        professor.setNome(dto.getNome());
        professor.setEmail(dto.getEmail());
        professor.setSenha(dto.getSenha());
        professor.setMatricula(dto.getMatricula());

        // Associa disciplinas (se fornecidas)
        if (disciplinas != null) {
            professor.setConjDisciplinas(disciplinas);
            disciplinas.forEach(d -> d.setProfessor(professor));
        }

        // Questões validadas não são setadas na criação
        // (são gerenciadas pelos endpoints específicos)

        return professor;
    }
}