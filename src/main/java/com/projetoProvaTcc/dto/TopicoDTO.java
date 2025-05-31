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

    //private Disciplina disciplina;
    //private List<Topico> conjSubTopicos; //recursivo também :x
    //private List<TagDTO> conjTags;
}
