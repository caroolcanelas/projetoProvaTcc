package com.projetoProvaTcc.entity;

import com.projetoProvaTcc.exception.ModelException;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

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
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = TAMANHO_INSTRUCAO_INICIAL, name = "instrucao_inicial")
	private String instrucaoInicial;

    @Column(length = TAMANHO_SUPORTE)
	private String suporte;

    @Column(length = TAMANHO_COMANDO)
	private String comando;

    @Enumerated(EnumType.STRING)
	private NivelQuestao nivel;

    @Enumerated(EnumType.STRING)
	private TipoQuestao tipo;

    @Column
	private boolean validada;
	
	// 
	// ATRIBUTOS DE RELACIONAMENTO
	//
    @ManyToMany(fetch = FetchType.LAZY) 
    @JoinTable(name = "foi_marcada_com",
			joinColumns = @JoinColumn(name = "id_questao"),
			inverseJoinColumns = @JoinColumn(name = "id_tag"))
    private List<Tag> conjTags;

	@OneToMany
	private List<Questao> conjQuestoesDerivadas; // relacionamento unidirecional

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Opcao> conjOpcoes ;            // relacionamento unidirecional

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Recurso> conjRecursos;

	//
	// MÉTODOS
	// 
	public Questao() {
		super();
		this.conjTags = new ArrayList<>();
		this.conjQuestoesDerivadas = new ArrayList<>();
		this.conjOpcoes = new ArrayList<>();
		this.conjRecursos = new ArrayList<>();
	}


	public Questao(String instrucaoInicial, String suporte, String comando,
				   NivelQuestao nivel, TipoQuestao tipo, boolean validada) throws ModelException {
		this(); // Chama o construtor padrão para inicializar as coleções
		this.setInstrucaoInicial(instrucaoInicial);
		this.setSuporte(suporte);
		this.setComando(comando);
		this.setNivel(nivel);
		this.setTipo(tipo);
		this.setValidada(validada);
	}
	
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	//instrução
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

	//suporte
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

	//comando
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

	//nivel questao
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

	//tipo questao
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

	//se é validada
	public boolean isValidada() {
		return this.validada;
	}

	public void setValidada(boolean validada) {
		this.validada = validada;
	}

	//conjTag - add e remove

	public List<Tag> getConjTags() {
		return new ArrayList<>(this.conjTags);
	}

	public void setConjTags(List<Tag> conjTags) throws ModelException {
		if (conjTags == null) {
			throw new ModelException("O conjunto de tags não pode ser nulo!");
		}
		this.conjTags.clear();
		this.conjTags.addAll(conjTags);
	}

	public void addTag(Tag tag) throws ModelException{
		if (tag == null) {
			throw new ModelException("A tag não pode ser nula");
		}
		if (!this.conjTags.contains(tag)) {
			this.conjTags.add(tag);
		}
	}

	public void removeTag(Tag tag) throws ModelException{
		if(tag == null) {
			throw new ModelException("A tag não pode ser nula");
		}
		this.conjTags.remove(tag);
	}

	//questoesDerivadas -- add e remove
	public boolean addQuestaoDerivada(Questao questao) throws ModelException{
		Questao.validarQuestao(questao);
		if (this.conjQuestoesDerivadas.contains(questao)) { // Evita duplicatas
			return false;
		}
		return this.conjQuestoesDerivadas.add(questao);
	}

	public boolean removeQuestaoDerivada(Questao questao) throws ModelException{
		return this.conjQuestoesDerivadas.remove(questao);
	}

	// questoesDerivadas
	public List<Questao> getConjQuestoesDerivadas() {
		return new ArrayList<>(this.conjQuestoesDerivadas); // Retorna uma cópia
	}

	public void setConjQuestoesDerivadas(List<Questao> conjQuestoesDerivadas) throws ModelException {
		Questao.validarConjQuestoesDerivadas(conjQuestoesDerivadas);
		this.conjQuestoesDerivadas.clear();
		this.conjQuestoesDerivadas.addAll(conjQuestoesDerivadas);
	}

	public static void validarConjQuestoesDerivadas(List<Questao> conjQuestoesDerivadas) throws ModelException {
		if(conjQuestoesDerivadas == null)
			throw new ModelException("O conjunto de questões derivadas da questão não pode ser nulo!");
	}

	public static void validarQuestao (Questao questao) throws ModelException {
		if(questao == null)
			throw new ModelException("A questão não pode ser nulo");
	}

	//add e remove - conjOpcoes

	public boolean addOpcao(Opcao opcao) throws ModelException{
		Questao.validarOpcao(opcao);
		if (this.conjOpcoes.contains(opcao)) { // Evita duplicatas
			return false;
		}
		return this.conjOpcoes.add(opcao);
	}

	public boolean removeOpcao(Opcao opcao) throws ModelException{
		return this.conjOpcoes.remove(opcao);
	}

	// conjOpcoes
	public List<Opcao> getConjOpcoes() {
		return new ArrayList<>(this.conjOpcoes); // Retorna uma cópia
	}

	public void setConjOpcoes(List<Opcao> conjOpcoes) throws ModelException {
		Questao.validarConjOpcoes(conjOpcoes);
		this.conjOpcoes.clear();
		this.conjOpcoes.addAll(conjOpcoes);
	}

	public static void validarConjOpcoes(List<Opcao> conjOpcoes) throws ModelException {
		if(conjOpcoes == null)
			throw new ModelException("O conjunto de opções da questão não pode ser nulo!");
	}

	public static void validarOpcao (Opcao opcao) throws ModelException {
		if(opcao == null)
			throw new ModelException("A opção não pode ser nulo");
	}

	//add e remove - conjRecursos

	public boolean addRecurso(Recurso recurso) throws ModelException{
		Questao.validarRecurso(recurso);
		if (this.conjRecursos.contains(recurso)) { // Evita duplicatas
			return false;
		}
		return this.conjRecursos.add(recurso);
	}

	public boolean removeRecurso(Recurso recurso) throws ModelException{
		return this.conjRecursos.remove(recurso);
	}

	//conjRecursos

	public List<Recurso> getConjRecursos(){
		return new ArrayList<>(this.conjRecursos); // Retorna uma cópia
	}

	public void setConjRecursos(List<Recurso> conjRecursos) throws ModelException {
		Questao.validarConjRecursos(conjRecursos);
		this.conjRecursos.clear();
		this.conjRecursos.addAll(conjRecursos);
	}

	public static void validarConjRecursos(List<Recurso> conjRecursos) throws ModelException {
		if(conjRecursos == null)
			throw new ModelException("O conjunto de recursos da questão não pode ser nulo!");
	}

	public static void validarRecurso (Recurso recurso) throws ModelException {
		if(recurso == null)
			throw new ModelException("O recurso não pode ser nulo");
	}


	public void validarQuestao() throws ModelException {
		if (this.conjOpcoes == null || this.conjOpcoes.isEmpty()) {
			throw new ModelException("A questão deve conter pelo menos uma opção.");
		}

		boolean temCorreta = this.conjOpcoes.stream().anyMatch(Opcao::isCorreta);
		if (!temCorreta) {
			throw new ModelException("A questão deve conter pelo menos uma opção correta.");
		}

		if (this.conjTags == null || this.conjTags.isEmpty()) {
			throw new ModelException("A questão deve conter pelo menos uma tag.");
		}

		if (this.conjRecursos == null) {
			throw new ModelException("A lista de recursos não pode ser nula.");
		}

		if (this.conjQuestoesDerivadas == null) {
			throw new ModelException("A lista de questões derivadas não pode ser nula.");
		}
	}
	
}
