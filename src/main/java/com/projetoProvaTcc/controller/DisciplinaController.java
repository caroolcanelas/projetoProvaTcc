package com.projetoProvaTcc.controller;

import com.projetoProvaTcc.entity.Disciplina;
import com.projetoProvaTcc.service.DisciplinaService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/disciplina")
public class DisciplinaController {

    @Autowired
    private DisciplinaService disciplinaService;

    //Todos os endpoint chamam a camada SERVICE onde fica a logica de interação com o banco de dados
    @Operation(summary = "Cria uma disciplina")
    @PostMapping
    public ResponseEntity<Disciplina> criarDisciplina(@RequestBody Disciplina disciplina) {
        try {
            Disciplina salva = disciplinaService.salvar(disciplina);
            return ResponseEntity.status(HttpStatus.CREATED).body(salva);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Lista todas as disciplinas")
    @GetMapping
    public ResponseEntity<?> listarTodasDisciplinas(){
        try {
            List<Disciplina> disciplinas = disciplinaService.buscarTodasDisciplinas();
            return ResponseEntity.status(HttpStatus.OK).body(disciplinas);
        } catch (Exception e){
            return ResponseEntity.status(500).body("Erro ao buscar disciplinas: " + e.getMessage());
        }

    }

    @Operation(summary = "Exclui uma disciplina pelo ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarDisciplina(@PathVariable long id) { //nao tem body. apenas passa id na URL
        try {
            boolean remove = disciplinaService.deletarDisciplinaPorId(id);
            if (remove) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.status(404).body("Disciplina com ID " + id + " não encontrada.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao excluir disciplina: " + e.getMessage());
        }
    }
}

