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
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
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

    public void adicionarRecursoNaOpcao(Long idOpcao, Long idRecurso) throws Exception {
        Opcao opcao = repository.findById(idOpcao)
                .orElseThrow(() -> new Exception("Questão não encontrada"));

        Recurso recurso = recursoRepository.findById(Math.toIntExact(idRecurso))
                .orElseThrow(() -> new Exception("Recurso não encontrado"));

        opcao.addRecurso(recurso);
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

    @Transactional
    public OpcaoDTO atualizarl(int id, OpcaoDTO dto) throws ModelException {
        Opcao opcao = repository.findById((long) id)
                .orElseThrow(() -> new ModelException("Opção com ID " + id + " não encontrada."));

        // Atualiza apenas os campos não-nulos
        if (dto.getConteudo() != null) {
            opcao.setConteudo(dto.getConteudo());
        }

        if (dto.getCorreta() != null) {
            opcao.setCorreta(dto.getCorreta());
        }

        if (dto.getConjRecursos() != null) {
            List<Recurso> recursos = dto.getConjRecursos().stream()
                    .map(rDto -> recursoRepository.findById(rDto.getId())
                            .orElseGet(() -> {
                                Recurso novo = new Recurso();
                                try {
                                    novo.setConteudo(rDto.getConteudo());
                                } catch (ModelException e) {
                                    throw new RuntimeException(e);
                                }
                                return recursoRepository.save(novo);
                            }))
                    .collect(Collectors.toList());

            recursos.forEach(r -> r.setOpcao(opcao));
            opcao.setConjRecursos(recursos);
        }

        Opcao atualizado = repository.save(opcao);
        return OpcaoMapper.toDTO(atualizado);
    }

    public OpcaoDTO buscarPorId(int id) {
        Opcao opcao = repository.findById((long)id).orElse(null);
        if(opcao == null) return null;
        return OpcaoMapper.toDTO(opcao);
    }

    @Transactional
    public void importarOpcoesViaCsv(MultipartFile file) throws Exception {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            List<Opcao> opcoes = new ArrayList<>();
            boolean primeiraLinha = true;

            String linha;
            while ((linha = reader.readLine()) != null) {
                if (primeiraLinha) {
                    primeiraLinha = false;
                    continue; // Ignora cabeçalho
                }

                String[] dados = linha.split(","); // Formato: conteudo,correta (ex: "Texto da opção,true")
                Opcao opcao = new Opcao();
                opcao.setConteudo(dados[0].replace("\"", "").trim());
                opcao.setCorreta(Boolean.parseBoolean(dados[1].replace("\"", "").trim()));

                opcoes.add(opcao);
            }
            repository.saveAll(opcoes);
        } catch (IOException e) {
            throw new Exception("Erro ao ler arquivo CSV: " + e.getMessage());
        } catch (ModelException e) {
            throw new Exception("Erro de validação: " + e.getMessage());
        }
    }
}
