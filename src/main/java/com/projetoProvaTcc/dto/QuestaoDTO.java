package com.projetoProvaTcc.dto;

import com.projetoProvaTcc.entity.NivelQuestao;
import com.projetoProvaTcc.entity.Questao;
import com.projetoProvaTcc.entity.Recurso;
import com.projetoProvaTcc.entity.TipoQuestao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    //private List<Questao> conjQuestoesDerivadas;
    //private List<Recurso> conjRecursos;

}
