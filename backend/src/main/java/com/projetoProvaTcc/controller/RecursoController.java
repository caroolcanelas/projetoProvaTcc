package com.projetoProvaTcc.controller;

import com.projetoProvaTcc.dto.RecursoDTO;
import com.projetoProvaTcc.exception.ModelException;
import com.projetoProvaTcc.service.RecursoService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/recursos")
public class RecursoController {

    @Autowired
    private RecursoService recursoService;

    // Upload
    @Operation(summary = "Adiciona um recurso")
    @PostMapping("/upload")
    public ResponseEntity<RecursoDTO> upload(@RequestParam("arquivo") MultipartFile file) {
        try {
            RecursoDTO recursoDTO = recursoService.salvarArquivo(file);
            return ResponseEntity.status(HttpStatus.CREATED).body(recursoDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Download
    @Operation(summary = "Faz o get de um recurso")
    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> download(@PathVariable int id) {
        try {
            byte[] conteudo = recursoService.buscarConteudoPorId(id);
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"recurso_" + id + ".bin\"")
                    .body(conteudo);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Exclui um recurso pelo ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarRecurso(@PathVariable int id) {
        try {
            boolean remove = recursoService.deletarRecursoPorId(id);
            if (remove) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.status(404).body("Recurso com ID " + id + " não encontrada.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao excluir Recurso: " + e.getMessage());
        }
    }

    //update


    @Operation(summary = "Atualiza parcialmente um recurso")
    @PatchMapping(value = "/{idRecurso}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> atualizarParcialmenteRecurso
                (@PathVariable int idRecurso,
                 @RequestParam("arquivo") MultipartFile arquivo) {
        try {
            recursoService.atualizarParcialRecurso(arquivo, idRecurso);
            return ResponseEntity.ok("Questão atualizada parcialmente!");
        } catch (ModelException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

}
