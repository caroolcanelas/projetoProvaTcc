package com.projetoProvaTcc.controller;

import com.projetoProvaTcc.dto.QuestaoDTO;
import com.projetoProvaTcc.service.QuestaoService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

}
