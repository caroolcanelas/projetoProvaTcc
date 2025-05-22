package com.projetoProvaTcc.controller;

import com.projetoProvaTcc.dto.TagDTO;
import com.projetoProvaTcc.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    @Operation(summary = "Cria uma tag")
    @PostMapping
    public ResponseEntity<TagDTO> criaTag(@RequestBody TagDTO tagDTO) {
        try {
            TagDTO salva = tagService.salvar(tagDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(salva);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @Operation(summary = "Listar todas as opções")
    @GetMapping
    public ResponseEntity<List<TagDTO>> listaTodasTags() {
        try {
            List<TagDTO> tags = tagService.buscarTodasTags();
            return ResponseEntity.status(HttpStatus.OK).body(tags);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @Operation(summary = "Exclui uma tag pelo id")
    @DeleteMapping("{id}")
    public ResponseEntity<String> deletarTag(@PathVariable long id) {
        boolean remove = tagService.deletarTagPorId(id);
        try {
            if (remove) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.status(404).body("Tag com Id" + "não encontrada");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao excluir Tag: " + e.getMessage());
        }
    }
}
