package com.projetoProvaTcc.service;

import com.projetoProvaTcc.dto.ProfessorDTO;
import com.projetoProvaTcc.entity.Professor;
import com.projetoProvaTcc.exception.ModelException;
import com.projetoProvaTcc.mapper.ProfessorMapper;
import com.projetoProvaTcc.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfessorService {

    @Autowired
    private ProfessorRepository repository;

    public ProfessorDTO salvar(ProfessorDTO dto) throws ModelException {
        Professor professor = ProfessorMapper.toEntity(dto);
        return ProfessorMapper.toDTO(repository.save(professor));
    }

    public List<ProfessorDTO> listarTodos() {
        return repository.findAll().stream()
                .map(ProfessorMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ProfessorDTO buscarPorId(Long id) {
        return repository.findById(id)
                .map(ProfessorMapper::toDTO)
                .orElse(null);
    }

    public boolean deletarPorId(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
