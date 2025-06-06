package com.projetoProvaTcc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data // Gera getters, setters, toString, equals e hashCode
@NoArgsConstructor // Gera construtor sem argumentos
@AllArgsConstructor //Gera construtor com argumentos
public class OpcaoDTO {
    private int id;
    private String conteudo;
    private Boolean correta;

    //relaciona pelo recurso
    private List<RecursoDTO> conjRecursos;

    public OpcaoDTO(int id) {
    }

}


