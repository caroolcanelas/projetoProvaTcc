package com.projetoProvaTcc.repository;

import com.projetoProvaTcc.exception.ModelException;
import com.projetoProvaTcc.model.Disciplina;
import com.projetoProvaTcc.service.DisciplinaService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

public class TestaObjeto {


    @Autowired
    private DisciplinaService disciService;

    @PostConstruct
    public void init() throws ModelException {
        // Criando uma disciplina de teste após o Spring subir o contexto e o banco
        Disciplina d = new Disciplina("PROG II", "Programação de Computadores II", 3, "Aprender princípios de Programação II");

//        boolean sucesso = DisciService.;
//
//        if (sucesso) {
//            System.out.println("Disciplina salva com sucesso!");
//        } else {
//            System.out.println("Erro ao salvar a disciplina.");
//        }
    }
}
