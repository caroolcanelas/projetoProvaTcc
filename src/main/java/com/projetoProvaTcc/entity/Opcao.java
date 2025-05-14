package com.projetoProvaTcc.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.*;
import com.projetoProvaTcc.exception.ModelException;

@Entity
public class Opcao { //TODO já conferido
	//
	// ATRIBUTOS
	//
    @Id @GeneratedValue
    private int     id;
    @Column(length = 300)
	private String  conteudo;
    @Column
	private boolean correta;

	//
	// ATRIBUTOS DE RELACIONAMENTO
	//    
    @OneToMany(fetch = FetchType.LAZY)
	private List<Recurso> conjRecursos;

	//
	// MÉTODOS
	//
    public Opcao() {
    }
    
	public Opcao(String conteudo, boolean correta) throws ModelException {
		super();
		this.setConteudo(conteudo);
		this.setCorreta(correta);

		this.conjRecursos = new ArrayList<>();

	}
	//id
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	//conteudo
	public String getConteudo() {
		return this.conteudo;
	}

	public void setConteudo(String conteudo) throws ModelException {
		Opcao.validarConteudo(conteudo);
		this.conteudo = conteudo;
	}

	//se é a respos correta
	public boolean isCorreta() {
		return this.correta;
	}

	public void setCorreta(boolean correta) throws ModelException {
		this.correta = correta;
	}

	//conteudo
	public static void validarConteudo(String conteudo) throws ModelException {
		if (conteudo == null || conteudo.length() == 0)
			throw new ModelException("A opção precisa ter conteúdo!");
	}

	//conjRecursos

	public Set<Recurso> getConjRecursos(){return new HashSet<Recurso>(this.conjRecursos);}

	public void setConjRecursos(Set<Recurso> conjRecursos) throws ModelException {
		Opcao.validarConjRecursos(conjRecursos);
		this.conjRecursos = (List<Recurso>) conjRecursos;
	}

	public static void validarConjRecursos(Set<Recurso> conjRecursos) throws ModelException {
		if(conjRecursos == null)
			throw new ModelException("O conjunto de recursos da questão não pode ser nulo!");
	}

	public static void validarRecurso (Recurso recurso) throws ModelException {
		if(recurso == null)
			throw new ModelException("O recurso não pode ser nulo");
	}

	//add e remove - conjRecursos

	public boolean addRecurso(Recurso recurso) throws ModelException{
		Opcao.validarRecurso(recurso);
		return this.conjRecursos.add(recurso);
	}

	public boolean removeRecurso(Recurso recurso) throws ModelException{
		return this.conjRecursos.remove(recurso);
	}

	@Override
	public String toString() {
		return "Opcao{" + "conteúdo ='" + this.conteudo + '\'' + ", valor=" + this.correta + "}";
	}
}
