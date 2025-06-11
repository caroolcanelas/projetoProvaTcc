package com.projetoProvaTcc.service;

import com.projetoProvaTcc.dto.DisciplinaDTO;
import com.projetoProvaTcc.dto.ProfessorDTO;
import com.projetoProvaTcc.entity.Disciplina;
import com.projetoProvaTcc.entity.Professor;
import com.projetoProvaTcc.exception.ModelException;
import com.projetoProvaTcc.mapper.ProfessorMapper;
import com.projetoProvaTcc.repository.DisciplinaRepository;
import com.projetoProvaTcc.repository.ProfessorRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfessorService {

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private DisciplinaRepository disciplinaRepository;

    public ProfessorDTO salvar(ProfessorDTO dto) throws ModelException {
        Professor professor = ProfessorMapper.toEntity(dto);
        return ProfessorMapper.toDTO(professorRepository.save(professor));
    }

    public List<ProfessorDTO> listarTodos() {
        return professorRepository.findAll().stream()
                .map(ProfessorMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ProfessorDTO buscarPorId(Long id) {
        return professorRepository.findById(id)
                .map(ProfessorMapper::toDTO)
                .orElse(null);
    }

    public boolean deletarPorId(Long id) {
        if (professorRepository.existsById(id)) {
            professorRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional
    public void adicionarDisciplina(Long professorId, Long disciplinaId) throws ModelException {
        Professor professor = professorRepository.findById(professorId)
                .orElseThrow(() -> new ModelException("Professor não encontrado"));

        Disciplina disciplina = disciplinaRepository.findById(disciplinaId)
                .orElseThrow(() -> new ModelException("Disciplina não encontrada"));

        professor.addDisciplina(disciplina); // <-- aqui usamos o método da entidade
        professorRepository.save(professor);
    }

    @Transactional
    public void removerDisciplina(Long professorId, Long disciplinaId) throws ModelException {
        Professor professor = professorRepository.findById(professorId)
                .orElseThrow(() -> new ModelException("Professor não encontrado"));

        Disciplina disciplina = disciplinaRepository.findById(disciplinaId)
                .orElseThrow(() -> new ModelException("Disciplina não encontrada"));

        // validação opcional
        if (disciplina.getProfessor() == null || disciplina.getProfessor().getId() != professorId) {
            throw new ModelException("A disciplina não está associada a este professor.");
        }

        professor.removeDisciplina(disciplina);
        professorRepository.save(professor);
    }

    //metodo para login
    public Professor login(String email, String senha) throws ModelException {
        Professor professor = professorRepository.findByEmail(email)
                .orElseThrow(() -> new ModelException("Email não encontrado"));

        if (!professor.getSenha().equals(senha)) {
            throw new ModelException("Senha incorreta");
        }

        return professor;
    }


}
