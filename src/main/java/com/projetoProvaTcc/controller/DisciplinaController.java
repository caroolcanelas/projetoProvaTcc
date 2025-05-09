package com.projetoProvaTcc.controller;

import com.projetoProvaTcc.model.Disciplina;
import com.projetoProvaTcc.repository.DaoDisciplina;
import com.projetoProvaTcc.repository.DaoDisciplinaRepository;
import com.projetoProvaTcc.service.DisciplinaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/disciplinas")
public class DisciplinaController {

    @Autowired
    private DaoDisciplinaRepository daoDisciplinaRepository;

    private DisciplinaService disciplinaService;

    //endpoint chama a service
    @PostMapping
    public ResponseEntity<Disciplina> criaDisciplina(@RequestBody Disciplina disciplina) {
        Disciplina salva = disciplinaService.salvarDisciplina(disciplina);
        return new ResponseEntity<>(salva, HttpStatus.CREATED);
    }
}

