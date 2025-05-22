package com.projetoProvaTcc.controller;

import com.projetoProvaTcc.dto.RecursoDTO;
import com.projetoProvaTcc.service.RecursoService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/api/recursos")
public class RecursoController {

    @Autowired
    private RecursoService recursoService;

    // Upload
    @Operation(summary= "Faz o upload de um novo recurso")
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
    @Operation(summary= "Lista o recurso pelo id")
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
}
