package com.projetoProvaTcc.controller;

import com.projetoProvaTcc.dto.DisciplinaDTO;
import com.projetoProvaTcc.dto.TopicoDTO;
import com.projetoProvaTcc.entity.Topico;
import com.projetoProvaTcc.exception.ModelException;
import com.projetoProvaTcc.mapper.TopicoMapper;
import com.projetoProvaTcc.service.DisciplinaService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/disciplina")
public class DisciplinaController {

    @Autowired
    private DisciplinaService disciplinaService;

    //Todos os endpoint chamam a camada SERVICE onde fica a logica de interação com o banco de dados
    @Operation(summary = "Cria uma disciplina")
    @PostMapping
    public ResponseEntity<DisciplinaDTO> criarDisciplina(@RequestBody DisciplinaDTO disciplinaDTO) {
        try {
            DisciplinaDTO salva = disciplinaService.salvar(disciplinaDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(salva);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Lista todas as disciplinas")
    @GetMapping
    public ResponseEntity<List<DisciplinaDTO>> listarTodasDisciplinas(){
        try {
            List<DisciplinaDTO> disciplinas = disciplinaService.buscarTodasDisciplinas();
            return ResponseEntity.status(HttpStatus.OK).body(disciplinas);
        } catch (Exception e){
            return ResponseEntity.status(500).build();
        }

    }

    @Operation(summary = "Exclui uma disciplina pelo ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarDisciplina(@PathVariable long id) { //nao tem body. apenas passa id na URL
        try {
            boolean remove = disciplinaService.deletarDisciplinaPorId(id);
            if (remove) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.status(404).body("Disciplina com ID " + id + " não encontrada.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao excluir disciplina: " + e.getMessage());
        }
    }

    @Operation(summary = "Adiciona topico na disciplina")
    @PostMapping("/{idDisciplina}/topico")
    public ResponseEntity<?> adicionarTopico(@PathVariable int idDisciplina, @RequestBody TopicoDTO dto) throws ModelException {
        Topico topico;

        if (dto.getId() != 0) {
            // Se o ID está presente, só precisa dele para o service buscar
            topico = new Topico();
            topico.setId(dto.getId());
        } else {
            // Se for novo, aí sim usa o mapper completo (com validações)
            topico = TopicoMapper.toEntity(dto);
        }

        disciplinaService.adicionarTopicoNaDisciplina(idDisciplina, topico);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Remove topico na disciplina")
    @DeleteMapping("/{idDisciplina}/topico/{idTopico}")
    public ResponseEntity<?> removerTopico(@PathVariable int idDisciplina, @PathVariable int idTopico) throws ModelException {
        disciplinaService.removerTopicoDaDisciplina(idDisciplina, idTopico);
        return ResponseEntity.noContent().build();
    }
}

