package com.projetoProvaTcc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfessorDTO {
    private Long id;
    private String nome;
    private String email;
    private String senha;
    private int matricula;

    //relacionamento de professor com disciplina

    //relacinamento com questao valdiada
}
