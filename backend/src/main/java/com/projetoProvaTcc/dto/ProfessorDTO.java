package com.projetoProvaTcc.dto;

import com.projetoProvaTcc.entity.Disciplina;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
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
    private String senha; //precisa ainda ?
    private int matricula;

    //relacionamento de professor com disciplina
    private List<Long> conjDisciplinas;

    //relacinamento com questao validado
}
