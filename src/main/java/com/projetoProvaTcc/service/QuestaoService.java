package com.projetoProvaTcc.service;

import com.projetoProvaTcc.dto.QuestaoDTO;
import com.projetoProvaTcc.entity.Opcao;
import com.projetoProvaTcc.entity.Questao;
import com.projetoProvaTcc.entity.Recurso;
import com.projetoProvaTcc.exception.ModelException;
import com.projetoProvaTcc.repository.OpcaoRepository;
import com.projetoProvaTcc.repository.QuestaoRepository;
import com.projetoProvaTcc.repository.RecursoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
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

        //a questão só busca se já existe no banco pelp id, se for nullo tudo bem, não é obrigatório
//        List<Questao> questoesDerivadas = dto.getConjQuestoesDerivadas() == null ?
//               new ArrayList<>() :
//                dto.getConjQuestoesDerivadas().stream()
//                      .map(questaoDTO -> questaoRepository.findById(Long.valueOf(questaoDTO.getId()))
//                               .orElseThrow(() -> new RuntimeException("Questão derivada não encontrada: " + questaoDTO.getId())))
//                       .collect(Collectors.toList());

        Questao questao = QuestaoMapper.toEntity(dto, opcoes, recursos);
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
    }
