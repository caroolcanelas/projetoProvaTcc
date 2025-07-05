package com.projetoProvaTcc.controller;

import com.projetoProvaTcc.dto.QuestaoDTO;
import com.projetoProvaTcc.dto.TopicoDTO;
import com.projetoProvaTcc.exception.ModelException;
import com.projetoProvaTcc.service.QuestaoService;
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
@RequestMapping("api/questao")
@CrossOrigin(origins = "http://localhost:5173")
public class QuestaoController {

    @Autowired
    private QuestaoService questaoService;

    @Operation(summary = "Cria uma questão")
    @PostMapping
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = TopicoDTO.class),
                    examples = @ExampleObject(value = """
            {
               "instrucaoInicial": "string",
               "suporte": "string",
               "comando": "string",
               "nivel": "FACIL",
               "tipo": "RESPOSTA_UNICA",
               "validada": true,
               "matriculaProfessorValidador": 0
             }
        """)
            )
    )
    public ResponseEntity<QuestaoDTO> criaQuestao(@RequestBody QuestaoDTO questaoDTO) {
        try {
            QuestaoDTO salva = questaoService.salvar(questaoDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(salva);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Lista uma questão por id")
    @GetMapping("/{id}")
    public QuestaoDTO getPorId(@PathVariable int id){
        return questaoService.buscarPorId(id);
    }

    @Operation(summary= "Listar todas as questões")
    @GetMapping
    public ResponseEntity<List<QuestaoDTO>> listarTodasQuestoes(){
        try{
            List<QuestaoDTO> questoes = questaoService.buscarTodasQuestoes();
            return ResponseEntity.status(HttpStatus.OK).body(questoes);
        } catch (Exception e){
            return ResponseEntity.status(500).build();    }
    }

    @Operation(summary = "Exclui uma questão pelo ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarQuestao(@PathVariable long id) {
        try {
            boolean remove = questaoService.deletarQuestaoPorId(id);
            if (remove) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.status(404).body("Questão com ID " + id + " não encontrada.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao excluir Questão: " + e.getMessage());
        }
    }

    @Operation(summary = "Associa um recurso já existente a uma questão")
    @PostMapping("/{idQuestao}/recurso")
    public ResponseEntity<?> adicionarRecursoExistente(
            @PathVariable Long idQuestao,
            @RequestBody Long idRecurso
    ) throws Exception {
        questaoService.adicionarRecursoNaQuestao(idQuestao, idRecurso);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Remove recurso da questão")
    @DeleteMapping("/{idQuestao}/recurso/{idRecurso}")
    public ResponseEntity<?> removerRecurso(@PathVariable int idQuestao, @PathVariable int idRecurso) throws ModelException {
        questaoService.removerRecursoDaQuestao(idQuestao, idRecurso);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Associa uma opção já existente a uma questão")
    @PostMapping("/{idQuestao}/opcao/")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = TopicoDTO.class),
                    examples = @ExampleObject(value = """
            {
               "conjOpcoes": [0]
             }
        """)
            )
    )
    public ResponseEntity<?> adicionarOpcaoNaQuestao(
            @PathVariable int idQuestao,
            @RequestBody int idOpcao
    ) throws Exception {
        questaoService.adicionarOpcaoNaQuestao(idQuestao, idOpcao);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Remove opção da questão")
    @DeleteMapping("/{idQuestao}/opcao/{idOpcao}")
    public ResponseEntity<?> removerOpcao(@PathVariable int idQuestao, @PathVariable int idOpcao) throws ModelException {
        questaoService.removerOpcaoDaQuestao(idQuestao, idOpcao);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Adiciona tag na questão")
    @PostMapping("/{idQuestao}/addTag")
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
    public ResponseEntity<?> adicionarTags(@PathVariable int idQuestao, @RequestBody List<String> nomesTags) {
        try {
            questaoService.adicionarTagsNaQuestao(idQuestao, nomesTags);
            return ResponseEntity.ok().body("Tags adicionadas com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

    @Operation(summary = "Remove tags da questão")
    @DeleteMapping("/{idQuestao}/removeTags")
    public ResponseEntity<?> removerTags(@PathVariable int idQuestao, @RequestBody List<String> nomesTags) {
        try {
            questaoService.removerTagDaQuestao(idQuestao, nomesTags);
            return ResponseEntity.ok().body("Tags removidas com sucesso!");
        } catch (ModelException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //add questao derivada
    @Operation(summary = "Adiciona Questão Derivada em Questão")
    @PostMapping("/{idQuestao}/questaoDerivada")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = TopicoDTO.class),
                    examples = @ExampleObject(value = """
            {
               "instrucaoInicial": "string",
               "suporte": "string",
               "comando": "string",
               "nivel": "FACIL",
               "tipo": "RESPOSTA_UNICA",
               "validada": true,
               "matriculaProfessorValidador": 0
             }
        """)
            )
    )
    public ResponseEntity<?> adicionaQuestaoDerivada(@PathVariable int idQuestao, @RequestBody QuestaoDTO dto) throws ModelException{
        questaoService.adicionarQuestaoDerivadaEmQuestao(idQuestao, dto.getConjQuestoesDerivadas());
        return ResponseEntity.ok().body("Questão Derivada adicionados com sucesso!");
    }

    //deletar questao derivada
    @Operation(summary = "Remove uma questão derivada de uma questão")
    @DeleteMapping("/{idQuestao}/questaoDerivada/{idQuestaoDerivada}")
    public ResponseEntity<?> removerQuestaoDerivada(@PathVariable int idQuestao, @PathVariable int idQuestaoDerivada) throws ModelException {

        questaoService.removerQuestaoDerivadaDeQuestao(idQuestao, idQuestaoDerivada);
        return ResponseEntity.noContent().build();
    }

    //update
    @Operation(summary = "Atualiza parcialmente uma questão")
    @PatchMapping("/{idQuestao}")
    public ResponseEntity<?> atualizarParcialmenteQuestao(
            @PathVariable int idQuestao,
            @RequestBody QuestaoDTO dto) {
        try {
            questaoService.atualizarParcialQuestao(idQuestao, dto);
            return ResponseEntity.ok("Questão atualizada parcialmente!");
        } catch (ModelException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

    //import batch de questoes via csv
    @Operation(summary = "Faz o upload em batch de questões")
    @PostMapping(value = "/importar-csv",  consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> importarQuestoesViaCsv(@RequestParam("arquivo") MultipartFile file) {
        try {
            questaoService.importarQuestoesViaCsv(file);
            return ResponseEntity.ok("Importação de questões realizada com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro na importação: " + e.getMessage());
        }
    }


}
