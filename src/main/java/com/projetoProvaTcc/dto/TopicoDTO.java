package com.projetoProvaTcc.dto;

import com.projetoProvaTcc.entity.Disciplina;
import com.projetoProvaTcc.entity.Tag;
import com.projetoProvaTcc.entity.Topico;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data // Gera getters, setters, toString, equals e hashCode
@NoArgsConstructor // Gera construtor sem argumentos
@AllArgsConstructor //Gera construtor com argumentos
public class TopicoDTO {
    private int id;
    private int numOrdem;
    private String nome;
    private String conteudo;

    //retornar apenas id de disciplina
    private Integer disciplina;

    //retornant apenas id de subtopico
    private List<Integer> conjSubTopicos; //recursivo também :x

    //retornar o nome da tag
    private List<String> conjTags;
}
