package com.projetoProvaTcc.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfessorDTO {

    @Schema(hidden = true)
    private Long id;

    private String nome;
    private String email;
    private String senha;
    private Integer matricula;


    //relacionamento de professor com disciplina
    private List<Integer> conjDisciplinas = new ArrayList<>();;

}
