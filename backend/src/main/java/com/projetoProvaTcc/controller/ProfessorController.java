package com.projetoProvaTcc.controller;

import com.projetoProvaTcc.dto.DisciplinaDTO;
import com.projetoProvaTcc.dto.LoginDTO;
import com.projetoProvaTcc.dto.ProfessorDTO;
import com.projetoProvaTcc.entity.Professor;
import com.projetoProvaTcc.service.ProfessorService;
import com.projetoProvaTcc.exception.ModelException;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("api/professor")
public class ProfessorController {

    @Autowired
    private ProfessorService service;

    @Operation(summary = "Cria um professor")
    @PostMapping
    public ResponseEntity<ProfessorDTO> criar(@RequestBody ProfessorDTO dto) {
        try {
            return ResponseEntity.ok(service.salvar(dto));
        } catch (ModelException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public List<ProfessorDTO> listar() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfessorDTO> buscarPorId(@PathVariable Long id) {
        ProfessorDTO dto = service.buscarPorId(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProfessorDTO> deletar(@PathVariable Long id) {
        return service.deletarPorId(id) ?
                ResponseEntity.noContent().build() :
                ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/disciplinas/{disciplinaId}")
    public ResponseEntity<?> adicionarDisciplina(
            @PathVariable Long id,
            @PathVariable Long disciplinaId) {
        try {
            service.adicionarDisciplina(id,disciplinaId);
            return ResponseEntity.ok("Disciplina associada com sucesso.");
        } catch (ModelException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}/disciplinas/{disciplinaId}")
    public ResponseEntity<?> removerDisciplina(
            @PathVariable Long id,
            @PathVariable Long disciplinaId) {
        try {
            service.removerDisciplina(id, disciplinaId);
            return ResponseEntity.ok("Disciplina desassociada com sucesso.");
        } catch (ModelException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Endpoint para adicionar uma questão a um professor
    @PostMapping("/{matricula}/questoes/{idQuestao}")
    public ResponseEntity<String> adicionarQuestao(
            @PathVariable int matricula,
            @PathVariable Long idQuestao) {
        try {
            service.adicionarQuestaoAProfessor(matricula, idQuestao);
            return ResponseEntity.ok("Questão adicionada ao professor com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

    // Endpoint para remover uma questão de um professor
    @DeleteMapping("/{matricula}/questoes/{idQuestao}")
    public ResponseEntity<String> removerQuestao(
            @PathVariable int matricula,
            @PathVariable Long idQuestao) {
        try {
            service.removerQuestaoDeProfessor(matricula, idQuestao);
            return ResponseEntity.ok("Questão removida do professor com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        try {
            Professor professor = service.login(loginDTO.getEmail(), loginDTO.getSenha());
            return ResponseEntity.ok("Login realizado com sucesso! Professor: " + professor.getNome());
        } catch (ModelException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Erro: " + e.getMessage());
        }
    }

}
