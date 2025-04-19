package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

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
    @Id @GeneratedValue
    private int         id;
    @Column
	private int    numOrdem;
    @Column(length = TAMANHO_MAXIMO_NOME)
	private String nome;
    @Column(length = TAMANHO_CONTEUDO)
	private String conteudo;

	//
	// ATRIBUTOS DE RELACIONAMENTO
	//
    @ManyToOne(fetch = FetchType.LAZY)
	private Disciplina disciplina; 		// relacionamento bidirecional
    
    @OneToMany
	private List<Topico> conjSubTopicos; // relacionamento unidirecional
    
    @ManyToMany(mappedBy = "conjTopicosAderentes")
    private List<Tag> conjTags; // relacionamento birecional
        
	//
	// MÉTODOS
	//
    public Topico() {
    }
    
	public Topico(int numOrdem, String nome, String conteudo, Disciplina disciplina) throws ModelException {
		super();
		this.setNumOrdem(numOrdem);
		this.setNome(nome);
		this.setConteudo(conteudo);
		this.setDisciplina(disciplina);

		this.conjSubTopicos = new ArrayList<>();
		this.conjTags = new ArrayList<>();
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

	private void setConteudo(String conteudo) throws ModelException {
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
	public Set<Topico> getConjSubTopicos() {
		// Retorno uma cópia do conjunto de subtópicos
		return new HashSet<Topico>(this.conjSubTopicos);
	}

	public void setConjSubTopicos(Set<Topico> conjSubTopicos) throws ModelException {
		Topico.validarConjSubTopicos(conjSubTopicos);
		this.conjSubTopicos = (List<Topico>) conjSubTopicos;
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

	public void addTag(Tag tag) throws ModelException{
		if (tag == null) {
			throw new ModelException("A tag não pode ser nula");
		}
		this.conjTags.add(tag);
	}

	public void removeTag(Tag tag) throws ModelException{
		if(tag == null) {
			throw new ModelException("A tag não pode ser nula");
		}
		this.conjTags.remove(tag);
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

	public static void validarConjSubTopicos(Set<Topico> conjSubTopicos) throws ModelException {
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