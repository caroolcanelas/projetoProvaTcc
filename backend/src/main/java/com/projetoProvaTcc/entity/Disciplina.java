package com.projetoProvaTcc.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import com.projetoProvaTcc.exception.ModelException;
import java.util.ArrayList;

import java.util.List;

@Entity
public class Disciplina { // TODO já conferido
	//
    // CONSTANTES
	//
    final public static int TAMANHO_MAX_CODIGO_DISCIPLINA = 14; // Atualizado para 2, se o código deve ter 2 dígitos
    final public static int TAMANHO_MINIMO_NOME = 5;
    final public static int TAMANHO_MAXIMO_NOME = 50;
    final public static int TAMANHO_OBJETIVO_GERAL = 300;
    final public static int MAX_NUM_CREDITOS = 8;

    //
    // ATRIBUTOS
    //
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //ajuda na ordem de inserção no banco. sequencial
    private int id;

    @Column(length = TAMANHO_MAX_CODIGO_DISCIPLINA, unique = true)
    private String codigo;

    @Column(length = TAMANHO_MAXIMO_NOME)
    private String nome;

    @Column
    private int numCreditos;

    @Column(length = TAMANHO_OBJETIVO_GERAL)
    private String objetivoGeral;

    //
    // ATRIBUTOS DE RELACIONAMENTO
    // 
    @OneToMany(mappedBy = "disciplina", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Topico> conjTopicos; // relacionamento bidirecional

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professor_id")
    private Professor professor;

    //relação com turma?

    // 
    // MÉTODOS
    //
    public Disciplina() {
        super();
    }
    
    public Disciplina(String codigo, String nome, int numCreditos, String objetivoGeral) throws ModelException {
    	super();
    	this.setCodigo(codigo);
        this.setNome(nome);
        this.setNumCreditos(numCreditos);
        this.setObjetivoGeral(objetivoGeral);
        this.conjTopicos = new ArrayList<>();
    }

    public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

   public String getCodigo() {
        return this.codigo;
    }

    public void setCodigo(String codigo) throws ModelException {
        Disciplina.validarCodigo(codigo);
        this.codigo = codigo;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) throws ModelException {
        Disciplina.validarNome(nome);
        this.nome = nome;
    }

    public int getNumCreditos() {
        return this.numCreditos;
    }

    public void setNumCreditos(int numCreditos) throws ModelException {
        Disciplina.validarNumCreditos(numCreditos);
        this.numCreditos = numCreditos;
    }

    public String getObjetivoGeral() {
    	return this.objetivoGeral;
    }
    
    public void setObjetivoGeral(String objetivo) throws ModelException {
        Disciplina.validarObjetivoGeral(objetivo);
        this.objetivoGeral = objetivo;
    }

    public List<Topico> getConjTopicos() {
    	// Retorno uma cópia do conjunto de tópicos
        return this.conjTopicos;
	}

	public void setConjTopicos(List<Topico> conjTopicos) throws ModelException {
		Disciplina.validarConjTopicos(conjTopicos);
		this.conjTopicos = (List<Topico>) conjTopicos;
	}

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

	public boolean addTopico(Topico topico) throws ModelException {
		Disciplina.validarTopico(topico);
		return this.conjTopicos.add(topico);
	}

	public boolean removeTopico(Topico topico) throws ModelException {
        boolean removed = this.conjTopicos.remove(topico);
        if (removed) {
            topico.setDisciplina(null);  // 🔥 ISSO É OBRIGATÓRIO!
        }
        return removed;
	}
	
	// Métodos de validação
    public static void validarNumCreditos(int numCreditos) throws ModelException {
        if(numCreditos < 0)
            throw new ModelException("A quantidade de creditos precisa ser maior ou igual a 0");
        if(numCreditos > MAX_NUM_CREDITOS)
            throw new ModelException("A quantidade de creditos não pode ultrapassar a " + MAX_NUM_CREDITOS);
    }

    public static void validarCodigo(String codigo) throws ModelException {
        if(codigo == null || codigo.length() == 0) 
            throw new ModelException("O código não pode ser nulo!");
        		
        if(codigo.length() > TAMANHO_MAX_CODIGO_DISCIPLINA)
            throw new ModelException("O código da disciplina não pode ser maior que " + TAMANHO_MAX_CODIGO_DISCIPLINA + " caracteres");
    }

    public static void validarNome(String nome) throws ModelException {
        if (nome == null || nome.length() == 0)
            throw new ModelException("O nome não pode ser nulo!");
        if (nome.length() < TAMANHO_MINIMO_NOME || nome.length() > TAMANHO_MAXIMO_NOME)
            throw new ModelException("O nome deve ter de " + TAMANHO_MINIMO_NOME + " a " +
                    TAMANHO_MAXIMO_NOME + " caracteres!");
        for (int i = 0; i < nome.length(); i++) {
            char c = nome.charAt(i);
            if( !Character.isAlphabetic(c) && !Character.isDigit(c) && !Character.isSpaceChar(c) && c != '\'')
                throw new ModelException("O caracter na posição " + i + " é inválido: " + c);
        }
    }

    public static void validarObjetivoGeral(String objetivo) throws ModelException {
    	if(objetivo == null || objetivo.length() == 0)
    		throw new ModelException("É necessário definir o conteúdo do objetivo geral da disciplina");
    }

    public static void validarConjTopicos(List<Topico> conjTopicos) throws ModelException {
    	if(conjTopicos == null)
    		throw new ModelException("O conjunto de tópicos não pode ser nulo");
    }

    public static void validarTopico(Topico topico) throws ModelException {
    	if(topico == null)
    		throw new ModelException("O tópico não pode ser nulo");
    }
    
    

    @Override
    public String toString() {
        return "Disciplina{" +
                "código ='" + this.codigo + '\'' +
                ", nome='" + this.nome + '\'' +
                ", creditos=" + this.numCreditos +
                '}';
    }
}