package com.projetoProvaTcc.controller;

import com.projetoProvaTcc.dto.RecursoDTO;
import com.projetoProvaTcc.entity.Recurso;
import com.projetoProvaTcc.exception.ModelException;
import com.projetoProvaTcc.repository.RecursoRepository;
import com.projetoProvaTcc.service.RecursoService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/recursos")
@CrossOrigin(origins = "*")
public class RecursoController {

    @Autowired
    private RecursoService recursoService;

    @Autowired
    private RecursoRepository recursoRepository;

    // Upload
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RecursoDTO> upload(@RequestParam("arquivo") MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new Exception("Arquivo vazio");
        }

        Recurso recurso = new Recurso();
        recurso.setConteudo(file.getBytes());
        recurso.setNomeArquivo(file.getOriginalFilename());
        recurso.setTipoArquivo(file.getContentType());

        recurso = recursoRepository.save(recurso);

        RecursoDTO dto = new RecursoDTO(recurso.getId(), recurso.getConteudo());
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    // Download
    @Operation(summary = "Faz o get de um recurso")
    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> obterRecurso(@PathVariable int id) throws Exception {
        Recurso recurso = recursoRepository.findById(id)
                .orElseThrow(() -> new Exception("Recurso não encontrado"));

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(recurso.getTipoArquivo()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + recurso.getNomeArquivo() + "\"")
                .body(recurso.getConteudo());
    }

    @Operation(summary = "Exclui um recurso pelo ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarRecurso(@PathVariable int id) {
        try {
            boolean remove = recursoService.deletarRecursoPorId(id);
            if (remove) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.status(404).body("Recurso com ID " + id + " não encontrada.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao excluir Recurso: " + e.getMessage());
        }
    }

    //update


    @Operation(summary = "Atualiza parcialmente um recurso")
    @PatchMapping(value = "/{idRecurso}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> atualizarParcialmenteRecurso
                (@PathVariable int idRecurso,
                 @RequestParam("arquivo") MultipartFile arquivo) {
        try {
            recursoService.atualizarParcialRecurso(arquivo, idRecurso);
            return ResponseEntity.ok("Questão atualizada parcialmente!");
        } catch (ModelException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }


    //precisa desse método?
    //Todos os arquivos contidos no CSV, devem existir no ZIP
//    @Operation(summary = "Importa recursos em lote via CSV e ZIP")
//    @PostMapping(value = "/importar-lote", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<String> importarRecursosEmLote(
//            @RequestParam("csv") MultipartFile csvFile,
//            @RequestParam("zip") MultipartFile zipFile
//    ) {
//        try {
//            recursoService.importarRecursosViaCsv(csvFile, zipFile);
//            return ResponseEntity.ok("Recursos importados com sucesso!");
//        } catch (ModelException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body("Erro durante a importação: " + e.getMessage());
//        }
//    }

}
