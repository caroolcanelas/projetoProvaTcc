package com.projetoProvaTcc.service;

import com.projetoProvaTcc.dto.RecursoDTO;
import com.projetoProvaTcc.entity.Recurso;
import com.projetoProvaTcc.exception.ModelException;
import com.projetoProvaTcc.repository.RecursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class RecursoService {

    @Autowired
    private RecursoRepository recursoRepository;

    public RecursoDTO salvarArquivo(MultipartFile file) throws IOException, ModelException {
        byte[] conteudo = file.getBytes();

        Recurso recurso = new Recurso();
        recurso.setConteudo(conteudo); // aqui já valida se está vazio

        recurso = recursoRepository.save(recurso);

        return new RecursoDTO(recurso.getId()); // só retorna ID
    }

    public byte[] buscarConteudoPorId(int id) throws Exception {
        Recurso recurso = recursoRepository.findById(id)
                .orElseThrow(() -> new Exception("Recurso não encontrado"));
        return recurso.getConteudo();
    }

    public boolean deletarRecursoPorId(int id) throws ModelException {
        if (recursoRepository.existsById(id)) {
            recursoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    //update
    public void atualizarParcialRecurso(MultipartFile arquivo, int idRecurso) throws ModelException {
        Recurso recurso = recursoRepository.findById(idRecurso)
                .orElseThrow(() -> new ModelException("Recurso não encontrada com ID: " + idRecurso));

        try {
            if (arquivo != null && !arquivo.isEmpty()) {
                recurso.setConteudo(arquivo.getBytes());
            }

            recursoRepository.save(recurso);

        } catch (IOException e) {
            throw new ModelException("Erro ao processar o arquivo: " + e.getMessage());
        }
    }

}