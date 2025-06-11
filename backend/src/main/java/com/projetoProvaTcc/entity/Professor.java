package com.projetoProvaTcc.entity;

import com.projetoProvaTcc.exception.ModelException;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity
public class Professor {
	//
	// CONSTANTES
	//
	final public static int TAMANHO_MINIMO_NOME = 5;
	final public static int TAMANHO_MAXIMO_NOME = 40;
	final public static int TAMANHO_MAXIMO_EMAIL = 100;
	final public static int TAMANHO_MAXIMO_MATR = 10;
	final public static int TAMANHO_MAXIMO_SENHA = 10;


	final private static String _REGEX_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
	                                         + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	final private static Pattern PATTERN_EMAIL = Pattern.compile(_REGEX_EMAIL, Pattern.CASE_INSENSITIVE);

	//
	// ATRIBUTOS
	//
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Getter
	@Column(nullable = false, unique = true, length = TAMANHO_MAXIMO_MATR)
	private int matricula;

	@Getter
    @Column(nullable = false, length = TAMANHO_MAXIMO_NOME)
	private String nome;

	@Getter
    @Column(nullable = false, unique = true, length = TAMANHO_MAXIMO_EMAIL)
	private String email;

	@Getter
	@Column(nullable = false, length = TAMANHO_MAXIMO_SENHA)
	private String senha;

	@OneToMany(mappedBy = "professor", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Disciplina> conjDisciplinas ;  // relacionamento unidirecional

	//uma questão pode ser validada por um professor, e um professor pode validar várias questões.
	@OneToMany(mappedBy = "professorValidador")
	private List<Questao> conjQuestoesValidadas = new ArrayList<>();

	//
	// MÉTODOS
	//

	public Professor() {
		super();
		this.conjQuestoesValidadas = new ArrayList<>();
		this.conjDisciplinas = new ArrayList<>();
	}

	public Professor(String nome, String email, String senha, int matricula) throws ModelException {
		this();
		this.setMatricula(matricula);
		this.setNome(nome);
		this.setEmail(email);
		this.setSenha(senha);
	}


	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setMatricula(int matr) throws ModelException {
		Professor.validarMatr(matr);
		this.matricula = matr;
	}

	public void setNome(String nome) throws ModelException {
		Professor.validarNome(nome);
		this.nome = nome;
	}

    public void setEmail(String email) throws ModelException {
		Professor.validarEmail(email);
		this.email = email;
	}

	private void setSenha(String senha) throws ModelException {
		Professor.validarSenha(senha);
		this.senha = senha;
	}

	//conjDisciplinas
	public List<Disciplina> getConjDisciplinas(){
		return conjDisciplinas;
	}

	public void setConjDisciplinas(List<Disciplina> conjDisciplinas){
		this.conjDisciplinas = conjDisciplinas;
	}

	//conjQuestoesValidadas
	public List<Questao> getConjQuestoesValidadas() {
		return conjQuestoesValidadas;
	}

	public void setConjQuestoesValidadas(List<Questao> conjQuestoesValidadas) {
		this.conjQuestoesValidadas = conjQuestoesValidadas;
	}

	// add e remove disciplina de professor

	public void addDisciplina(Disciplina disciplina) {
		this.conjDisciplinas.add(disciplina);
		disciplina.setProfessor(this);
	}

	public void removeDisciplina(Disciplina disciplina) {
		this.conjDisciplinas.remove(disciplina);
		disciplina.setProfessor(null);
	}


	// Métodos de validação
	public static void validarMatr(int numMatr) throws ModelException {
		if(numMatr < 0)
			throw new ModelException("A quantidade de matricula precisa ser maior ou igual a 0");
		if(String.valueOf(numMatr).length() > TAMANHO_MAXIMO_MATR)
			throw new ModelException("A quantidade de caracteres não pode ultrapassar a " + TAMANHO_MAXIMO_MATR);
	}

	public static void validarNome(String nome) throws ModelException {
		if (nome == null || nome.length() == 0)
			throw new ModelException("O nome não pode ser nulo!");
		if (nome.length() < TAMANHO_MINIMO_NOME || nome.length() > TAMANHO_MAXIMO_NOME)
			throw new ModelException("O nome deve ter de " + TAMANHO_MINIMO_NOME + " a " +
					            TAMANHO_MAXIMO_NOME + " caracteres!");
		for (int i = 0; i < nome.length(); i++) {
			char c = nome.charAt(i);
			if( !Character.isAlphabetic(c) && !Character.isSpaceChar(c) && c != '\'')
				throw new ModelException("O caracter na posição " + i + " é inválido: " + c);
		}
	}

	public static void validarEmail(String email) throws ModelException {
		if (email == null || email.length() == 0)
			throw new ModelException("O email não pode ser nulo!");
		Matcher matcher = PATTERN_EMAIL.matcher(email);
	    if(!matcher.matches())	
	    	throw new ModelException("O email é inválido: " + email);
	}

	// Métodos de validação
	public static void validarSenha(String senha) throws ModelException {
		if (senha == null || senha.length() == 0)
			throw new ModelException("A senha não pode ser nula!");
        if(senha.length() > TAMANHO_MAXIMO_SENHA)
			throw new ModelException("A quantidade de caracter da senha não pode ultrapassar a " + TAMANHO_MAXIMO_SENHA);
	}


}
