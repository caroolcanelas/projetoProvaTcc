package com.projetoProvaTcc.controller;

import com.projetoProvaTcc.dto.TagDTO;
import com.projetoProvaTcc.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    @Operation(summary = "Cria uma tag")
    @PostMapping
    public ResponseEntity<TagDTO> criarTag(@RequestBody TagDTO tagDTO) {
        try {
            TagDTO salva = tagService.salvar(tagDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(salva);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Busca tag por ID")
    @GetMapping("/{id}")
    public ResponseEntity<TagDTO> buscarPorId(@PathVariable int id) {
        TagDTO dto = tagService.buscarPorId(id);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @Operation(summary = "Listar todas as tags")
    @GetMapping
    public ResponseEntity<List<TagDTO>> listarTodasTags() {
        try {
            List<TagDTO> tags = tagService.buscarTodasTags();
            return ResponseEntity.status(HttpStatus.OK).body(tags);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TagDTO> atualizarParcial(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        try {
            TagDTO atualizado = tagService.atualizar(id, updates);
            if (atualizado != null) {
                return ResponseEntity.ok(atualizado);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @Operation(summary = "Exclui uma tag pelo ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> detelarTag(@PathVariable long id) {
        try {
            boolean remove = tagService.deletarTagPorId(id);
            if (remove) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.status(404).body("Tag com ID " + id + " não encontrada.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao excluir Tag: " + e.getMessage());
        }
    }
}