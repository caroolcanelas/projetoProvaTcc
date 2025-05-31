package com.projetoProvaTcc.controller;

import com.projetoProvaTcc.dto.OpcaoDTO;
import com.projetoProvaTcc.dto.RecursoDTO;
import com.projetoProvaTcc.entity.Recurso;
import com.projetoProvaTcc.exception.ModelException;
import com.projetoProvaTcc.service.OpcaoService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

@Operation(summary = "Lista uma opção por id")
@GetMapping("/{id}")
public OpcaoDTO getPorId(@PathVariable int id){
    return opcaoService.buscarPorId(id);
}

    @Operation(summary = "Adiciona recurso na opção")
    @PostMapping(value = "/{idOpcao}/recurso", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> adicionarRecurso(
            @PathVariable int idOpcao,
            @RequestPart("arquivo") MultipartFile arquivo,
            @RequestPart(value = "id", required = false) Integer id
    ) throws Exception {

        Recurso recurso = new Recurso();

        if (id != null && id != 0) {
            recurso.setId(id);
        }

        if (arquivo != null && !arquivo.isEmpty()) {
            recurso.setConteudo(arquivo.getBytes());
        } else {
            throw new ModelException("Arquivo é obrigatório.");
        }

        opcaoService.adicionarRecursoNaOpcao(idOpcao, recurso);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Remove recurso na opção")
    @DeleteMapping("/{idOpcao}/recurso/{idRecurso}")
    public ResponseEntity<?> removerRecurso(@PathVariable int idOpcao, @PathVariable int idRecurso) throws ModelException {
        opcaoService.removerRecursoDaOpcao(idOpcao, idRecurso);
        return ResponseEntity.noContent().build();
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
