package com.projetoProvaTcc.dto;

import java.util.List;

public class TagDTO {
    private int id;
    private String tagName;
    private String assunto;
    private List<QuestaoDTO> conjQuestoes;
    private List<TopicoDTO> conjTopicosAderentes;

    public TagDTO(){}

    public TagDTO(int id, String tagName, String assunto, List<QuestaoDTO> conjQuestoes, List<TopicoDTO> conjTopicosAderentes){
        this.id = id;
        this.assunto = assunto;
        this.tagName = tagName;
        this.conjQuestoes = conjQuestoes;
        this.conjTopicosAderentes = conjTopicosAderentes;
    }

    //id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    //assunto
    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    //tagName
    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    //conjQuestoes
    public List<QuestaoDTO> getConjQuestoes() {
        return getConjQuestoes();
    }

    public void setConjQuestoes(List<QuestaoDTO> conjQuestoes) {
        this.conjQuestoes = conjQuestoes;
    }

    //conjTopicosAderentes
    public List<TopicoDTO> getConjTopicosAderentes() {
        return getConjTopicosAderentes();
    }

    public void setConjTopicosAderentes(List<TopicoDTO> conjTopicosAderentes) {
        this.conjTopicosAderentes = conjTopicosAderentes;
    }

}
