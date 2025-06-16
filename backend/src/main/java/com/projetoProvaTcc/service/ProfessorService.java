package com.projetoProvaTcc.service;

import com.projetoProvaTcc.dto.DisciplinaDTO;
import com.projetoProvaTcc.dto.ProfessorDTO;
import com.projetoProvaTcc.entity.Disciplina;
import com.projetoProvaTcc.entity.Professor;
import com.projetoProvaTcc.entity.Questao;
import com.projetoProvaTcc.exception.ModelException;
import com.projetoProvaTcc.mapper.ProfessorMapper;
import com.projetoProvaTcc.repository.DisciplinaRepository;
import com.projetoProvaTcc.repository.ProfessorRepository;
import com.projetoProvaTcc.repository.QuestaoRepository;
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

    @Autowired
    private QuestaoRepository questaoRepository;

    public ProfessorDTO salvar(ProfessorDTO dto) throws ModelException {
        // 1. Obter lista de IDs das disciplinas do DTO
        List<Long> idsDisciplinas = dto.getConjDisciplinas()
                .stream()
                .map(Integer::longValue)
                .collect(Collectors.toList());

        List<Disciplina> disciplinas = (!idsDisciplinas.isEmpty())
                ? disciplinaRepository.findAllById(idsDisciplinas)
                : null;

        // 3. Converter DTO para entidade usando as disciplinas carregadas
        Professor professor = ProfessorMapper.toEntity(dto, disciplinas);

        // 4. Salvar o professor e retornar o DTO
        Professor professorSalvo = professorRepository.save(professor);

        return ProfessorMapper.toDTO(professorSalvo);
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

    @Transactional
    public void adicionarQuestaoAProfessor(int matriculaProfessor, Long idQuestao) throws ModelException {
        Professor professor = professorRepository.findByMatricula(matriculaProfessor)
                .orElseThrow(() -> new ModelException("Professor não encontrado com matrícula: " + matriculaProfessor));

        Questao questao = questaoRepository.findById(idQuestao)
                .orElseThrow(() -> new ModelException("Questão não encontrada com id: " + idQuestao));

        // Associa questão ao professor
        questao.setProfessorValidador(professor);
        professor.getConjQuestoesValidadas().add(questao);

        // Salva as entidades para persistir a associação
        questaoRepository.save(questao);
        professorRepository.save(professor);
    }

    @Transactional
    public void removerQuestaoDeProfessor(int matriculaProfessor, Long idQuestao) throws ModelException {
        Professor professor = professorRepository.findByMatricula(matriculaProfessor)
                .orElseThrow(() -> new ModelException("Professor não encontrado com matrícula: " + matriculaProfessor));

        Questao questao = questaoRepository.findById(idQuestao)
                .orElseThrow(() -> new ModelException("Questão não encontrada com id: " + idQuestao));

        if (questao.getProfessorValidador() != null
                && questao.getProfessorValidador().getMatricula().equals(matriculaProfessor)) {

            // Remove associação
            questao.setProfessorValidador(null);
            professor.getConjQuestoesValidadas().remove(questao);

            // Salva para persistir mudança
            questaoRepository.save(questao);
            professorRepository.save(professor);
        } else {
            throw new ModelException("Questão não está associada a esse professor");
        }
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
