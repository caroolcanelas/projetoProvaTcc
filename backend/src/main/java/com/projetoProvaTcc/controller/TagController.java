package com.projetoProvaTcc.controller;

import com.projetoProvaTcc.dto.TagDTO;
import com.projetoProvaTcc.dto.TopicoDTO;
import com.projetoProvaTcc.exception.ModelException;
import com.projetoProvaTcc.service.TagService;
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
@RequestMapping("api/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    @Operation(summary = "Cria uma tag")
    @PostMapping
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = TopicoDTO.class),
                    examples = @ExampleObject(value = """
            {
              "tagName": "string",
              "assunto": "string"
            }
        """)
            )
    )
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

    @Operation(summary = "Atualiza parcialmente uma tag")
    @PatchMapping("/{id}")
    public ResponseEntity<TagDTO> atualizarParcial(@PathVariable Long id, @RequestBody TagDTO dto) {
        try {
            TagDTO atualizado = tagService.atualizar(id, dto);
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

    //adiciona questao na tag
    @Operation(summary = "Adiciona uma questão a tag")
    @PostMapping("/add-questao/{idQuestao}")
    public ResponseEntity<String> adicionarQuestaoNasTags(
            @RequestBody List<String> nomesTags,
            @PathVariable int idQuestao) {
        try {
            tagService.adicionarQuestaoNaTag(nomesTags, idQuestao);
            return ResponseEntity.ok("Questão adicionada às tags com sucesso!");
        } catch (ModelException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    //remover questao de tag
    @Operation(summary = "Remove uma questão da  tag")
    @DeleteMapping("/remove-questao/{idQuestao}")
    public ResponseEntity<String> removerQuestaoDasTags(
            @RequestBody List<String> nomesTags,
            @PathVariable int idQuestao) {
        try {
            tagService.removerQuestaoDasTags(nomesTags, idQuestao);
            return ResponseEntity.ok("Questão removida das tags com sucesso!");
        } catch (ModelException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Adiciona tópicos na tag informada")
    @PostMapping("/{nomeTag}/add-topicos")
    public ResponseEntity<String> adicionarTopicosNaTag(
            @PathVariable String nomeTag,
            @RequestBody List<String> nomesTopicos) {
        try {
            tagService.adicionarTopicosNaTag(nomeTag, nomesTopicos);
            return ResponseEntity.ok("Tópicos adicionados na tag com sucesso!");
        } catch (ModelException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

    @Operation(summary = "Remove tópicos da tag informada")
    @DeleteMapping("/{nomeTag}/remove-topicos")
    public ResponseEntity<String> removerTopicosDaTag(
            @PathVariable String nomeTag,
            @RequestBody List<String> nomesTopicos) {
        try {
            tagService.removerTopicosDaTag(nomeTag, nomesTopicos);
            return ResponseEntity.ok("Tópicos removidos da tag com sucesso!");
        } catch (ModelException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }


    //import batch de tags via csv
    @Operation(summary = "Faz o upload em batch de tags")
    @PostMapping(value = "/importar-csv",  consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> importarTagsViaCsv(@RequestParam("arquivo") MultipartFile file) {
        try {
            tagService.importarTagsViaCsv(file);
            return ResponseEntity.ok("Importação de tags realizada com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro na importação: " + e.getMessage());
        }
    }
}