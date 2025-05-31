package com.projetoProvaTcc.controller;

import com.projetoProvaTcc.dto.TopicoDTO;
import com.projetoProvaTcc.service.TopicoService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/topico")
public class TopicoController {

    @Autowired
    private TopicoService topicoService;

    @Operation(summary = "Cria um tópico")
    @PostMapping
    public ResponseEntity<TopicoDTO> criarTopico(@RequestBody TopicoDTO topicoDTO) {
        try {
            TopicoDTO salva = topicoService.salvar(topicoDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(salva);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary= "Listar um tópico por id")
    @GetMapping("/{id}")
    public TopicoDTO getPorId(@PathVariable int id) {
        return topicoService.buscarPorId(id);
    }

    @Operation(summary= "Listar todos os Tópicos")
    @GetMapping
    public ResponseEntity<List<TopicoDTO>> listarTodosTopicos(){
        try{
            List<TopicoDTO> topicos = topicoService.buscarTodosTopicos();
            return ResponseEntity.status(HttpStatus.OK).body(topicos);
        } catch (Exception e){
            return ResponseEntity.status(500).build();    }
    }

    @Operation(summary = "Exclui um tópico pelo ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarTopico(@PathVariable long id) {
        try {
            boolean remove = topicoService.deletarTopicoPorId(id);
            if (remove) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.status(404).body("Tópico com ID " + id + " não encontrada.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao excluir Tópico: " + e.getMessage());
        }
    }

}
