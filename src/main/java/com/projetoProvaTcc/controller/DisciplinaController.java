package com.projetoProvaTcc.controller;

import com.projetoProvaTcc.entity.Disciplina;
import com.projetoProvaTcc.service.DisciplinaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/disciplina")
public class DisciplinaController {

    private DisciplinaService disciplinaService;

    //endpoint chama a service
    @PostMapping
    public ResponseEntity<Disciplina> criarDisciplina(@RequestBody Disciplina disciplina) {
        try {
            Disciplina salva = disciplinaService.salvar(disciplina);
            return ResponseEntity.status(HttpStatus.CREATED).body(salva); // ou: return new ResponseEntity<>(salva, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build(); // ou: ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

