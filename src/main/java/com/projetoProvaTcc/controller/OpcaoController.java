package com.projetoProvaTcc.controller;

import com.projetoProvaTcc.dto.OpcaoDTO;
import com.projetoProvaTcc.service.OpcaoService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/opcao")
public class OpcaoController {

@Autowired
private OpcaoService opcaoService;

@Operation(summary = "Cria uma opção")
@PostMapping
public ResponseEntity<OpcaoDTO> criarOpcao(@RequestBody OpcaoDTO opcaoDTO) {
    try {
        OpcaoDTO salva = opcaoService.salvar(opcaoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(salva);
    } catch (Exception e) {
        return ResponseEntity.badRequest().build();
    }
}

@Operation(summary= "Listar todas as opções")
@GetMapping
public ResponseEntity<List<OpcaoDTO>> listarTodasOpcoes(){
    try{
        List<OpcaoDTO> opcoes = opcaoService.buscarTodasOpcoes();
        return ResponseEntity.status(HttpStatus.OK).body(opcoes);
    } catch (Exception e){
        return ResponseEntity.status(500).build();    }
}

    @Operation(summary = "Exclui uma opção pelo ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarOpcao(@PathVariable long id) {
        try {
            boolean remove = opcaoService.deletarOpcaoPorId(id);
            if (remove) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.status(404).body("Opção com ID " + id + " não encontrada.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao excluir Opção: " + e.getMessage());
        }
    }

}
