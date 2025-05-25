package com.projetoProvaTcc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data // Gera getters, setters, toString, equals e hashCode
@NoArgsConstructor // Gera construtor sem argumentos
@AllArgsConstructor //Gera construtor com argumentos
public class TagDTO {
    private int id;
    private String tagName;
    private String assunto;
    //private List<QuestaoDTO> conjQuestoes;
    //private List<TopicoDTO> conjTopicosAderentes;
}
