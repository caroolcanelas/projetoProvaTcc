package com.projetoProvaTcc.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.projetoProvaTcc.exception.ModelException;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Topico {//TODO já conferido
	//
	// CONSTANTES
	//
	final public static int TAMANHO_MAXIMO_NOME = 150;
	final public static int TAMANHO_CONTEUDO    = 300;
	final public static int NUM_ORDEM_MINIMO    = 1;
	final public static int NUM_ORDEM_MAXIMO    = 14;

	//
	// ATRIBUTOS
	//
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
	private int numOrdem;

    @Column(length = TAMANHO_MAXIMO_NOME)
	private String nome;

    @Column(length = TAMANHO_CONTEUDO)
	private String conteudo;

	//
	// ATRIBUTOS DE RELACIONAMENTO
	//
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "disciplina_id")
	@JsonBackReference
	private Disciplina disciplina; 		// relacionamento bidirecional

	@OneToMany(cascade = CascadeType.ALL)
	private List<Topico> conjSubTopicos; // relacionamento unidirecional
    
    @ManyToMany(mappedBy = "conjTopicosAderentes")
    private List<Tag> conjTags; // relacionamento birecional
        
	//
	// MÉTODOS
	//

	//pra não inicializar como null
    public Topico() {
		this.conjSubTopicos = new ArrayList<>();
		this.conjTags = new ArrayList<>();
    }
    
	public Topico(int numOrdem, String nome, String conteudo, Disciplina disciplina) throws ModelException {
		super();
		this.setNumOrdem(numOrdem);
		this.setNome(nome);
		this.setConteudo(conteudo);
		this.setDisciplina(disciplina);
	}

	//id
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	//numOrdem
	public int getNumOrdem() {
		return this.numOrdem;
	}

	public void setNumOrdem(int numOrdem) throws ModelException {
		Topico.validarNumOrdem(numOrdem);
		this.numOrdem = numOrdem;
	}

	//nome
	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) throws ModelException {
		Topico.validarNome(nome);
		this.nome = nome;
	}

	//conteudo
	public String getConteudo(){
		return this.conteudo;
	}

	public void setConteudo(String conteudo) throws ModelException {
		Topico.validarConteudo(conteudo);
		this.conteudo = conteudo;
	}

	//disciplina
	public Disciplina getDisciplina() {
		return this.disciplina;
	}

	public void setDisciplina(Disciplina disciplina) throws ModelException {
		Topico.validarDisciplina(disciplina);
		this.disciplina = disciplina;
	}

	//conjSubTopicos
	public List<Topico> getConjSubTopicos() {
		return this.conjSubTopicos;
	}

	public void setConjSubTopicos(List<Topico> conjSubTopicos) throws ModelException {
		Topico.validarConjSubTopicos(conjSubTopicos);
		this.conjSubTopicos = conjSubTopicos;
	}

	//conjSubTopicos - add e remove
	public boolean addSubTopico(Topico subTopico) throws ModelException {
		Topico.validarSubTopico(subTopico);
		return this.conjSubTopicos.add(subTopico);
	}

	public boolean removeSubTopico(Topico subTopico) throws ModelException {
		return this.conjSubTopicos.remove(subTopico);
	}

	//conjTags

	public List<Tag> getConjTags() {
		return this.conjTags;
	}

	public void setConjTags(List<Tag> conjTags) throws ModelException {
		if (conjTags == null) {
			throw new ModelException("O conjunto de tags não pode ser nulo!");
		}
		this.conjTags.clear();
		this.conjTags.addAll(conjTags);
	}

	//conjTags - add e remove

	public void addTag(Tag tag) throws ModelException {
		if (tag == null) {
			throw new ModelException("A tag não pode ser nula");
		}
		if (!this.conjTags.contains(tag)) {
			this.conjTags.add(tag);
			if (!tag.getConjTopicosAderentes().contains(this)) {
				tag.getConjTopicosAderentes().add(this);
			}
		}
	}

	public void removeTag(Tag tag) throws ModelException{
		if(tag == null) {
			throw new ModelException("A tag não pode ser nula");
		}
		this.conjTags.remove(tag);
		tag.getConjTopicosAderentes().remove(this);
	}

	//Validações
	public static void validarNumOrdem(int numOrdem) throws ModelException {
		if (numOrdem < NUM_ORDEM_MINIMO || numOrdem > NUM_ORDEM_MAXIMO) {
			throw new ModelException(
					"O número de ordem deve ser entre " + NUM_ORDEM_MINIMO + " e " + NUM_ORDEM_MAXIMO + ".");
		}
	}

	public static void validarNome(String nome) throws ModelException {
		if (nome == null || nome.length() == 0) {
			throw new ModelException("O nome não pode ser nulo!");
		}
		if (nome.length() > TAMANHO_MAXIMO_NOME) {
			throw new ModelException("O nome deve ter no máximo " + TAMANHO_MAXIMO_NOME + " caracteres!");
		}
	}

	private static void validarConteudo(String conteudo) throws ModelException {
		if (conteudo == null || conteudo.length() == 0) {
			throw new ModelException("O conteúdo não pode ser nulo!");
		}
	}

	public static void validarDisciplina(Disciplina disciplina) throws ModelException {
		if (disciplina == null)
			throw new ModelException("O tópico precisa estar vinculado a uma disciplina!");
	}

	public static void validarConjSubTopicos(List<Topico> conjSubTopicos) throws ModelException {
		if (conjSubTopicos == null)
			throw new ModelException("O conjunto de subtópicos não pode ser nulo!");
	}

	public static void validarSubTopico(Topico subTopico) throws ModelException {
		if (subTopico == null)
			throw new ModelException("O subtópico não pode ser nulo!");
	}

	@Override
	public String toString() {
		return "Número de Ordem: " + this.numOrdem + " - Nome: " + this.nome + " - Conteúdo: " + this.conteudo;
	}
}