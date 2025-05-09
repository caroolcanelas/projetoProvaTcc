package com.projetoProvaTcc.controller;

import com.projetoProvaTcc.model.Disciplina;
import com.projetoProvaTcc.repository.DaoDisciplina;
import com.projetoProvaTcc.repository.DaoDisciplinaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/disciplinas")
public class DisciplinaController {

    @Autowired
    private DaoDisciplinaRepository daoDisciplinaRepository;

    @PostMapping
    public Disciplina salvar(@RequestBody Disciplina disciplina) {
        return daoDisciplinaRepository.save(disciplina);
    }
}

