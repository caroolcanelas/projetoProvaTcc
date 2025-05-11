package com.projetoProvaTcc.controller;

import com.projetoProvaTcc.model.Disciplina;
import com.projetoProvaTcc.service.DisciplinaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/disciplina")
public class DisciplinaController {

    private DisciplinaService disciplinaService;

    //endpoint chama a service
    @PostMapping
    public ResponseEntity<Disciplina> criaDisciplina(@RequestBody Disciplina disciplina) {
        Disciplina salva = disciplinaService.salvarDisciplina(disciplina);
        return new ResponseEntity<>(salva, HttpStatus.CREATED);
    }
}

