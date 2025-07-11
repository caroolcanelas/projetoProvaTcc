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
public class TopicoDTO {

    @Schema(hidden = true)
    private int id;

    private Integer numOrdem;
    private String nome;
    private String conteudo;

    //retornar apenas id de disciplina
    private Integer disciplina;

    //retornant apenas id de subtopico
    @Schema(hidden = true)
    private List<Integer> conjSubTopicos = new ArrayList<>(); //recursivo também :x

    //retornar o nome da tag
    @Schema(hidden = true)
    private List<String> conjTags= new ArrayList<>();
}
