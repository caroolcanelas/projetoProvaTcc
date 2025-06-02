package com.projetoProvaTcc.controller;

import com.projetoProvaTcc.dto.OpcaoDTO;
import com.projetoProvaTcc.dto.QuestaoDTO;
import com.projetoProvaTcc.entity.Recurso;
import com.projetoProvaTcc.exception.ModelException;
import com.projetoProvaTcc.service.QuestaoService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/questao")
public class QuestaoController {

    @Autowired
    private QuestaoService questaoService;

    @Operation(summary = "Cria uma questão")
    @PostMapping
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


}
