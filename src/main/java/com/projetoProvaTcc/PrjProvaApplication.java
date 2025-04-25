package com.projetoProvaTcc;

import com.projetoProvaTcc.exception.ModelException;
import com.projetoProvaTcc.model.Disciplina;
import com.projetoProvaTcc.repository.DaoDisciplina;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class PrjProvaApplication {

	@Autowired
	private DaoDisciplina daoDisciplina;

	public static void main(String[] args) {
		SpringApplication.run(PrjProvaApplication.class, args);
	}

	@PostConstruct
	public void init() throws ModelException {
		// Criando uma disciplina de teste após o Spring subir o contexto e o banco
		Disciplina d = new Disciplina("PROG II", "Programação de Computadores II", 3, "Aprender princípios de Programação II");

		boolean sucesso = daoDisciplina.salvar(d);

		if (sucesso) {
			System.out.println("Disciplina salva com sucesso!");
		} else {
			System.out.println("Erro ao salvar a disciplina.");
		}
	}
}
