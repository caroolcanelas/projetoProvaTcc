package com.projetoProvaTcc.service;

import com.projetoProvaTcc.dto.QuestaoDTO;
import com.projetoProvaTcc.entity.*;
import com.projetoProvaTcc.exception.ModelException;
import com.projetoProvaTcc.repository.OpcaoRepository;
import com.projetoProvaTcc.repository.QuestaoRepository;
import com.projetoProvaTcc.repository.RecursoRepository;
import com.projetoProvaTcc.repository.TagRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.projetoProvaTcc.mapper.QuestaoMapper;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestaoService {

    @Autowired
    private QuestaoRepository questaoRepository;

    @Autowired
    private OpcaoRepository opcaoRepository;

    @Autowired
    private RecursoRepository recursoRepository;

    @Autowired
    private TagRepository tagRepository;


    @Transactional
    public QuestaoDTO salvar(QuestaoDTO dto) throws ModelException {

        //se a opção nao existir ele cria
        List<Opcao> opcoes = dto.getConjOpcoes().stream()
            .map(opcaoDTO ->
                opcaoRepository.findById(Long.valueOf(opcaoDTO.getId()))
                        .orElseGet(() -> {
                            // Criar nova Opcao se não existir
                            Opcao novaOpcao = new Opcao();
                            try {
                                novaOpcao.setConteudo(opcaoDTO.getConteudo());
                            } catch (ModelException e) {
                                throw new RuntimeException(e);
                            }
                            try {
                                novaOpcao.setCorreta(opcaoDTO.isCorreta());
                            } catch (ModelException e) {
                                throw new RuntimeException(e);
                            }
                            return opcaoRepository.save(novaOpcao);
                        })
        )
                .collect(Collectors.toList());

        //se o recurso não existir ele (confesso que não sei muito como fazer esse post pra testar
        List<Recurso> recursos = dto.getConjRecursos().stream()
                .map(recursoDTO ->
                        recursoRepository.findById(recursoDTO.getId())
                                .orElseGet(()->{
                                    Recurso novoRecurso = new Recurso();
                                    try{
                                        novoRecurso.setConteudo(recursoDTO.getConteudo());
                                    } catch (ModelException e) {
                                        throw new RuntimeException(e);
                                    }
                                    novoRecurso.setId(recursoDTO.getId());
                                    return recursoRepository.save(novoRecurso);
                                })
                )
                .collect(Collectors.toList());

        List<Tag> tags = dto.getConjTags().stream()
                .map(tagDTO ->
                        tagRepository.findById(Long.valueOf(tagDTO.getId()))
                                .orElseGet(() -> {
                                    // Criar nova Tag se não existir
                                    Tag novaTag = new Tag();
                                    try {
                                        novaTag.setTagName(tagDTO.getTagName());
                                    } catch (ModelException e) {
                                        throw new RuntimeException(e);
                                    }
                                    try {
                                        novaTag.setAssunto(tagDTO.getAssunto());
                                    } catch (ModelException e) {
                                        throw new RuntimeException(e);
                                    }
                                    return tagRepository.save(novaTag);
                                })
                )
                .collect(Collectors.toList());

        //busca questoes derivadas por id

        List<Questao> questaoDerivada = dto.getConjQuestoesDerivadas().stream()
                .map(id -> {
                    try {
                        return questaoRepository.findById(Long.valueOf(id))
                                .orElseThrow(() -> new ModelException("Questão derivada " + id + " não encontrado"));
                    } catch (ModelException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());


        // Agora converte para Entidade
        Questao questao = QuestaoMapper.toEntity(dto, opcoes, recursos, tags, questaoDerivada);
        questao.validarQuestao(); //valida a questão antes de salvar
        Questao salva = questaoRepository.save(questao);
        return QuestaoMapper.toDTO(salva);
    }

    public List<QuestaoDTO> buscarTodasQuestoes() {
        return questaoRepository.findAll().stream()
                .map(QuestaoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public boolean deletarQuestaoPorId(long id) {
        if (questaoRepository.existsById(id)) {
            questaoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public QuestaoDTO buscarPorId(int id) {
        Questao questao = questaoRepository.findById((long) id).orElse(null);
        if(questao == null) return null;
        return QuestaoMapper.toDTO(questao);
    }

    public void adicionarRecurso(Long idQuestao, Long idRecurso) throws Exception {
        Questao questao = questaoRepository.findById(idQuestao)
                .orElseThrow(() -> new Exception("Questão não encontrada"));

        Recurso recurso = recursoRepository.findById(Math.toIntExact(idRecurso))
                .orElseThrow(() -> new Exception("Recurso não encontrado"));

        questao.addRecurso(recurso);
        questaoRepository.save(questao);
    }

    public void removerRecursoDaQuestao(int idQuestao, int idRecurso) throws ModelException {
        Questao questao = questaoRepository.findById((long) idQuestao)
                .orElseThrow(() -> new ModelException("Questão não encontrada"));

        Recurso recurso = recursoRepository.findById(idRecurso)
                .orElseThrow(() -> new ModelException("Recurso não encontrado"));

        // aqui a gente nao apaga o recurso do banco, só remove ele da questão mesmo
        boolean removido = questao.removeRecurso(recurso);
        if (!removido) {
            throw new ModelException("Recurso não está vinculado a essa questao.");
        }

        questaoRepository.save(questao);
    }


}
