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
public class TagDTO {

    @Schema(hidden = true)
    private int id;

    private String tagName;
    private String assunto;

    //relaciona pelo id da questao
    private List<Integer> conjQuestoes = new ArrayList<>();

    //relaciona pelo nome do topico pra deixar mais fácil
    private List<String> conjTopicosAderentes = new ArrayList<>();
}
