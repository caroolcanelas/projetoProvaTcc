package com.projetoProvaTcc.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Data // Gera getters, setters, toString, equals e hashCode
@NoArgsConstructor // Gera construtor sem argumentos
@AllArgsConstructor //Gera construtor com argumentos
public class DisciplinaDTO {

    @Schema(hidden = true)
    private int id;
    private String codigo;
    private String nome;
    private Integer numCreditos;
    private String objetivoGeral;

    //relaciona pelo id de topico
    @Schema(hidden = true)
    private List<Integer> conjTopicos = new ArrayList<>(); // relacionamento bidirecional

}
