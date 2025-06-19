package com.projetoProvaTcc.dto;

import com.projetoProvaTcc.entity.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Data // Gera getters, setters, toString, equals e hashCode
@NoArgsConstructor // Gera construtor sem argumentos
@AllArgsConstructor //Gera construtor com argumentos
public class QuestaoDTO {

    @Schema(hidden = true)
    private int id;

    private String instrucaoInicial;
    private String suporte;
    private String comando;
    private NivelQuestao nivel;
    private TipoQuestao tipo;
    private Boolean validada;


    //relaciona pela opcao
    @Schema(hidden = true)
    private List<OpcaoDTO> conjOpcoes= new ArrayList<>();

    @Schema(hidden = true)
    private List<Integer> conjQuestoesDerivadas =new ArrayList<>(); //To em dúvida de como fazer esse aqui

    //relaciona pelo recurso
    @Schema(hidden = true)
    private List<RecursoDTO> conjRecursos = new ArrayList<>();

    //relaciona pela tag
    @Schema(hidden = true)
    private List<TagDTO> conjTags = new ArrayList<>();

    //relacionamento com professor - tentando retornar apenas com a matricula
    private Integer matriculaProfessorValidador; // matrícula do professor validador

    //private Long matriculaProfessorValidador;

}
