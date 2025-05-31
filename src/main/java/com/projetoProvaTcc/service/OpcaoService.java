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

        Opcao opcao = OpcaoMapper.toEntity(dto, recursos);
        recursos.forEach(r -> r.setOpcao(opcao));  // associa recurso à opção

        Opcao salva = repository.save(opcao);
        return OpcaoMapper.toDTO(salva);
    }

    public void adicionarRecursoNaOpcao (int idOpcao, Recurso recurso) throws ModelException{
        //encontrar a opcao pelo id
        Opcao opcao = repository.findById((long) idOpcao)
                .orElseThrow(() -> new ModelException("Opcão não encontrada"));

        //o recurso já existe no banco?
        if(recurso.getId() != 0){
            Recurso recursoExistente = recursoRepository.findById(recurso.getId())
                    .orElseThrow(() -> new ModelException("Recurdo com ID" + recurso.getId() + "não encontrado"));

            //impede que relacione o mesmo recurso duas vezes
            if (opcao.getConjRecursos().contains(recursoExistente)){
                throw new ModelException("Recurso já estã associado a esta opção");
            }

            //se o recurso já existe, adiciona na opção
            recursoExistente.setOpcao(opcao);
            opcao.addRecurso(recursoExistente);
        } else {
            //se não existe cria um novo recurso
            recurso.setOpcao(opcao);
            opcao.addRecurso(recurso);
        }

        //salva no banco
        repository.save(opcao);
    }

    public void removerRecursoDaOpcao(int idOpcao, int idRecurso) throws ModelException {
        Opcao opcao = repository.findById((long) idOpcao)
                .orElseThrow(() -> new ModelException("Opção não encontrada"));

        Recurso recurso = recursoRepository.findById(idRecurso)
                .orElseThrow(() -> new ModelException("Recurso não encontrado"));

        // aqui a gente nao apaga o recurso do banco, só remove ele da opção mesmo
        boolean removido = opcao.removeRecurso(recurso);
        if (!removido) {
            throw new ModelException("Recurso não está vinculado a essa opção.");
        }

        repository.save(opcao);
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

    public OpcaoDTO buscarPorId(int id) {
        Opcao opcao = repository.findById((long)id).orElse(null);
        if(opcao == null) return null;
        return OpcaoMapper.toDTO(opcao);
    }
}
