package com.projetoProvaTcc.dto;

import com.projetoProvaTcc.entity.Disciplina;
import com.projetoProvaTcc.entity.Questao;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfessorDTO {
    private Long id;
    private String nome;
    private String email;
    private Integer matricula;

    //relacionamento de professor com disciplina
    private List<Integer> conjDisciplinas;

}
