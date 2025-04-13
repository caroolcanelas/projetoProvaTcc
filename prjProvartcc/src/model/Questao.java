package model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Entity
public class Questao { //TODO já conferido
	//
	// CONSTANTES
	//
	final public static int TAMANHO_INSTRUCAO_INICIAL = 500;
	final public static int TAMANHO_SUPORTE = 1024;
	final public static int TAMANHO_COMANDO = 500;

	//
	// ATRIBUTOS
	//
    @Id @GeneratedValue
    private int           id;
    @Column(length = TAMANHO_INSTRUCAO_INICIAL, name = "instrucao_inicial")
	private String 		  instrucaoInicial;
    @Column(length = TAMANHO_SUPORTE)
	private String 		  suporte;
    @Column(length = TAMANHO_COMANDO)
	private String 		  comando;
    @Enumerated(EnumType.STRING)
	private NivelQuestao  nivel;
    @Enumerated(EnumType.STRING)
	private TipoQuestao	  tipo;
    @Column
	private boolean       validada;
	
	// 
	// ATRIBUTOS DE RELACIONAMENTO
	//
    @ManyToMany(fetch = FetchType.LAZY) 
    @JoinTable(name = "foi_marcada_com", 
               joinColumns = @JoinColumn(name = "id_tag"),
               inverseJoinColumns = @JoinColumn(name = "id_questao"))
    private Set<Tag>   conjTags;
    
    @OneToMany(fetch = FetchType.LAZY)
	private Set<Questao>  conjQuestoesDerivadas; // relacionamento unidirecional
	
    @OneToMany(fetch = FetchType.LAZY)
    private Set<Opcao>    conjOpcoes; 			 // relacionamento unidirecional

    @OneToMany(fetch = FetchType.LAZY)
    private Set<Recurso>  conjRecursos; 			 

	//
	// MÉTODOS
	// 
	public Questao() {
		super();
	}

	public Questao(String instrucaoInicial, String suporte, String comando,
				   NivelQuestao nivel, TipoQuestao tipo, boolean validada) throws ModelException {
		super();
		this.setInstrucaoInicial(instrucaoInicial);
		this.setSuporte(suporte);
		this.setComando(comando);
		this.setNivel(nivel);
		this.setTipo(tipo);
		this.setValidada(validada);

		this.conjTags = new HashSet<>();
		this.conjQuestoesDerivadas = new HashSet<>();
		this.conjOpcoes = new HashSet<>();
		this.conjRecursos = new HashSet<>();

	}
	
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getInstrucaoInicial() {
		return this.instrucaoInicial;
	}

	public void setInstrucaoInicial(String instrucaoInicial) throws ModelException {
		Questao.validarInstrucaoInicial(instrucaoInicial);
		this.instrucaoInicial = instrucaoInicial;
	}
	
	public static void validarInstrucaoInicial(String instrucaoInicial) throws ModelException {
		if(instrucaoInicial == null || instrucaoInicial.length() == 0)
			throw new ModelException("A instrução inicial não pode ser nula!");
	}

	public String getSuporte() {
		return this.suporte;
	}

	public void setSuporte(String suporte) throws ModelException {
		Questao.validarSuporte(suporte);
		this.suporte = suporte;
	}

	public static void validarSuporte(String suporte) throws ModelException {
		if(suporte == null || suporte.length() == 0)
			throw new ModelException("O suporte não pode ser nulo!");
	}

	public String getComando() {
		return this.comando;
	}

	public void setComando(String comando) throws ModelException {
		Questao.validarComando(comando);
		this.comando = comando;
	}

	public static void validarComando(String comando) throws ModelException {
		if(comando == null || comando.length() == 0)
			throw new ModelException("O comando não pode ser nulo!");
	}

	public NivelQuestao getNivel() {
		return this.nivel;
	}

	public void setNivel(NivelQuestao nivel) throws ModelException {
		Questao.validarNivel(nivel);
		this.nivel = nivel;
	}

	public static void validarNivel(NivelQuestao nivel) throws ModelException {
		if(nivel == null)
			throw new ModelException("O nível da questão não pode ser nulo!");
	}

	public TipoQuestao getTipo() {
		return this.tipo;
	}

	public void setTipo(TipoQuestao tipo) throws ModelException {
		Questao.validarTipo(tipo);
		this.tipo = tipo;
	}

	public static void validarTipo(TipoQuestao tipo) throws ModelException {
		if(tipo == null)
			throw new ModelException("O tipo da questão não pode ser nulo!");
	}

	public boolean isValidada() {
		return this.validada;
	}

	public void setValidada(boolean validada) {
		this.validada = validada;
	}

	public Set<Questao> getConjQuestoesDerivadas() {
		return new HashSet<Questao>(this.conjQuestoesDerivadas);
	}

	public void setConjQuestoesDerivadas(Set<Questao> conjQuestoesDerivadas) throws ModelException {
		Questao.validarConjQuestoesDerivadas(conjQuestoesDerivadas);
		this.conjQuestoesDerivadas = conjQuestoesDerivadas;
	}

	public static void validarConjQuestoesDerivadas(Set<Questao> conjQuestoesDerivadas) throws ModelException {
		if(conjQuestoesDerivadas == null)
			throw new ModelException("O conjunto de questões derivadas questão não pode ser nulo!");
	}

	public Set<Opcao> getConjOpcoes() {
		return new HashSet<Opcao>(this.conjOpcoes);
	}

	public void setConjOpcoes(Set<Opcao> conjOpcoes) throws ModelException {
		Questao.validarConjOpcoes(conjOpcoes);		
		this.conjOpcoes = conjOpcoes;
	}
	
	public static void validarConjOpcoes(Set<Opcao> conjOpcoes) throws ModelException {
		if(conjOpcoes == null)
			throw new ModelException("O conjunto de opções da questão não pode ser nulo!");
	}

	public void validarQuestao() throws ModelException {
		// TODO Codificar esse método depois		
	}
	
}
