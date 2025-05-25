package com.projetoProvaTcc.entity;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import jakarta.persistence.*;
import com.projetoProvaTcc.exception.ModelException;

@Entity
public class Recurso { //TODO já conferido
	//
	// ATRIBUTOS
	//
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Lob
	@Column(name = "conteudo", columnDefinition = "LONGBLOB")
	private byte[] conteudo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "opcao_id")  // Aqui cria a FK!
	private Opcao opcao;

	public Opcao getOpcao() { return opcao; }
	public void setOpcao(Opcao opcao) { this.opcao = opcao; }


	//
	// MÉTODOS
	//
	public Recurso() {
	}

	public Recurso(String pathDoArquivo) throws ModelException, IOException {
		super();

		File arquivo = new File(pathDoArquivo);
		InputStream is = new FileInputStream(arquivo);
		long length = arquivo.length();
		byte[] bytes = new byte[(int) length];
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0)
			offset += numRead;
		is.close();

		if (offset < bytes.length) 
			throw new IOException("Não foi possível ler todo o arquivo " + arquivo.getName());		
		this.setConteudo(bytes);
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public byte[] getConteudo() {
		return this.conteudo;
	}

	public void setConteudo(byte[] conteudo) throws ModelException {
		Recurso.validarConteudo(conteudo);
		this.conteudo = conteudo;
	}

	public static void validarConteudo(byte[] conteudo) throws ModelException {
		if (conteudo == null || conteudo.length == 0)
			throw new ModelException("Recurso (imagem) está vazio!");
	}

	@Override
	public String toString() {
		return "Recurso{" + "id='" + this.id + "}";
	}

}
