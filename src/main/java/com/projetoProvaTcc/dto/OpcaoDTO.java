package com.projetoProvaTcc.dto;

import java.util.List;

public class OpcaoDTO {
    private int id;
    private String conteudo;
    private boolean correta;
    private List<RecursoDTO> conjRecursos;

    public OpcaoDTO() {}

    public OpcaoDTO(int id, String conteudo, boolean correta, List<RecursoDTO> conjRecursos) {
        this.id = id;
        this.conteudo = conteudo;
        this.correta = correta;
        this.conjRecursos = conjRecursos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public boolean isCorreta() {
        return correta;
    }

    public void setCorreta(boolean correta) {
        this.correta = correta;
    }

    public List<RecursoDTO> getConjRecursos() {
        return conjRecursos;
    }

    public void setConjRecursos(List<RecursoDTO> conjRecursos) {
        this.conjRecursos = conjRecursos;
    }
}


