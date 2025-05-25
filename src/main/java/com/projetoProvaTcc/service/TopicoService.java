package com.projetoProvaTcc.service;

import com.projetoProvaTcc.dto.TopicoDTO;
import com.projetoProvaTcc.entity.Topico;
import com.projetoProvaTcc.exception.ModelException;
import com.projetoProvaTcc.mapper.TopicoMapper;
import com.projetoProvaTcc.repository.TopicoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class TopicoService {

    @Autowired
    private TopicoRepository repository;

    @Transactional
    public TopicoDTO salvar(TopicoDTO dto) throws ModelException {

        Topico topico = TopicoMapper.toEntity(dto);
        Topico salva = repository.save(topico);
        return TopicoMapper.toDTO(salva);
    }

    public List<TopicoDTO> buscarTodosTopicos() {
        return repository.findAll().stream()
                .map(TopicoMapper::toDTO)
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
