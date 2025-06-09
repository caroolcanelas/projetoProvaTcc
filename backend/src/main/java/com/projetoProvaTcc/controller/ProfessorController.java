package com.projetoProvaTcc.controller;

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
