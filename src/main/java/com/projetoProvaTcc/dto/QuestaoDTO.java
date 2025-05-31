package com.projetoProvaTcc.dto;

import com.projetoProvaTcc.entity.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Data // Gera getters, setters, toString, equals e hashCode
@NoArgsConstructor // Gera construtor sem argumentos
@AllArgsConstructor //Gera construtor com argumentos
public class QuestaoDTO {

    private int id;
    private String instrucaoInicial;
    private String suporte;
    private String comando;
    private NivelQuestao nivel;
    private TipoQuestao tipo;
    private boolean validada;

    //relaciona pela opcao
    private List<OpcaoDTO> conjOpcoes;

    private List<Integer> conjQuestoesDerivadas; //To em dúvida de como fazer esse aqui

    //relaciona pelo recurso
    private List<RecursoDTO> conjRecursos = new ArrayList<>();

    //relaciona pela tag
    private List<TagDTO> conjTags;

}
