package com.projetoProvaTcc.service;

import com.projetoProvaTcc.dto.OpcaoDTO;
import com.projetoProvaTcc.entity.Opcao;
import com.projetoProvaTcc.exception.ModelException;
import com.projetoProvaTcc.mapper.OpcaoMapper;
import com.projetoProvaTcc.repository.OpcaoRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OpcaoService {

@Autowired
private OpcaoRepository repository;

@Transactional
public OpcaoDTO salvar(OpcaoDTO dto) throws ModelException {
    Opcao entidade = OpcaoMapper.toEntity(dto);
    Opcao salvaOpcao = repository.save(entidade);
    return OpcaoMapper.toDTO(salvaOpcao);
}

public List<OpcaoDTO> buscarTodasOpcoes() {
        List<Opcao> lista = repository.findAll();
        return lista.stream()
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
