package com.projetoProvaTcc.service;

import com.projetoProvaTcc.dto.RecursoDTO;
import com.projetoProvaTcc.entity.Recurso;
import com.projetoProvaTcc.exception.ModelException;
import com.projetoProvaTcc.repository.RecursoRepository;
import jakarta.transaction.Transactional;
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
}
