package com.projetoProvaTcc.controller;

import com.projetoProvaTcc.dto.DisciplinaDTO;
import com.projetoProvaTcc.dto.LoginDTO;
import com.projetoProvaTcc.dto.ProfessorDTO;
import com.projetoProvaTcc.dto.TopicoDTO;
import com.projetoProvaTcc.entity.Professor;
import com.projetoProvaTcc.service.ProfessorService;
import com.projetoProvaTcc.exception.ModelException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("api/professor")
public class ProfessorController {

    @Autowired
    private ProfessorService service;

    @Operation(summary = "Cria um professor")
    @PostMapping
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = TopicoDTO.class),
                    examples = @ExampleObject(value = """
            {
              "nome": "string",
              "email": "string",
              "senha": "string",
              "matricula": 0
            }
        """)
            )
    )
    public ResponseEntity<ProfessorDTO> criar(@RequestBody ProfessorDTO dto) {
        try {
            return ResponseEntity.ok(service.salvar(dto));
        } catch (ModelException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Lista todos professores")
    @GetMapping
    public List<ProfessorDTO> listar() {
        return service.listarTodos();
    }

    @Operation(summary = "Busca um professor especifico por id")
    @GetMapping("/{id}")
    public ResponseEntity<ProfessorDTO> buscarPorId(@PathVariable Long id) {
        ProfessorDTO dto = service.buscarPorId(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Exclui um professor pelo id")
    @DeleteMapping("/{id}")
    public ResponseEntity<ProfessorDTO> deletar(@PathVariable Long id) {
        return service.deletarPorId(id) ?
                ResponseEntity.noContent().build() :
                ResponseEntity.notFound().build();
    }

    @Operation(summary = "Adiciona uma disciplina ja existente a um professor")
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

    @Operation(summary = "Remove uma disciplina do professor")
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

    @Operation(summary = "Realiza login com professor existente")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        try {
            Professor professor = service.login(loginDTO.getEmail(), loginDTO.getSenha());
            return ResponseEntity.ok("Login realizado com sucesso! Professor: " + professor.getNome());
        } catch (ModelException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Erro: " + e.getMessage());
        }
    }

    @Operation(summary = "Importa professores em lote via CSV")
    @PostMapping(value = "/importar-csv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> importarProfessores(@RequestParam("arquivo") MultipartFile file) {
        try {
            service.importarProfessoresViaCsv(file);
            return ResponseEntity.ok("Professores importados com sucesso!");
        } catch (ModelException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Falha na importação: " + e.getMessage());
        }
    }

}
