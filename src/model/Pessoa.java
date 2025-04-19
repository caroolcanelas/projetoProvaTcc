package model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Pessoa {
	//
	// CONSTANTES
	//
	final public static int TAMANHO_MINIMO_NOME = 5;
	final public static int TAMANHO_MAXIMO_NOME = 40;
	final private static String _REGEX_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
	                                         + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	final private static Pattern PATTERN_EMAIL = Pattern.compile(_REGEX_EMAIL, Pattern.CASE_INSENSITIVE);
	
	//
	// ATRIBUTOS
	//
	private String cpf;
	private String nome;
	private String email;

	//
	// MÉTODOS
	//
	public Pessoa(String c, String n, String e) throws ModelException {
		super();
		this.setCpf(c);
		this.setNome(n);
		this.setEmail(e);
	}

	public String getCpf() {
		return this.cpf;
	}

	public void setCpf(String cpf) throws ModelException {
		Pessoa.validarCpf(cpf);
		this.cpf = cpf;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) throws ModelException {
		Pessoa.validarNome(nome);
		this.nome = nome;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) throws ModelException {
		Pessoa.validarEmail(email);
		this.email = email;
	}

	public static void validarCpf(String cpf) throws ModelException {
		if (cpf == null || cpf.length() == 0)
			throw new ModelException("O CPF não pode ser nulo!");
		if (cpf.length() != 14)
			throw new ModelException("O CPF deve ter 14 caracteres!");
		for (int i = 0; i < 14; i++) {
			char c = cpf.charAt(i);
			switch (i) {
			case 3:
			case 7:
				if (c != '.')
					throw new ModelException("Na posição " + i + " deve ter '.'");
				break;
			case 11:
				if (c != '-')
					throw new ModelException("Na posição 11 deve ter '-'");
				break;
			default:
				if (!Character.isDigit(c))
					throw new ModelException("Na posição " + i + " deve ter dígito: " + c);
				break;
			}
		}
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

	@Override
	public String toString() {
		return this.cpf + " - " + this.nome;
	}
}
