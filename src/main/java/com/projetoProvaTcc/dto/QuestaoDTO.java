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
    private List<OpcaoDTO> conjOpcoes;
    //private List<QuestaoDTO> conjQuestoesDerivadas; //To em dúvida de como fazer esse aqui
    private List<RecursoDTO> conjRecursos = new ArrayList<>();
    //private List<TagDTO> conjTags;

}
