package com.projetoProvaTcc.controller;

import com.projetoProvaTcc.dto.ProfessorDTO;
import com.projetoProvaTcc.service.ProfessorService;
import com.projetoProvaTcc.exception.ModelException;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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
}
