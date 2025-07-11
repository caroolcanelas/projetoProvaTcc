package com.projetoProvaTcc.controller;

import com.projetoProvaTcc.dto.TopicoDTO;
import com.projetoProvaTcc.exception.ModelException;
import com.projetoProvaTcc.service.TopicoService;
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

@RestController
@RequestMapping("api/topico")
public class TopicoController {

    @Autowired
    private TopicoService topicoService;

    @Operation(summary = "Cria um tópico")
    @PostMapping
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = TopicoDTO.class),
                    examples = @ExampleObject(value = """
            {
              "numOrdem": 0,
              "nome": "string",
              "conteudo": "string",
              "disciplina": 0
            }
        """)
            )
    )
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
    public ResponseEntity<TopicoDTO> getPorId(@PathVariable int id) {
        TopicoDTO dto = topicoService.buscarPorId(id);
        if (dto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // ou .body("Tópico não encontrado")
        }
        return ResponseEntity.ok(dto);
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

    @Operation(summary = "Atualiza parcialmente um tópico")
    @PatchMapping("/{id}")
    public ResponseEntity<?> atualizarParcial(@PathVariable int id, @RequestBody TopicoDTO dto) {
        try {
            TopicoDTO atualizado = topicoService.atualizar(id, dto);
            return ResponseEntity.ok(atualizado);
        } catch (ModelException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno: " + e.getMessage());
        }
    }

    @Operation(summary = "Adiciona SubTópico em tópico")
    @PostMapping("/{idTopico}/subTopico")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = TopicoDTO.class),
                    examples = @ExampleObject(value = """
            {
              "conjSubTopicos": [0]
            }
        """)
            )
    )
    public ResponseEntity<?> adicionaSubTopico(
            @PathVariable int idTopico,
            @RequestBody TopicoDTO dto) throws ModelException {
        try {
            topicoService.adicionarSubTopicoEmTopico(idTopico, dto.getConjSubTopicos());
            return ResponseEntity.ok().body("Subtópicos adicionados com sucesso!");
        } catch (ModelException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

    @Operation(summary = "Remove um subtópico de um tópico")
    @DeleteMapping("/{idTopico}/subtopico/{idSubtopico}")
    public ResponseEntity<?> removerSubtopico(@PathVariable int idTopico, @PathVariable int idSubtopico) throws ModelException {

        topicoService.removerSubtopico(idTopico, idSubtopico);
        return ResponseEntity.noContent().build();
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

    //add e remove de tag
    @Operation(summary = "Adiciona tag no topico")
    @PostMapping("/{idTopico}/addTag")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = TopicoDTO.class),
                    examples = @ExampleObject(value = """
            {
              "conjTags": [0]
            }
        """)
            )
    )
    public ResponseEntity<?> adicionarTags(@PathVariable int idTopico, @RequestBody TopicoDTO dto) {
        try {
            topicoService.adicionarTagsAoTopico(idTopico, dto.getConjTags());
            return ResponseEntity.ok().body("Tags adicionadas com sucesso!");
        } catch (Exception e) {
            e.printStackTrace(); // loga no console
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }

    }

    @Operation(summary = "Remove tags do tópico")
    @DeleteMapping("/{idTopico}/removeTags")
    public ResponseEntity<?> removerTags(@PathVariable int idTopico, @RequestBody TopicoDTO dto) {
        try {
            topicoService.removerTagDoTopico(idTopico, dto.getConjTags());
            return ResponseEntity.ok().body("Tags removidas com sucesso!");
        } catch (ModelException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //import batch de topicos via csv
    @Operation(summary = "Faz o upload em batch de tópicos")
    @PostMapping(value = "/importar-csv",  consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> importarTopicosViaCsv(@RequestParam("arquivo") MultipartFile file) {
        try {
            topicoService.importarTopicosViaCsv(file);
            return ResponseEntity.ok("Importação de tópicos realizada com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro na importação: " + e.getMessage());
        }
    }
}
