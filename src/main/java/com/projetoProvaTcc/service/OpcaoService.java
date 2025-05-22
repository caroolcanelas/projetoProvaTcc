package com.projetoProvaTcc.service;

import com.projetoProvaTcc.dto.OpcaoDTO;
import com.projetoProvaTcc.entity.Opcao;
import com.projetoProvaTcc.entity.Recurso;
import com.projetoProvaTcc.exception.ModelException;
import com.projetoProvaTcc.mapper.OpcaoMapper;
import com.projetoProvaTcc.repository.OpcaoRepository;
import com.projetoProvaTcc.repository.RecursoRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OpcaoService {

    @Autowired
    private OpcaoRepository repository;

    @Autowired
    private RecursoRepository recursoRepository;

    @Transactional
    public OpcaoDTO salvar(OpcaoDTO dto) throws ModelException {
        List<Recurso> recursos = dto.getConjRecursos().stream()
                .map(id -> recursoRepository.findById(id.getId())
                        .orElseThrow(() -> new RuntimeException("Recurso não encontrado: " + id)))
                .collect(Collectors.toList());

        Opcao opcao = OpcaoMapper.toEntity(dto, recursos);
        recursos.forEach(r -> r.setOpcao(opcao));  // associa recurso à opção

        Opcao salva = repository.save(opcao);
        return OpcaoMapper.toDTO(salva);
    }

    public List<OpcaoDTO> buscarTodasOpcoes() {
        return repository.findAll().stream()
                .map(OpcaoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public boolean deletarOpcaoPorId(long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
