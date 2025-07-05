package com.projetoProvaTcc.controller;

import com.projetoProvaTcc.dto.DisciplinaDTO;
import com.projetoProvaTcc.dto.TopicoDTO;
import com.projetoProvaTcc.entity.Topico;
import com.projetoProvaTcc.exception.ModelException;
import com.projetoProvaTcc.mapper.TopicoMapper;
import com.projetoProvaTcc.service.DisciplinaService;
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
@RequestMapping("/api/disciplina")
public class DisciplinaController {

    @Autowired
    private DisciplinaService disciplinaService;

    //Todos os endpoint chamam a camada SERVICE onde fica a logica de interação com o banco de dados
    @Operation(summary = "Cria uma disciplina")
    @PostMapping
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = TopicoDTO.class),
                    examples = @ExampleObject(value = """
            {
              "codigo": "string",
              "nome": "string",
              "numCreditos": 0,
              "objetivoGeral": "string"
             }
        """)
            )
    )
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

    @Operation(summary= "Listar uma disciplina por id")
    @GetMapping("/{id}")
    public DisciplinaDTO getPorId(@PathVariable int id) {
        return disciplinaService.buscarPorId(id);
    }

    @Operation(summary = "Atualiza parcialmente uma disciplina")
    @PatchMapping("/{id}")
    public ResponseEntity<DisciplinaDTO> atualizarParcialmenteDisciplina(
            @PathVariable int id,
            @RequestBody DisciplinaDTO dto) {
        try {
            DisciplinaDTO atualizada = disciplinaService.atualizar(id, dto);
            return ResponseEntity.ok(atualizada);
        } catch (ModelException e) {
            return ResponseEntity.badRequest().body(null); // ou retornar mensagem
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
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = TopicoDTO.class),
                    examples = @ExampleObject(value = """
            {
              "conjTopicos": [0]
            }
        """)
            )
    )
    public ResponseEntity<?> adicionarTopico(@PathVariable int idDisciplina, @RequestBody TopicoDTO dto) throws ModelException {
        Topico topico;
        if (dto.getId() != 0) {
            // Se o ID está presente, só precisa dele para o service buscar
            topico = new Topico();
            topico.setId(dto.getId());
        } else {
            // Se for novo, aí sim usa o mapper completo
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

    //import batch de disciplinas via csv
    @Operation(summary="Faz o upload em batch de disciplinas")
    @PostMapping(value = "/importar-csv",  consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> importarDisciplinasViaCsv(@RequestParam("arquivo")MultipartFile file){
        try{
            disciplinaService.importarDisiplinasViaCsv(file);
            return ResponseEntity.ok("Importação de disciplinas realizada com sucesso! ");
        } catch (Exception e){
            return ResponseEntity.badRequest().body("Erro na importação" + e.getMessage());
        }
    }

}

