package com.projetoProvaTcc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data // Gera getters, setters, toString, equals e hashCode
@NoArgsConstructor // Gera construtor sem argumentos
@AllArgsConstructor //Gera construtor com argumentos
public class DisciplinaDTO {
    private int id;
    private String codigo;
    private String nome;
    private Integer numCreditos;
    private String objetivoGeral;

    //relaciona pelo id de topico
    private List<Integer> conjTopicos; // relacionamento bidirecional

}
