package model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity
public class Tag { //TODO já conferido
	//
	// CONSTANTES
	//
	final public static int TAMANHO_TAGNAME = 15;
	final public static int NUM_ORDEM_MINIMO = 1;
	final public static int NUM_ORDEM_MAXIMO = 14;
	final public static int TAMANHO_ASSUNTO = 40;

	//
	// ATRIBUTOS
	//
    @Id @GeneratedValue
    private int    id;
    @Column(length = TAMANHO_TAGNAME)
	private String tagName;
    @Column(length = TAMANHO_ASSUNTO)
	private String assunto;

	//
	// ATRIBUTOS DE RELACIONAMENTO
	//
    
    // TODO Já Conferido com Questão
    @ManyToMany(mappedBy = "conjTags") 
	private Set<Questao> conjQuestoes; // relacionamento bidirecional

    
    @ManyToMany(fetch = FetchType.LAZY) 
    @JoinTable(name = "esta_aderente_a", 
               joinColumns = @JoinColumn(name = "id_topico"),
               inverseJoinColumns = @JoinColumn(name = "id_tag"))
    private Set<Topico>   conjTopicosAderentes;
    

	//
	// MÉTODOS
	//
    public Tag() {    	
    }
    
	public Tag(String tagName, String assunto) throws ModelException {
		super();
		this.setTagName(tagName);
		this.setAssunto(assunto);
		this.conjTopicosAderentes = new HashSet<Topico>();
		this.conjQuestoes = new HashSet<Questao>();
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAssunto() {
		return this.assunto;
	}

	public void setAssunto(String assunto) throws ModelException {
		Tag.validarAssunto(assunto);
		this.assunto = assunto;
	}

	public String getTagName() {
		return this.tagName;
	}

	public void setTagName(String tagName) throws ModelException {
		Tag.validarTagName(tagName);
		this.tagName= tagName;
	}

	public Set<Questao> getConjQuestoes() {
		// Retorno uma cópia do conjunto de questões
		return new HashSet<Questao>(this.conjQuestoes);
	}

	public void setConjQuestoes(Set<Questao> conjQuestoes) throws ModelException {
		Tag.validarConjQuestoes(conjQuestoes);
		this.conjQuestoes = conjQuestoes;
	}

	public boolean addQuestao(Questao questao) throws ModelException {
		Tag.validarQuestao(questao);
		return this.conjQuestoes.add(questao);
	}

	public boolean removeSubTopico(Questao questao) throws ModelException {
		return this.conjQuestoes.remove(questao);
	}

	public static void validarNumOrdem(int numOrdem) throws ModelException {
		if (numOrdem < NUM_ORDEM_MINIMO || numOrdem > NUM_ORDEM_MAXIMO) {
			throw new ModelException(
					"O número de ordem deve ser entre " + NUM_ORDEM_MINIMO + " e " + NUM_ORDEM_MAXIMO + ".");
		}
	}

	public static void validarTagName(String tagName) throws ModelException {
		if (tagName == null || tagName.length() == 0) {
			throw new ModelException("A TagName não pode ser nula!");
		}
		if (tagName.length() > TAMANHO_TAGNAME) {
			throw new ModelException("A TagName deve ter no máximo " + TAMANHO_TAGNAME + " caracteres!");
		}
	}

	public static void validarAssunto(String assunto) throws ModelException {
		if (assunto == null || assunto.length() == 0) {
			throw new ModelException("O assunto não pode ser nulo!");
		}
		if (assunto.length() > TAMANHO_ASSUNTO) {
			throw new ModelException("O conteudo deve ter no máximo " + TAMANHO_ASSUNTO + " caracteres!");
		}
	}

	public static void validarQuestao(Questao questao) throws ModelException {
		if (questao == null)
			throw new ModelException("A questão não pode ser nula!");
	}

	public static void validarConjQuestoes(Set<Questao> conjQuestoes) throws ModelException {
		if (conjQuestoes == null)
			throw new ModelException("O conjunto de questões não pode ser nulo!");
	}

	@Override
	public String toString() {
		return "#" + this.tagName;
	}
}