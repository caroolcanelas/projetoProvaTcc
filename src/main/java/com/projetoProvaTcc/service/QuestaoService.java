package com.projetoProvaTcc.service;

import com.projetoProvaTcc.dto.QuestaoDTO;
import com.projetoProvaTcc.entity.Opcao;
import com.projetoProvaTcc.entity.Questao;
import com.projetoProvaTcc.exception.ModelException;
import com.projetoProvaTcc.repository.OpcaoRepository;
import com.projetoProvaTcc.repository.QuestaoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.projetoProvaTcc.mapper.QuestaoMapper;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestaoService {

    @Autowired
    private QuestaoRepository questaoRepository;

    @Autowired
    private OpcaoRepository opcaoRepository;

    @Transactional
    public QuestaoDTO salvar(QuestaoDTO dto) throws ModelException {
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
        Questao questao = QuestaoMapper.toEntity(dto, opcoes);
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
