package com.projetoProvaTcc.service;

import com.projetoProvaTcc.dto.QuestaoDTO;
import com.projetoProvaTcc.entity.*;
import com.projetoProvaTcc.exception.ModelException;
import com.projetoProvaTcc.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.projetoProvaTcc.mapper.QuestaoMapper;
import org.springframework.web.multipart.MultipartFile;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

    @Autowired
    private ProfessorRepository professorRepository;


    @Transactional
    //salvar nova opção
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
                                        novaOpcao.setCorreta(opcaoDTO.getCorreta());
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
                                .orElseGet(() -> {
                                    Recurso novoRecurso = new Recurso();
                                    try {
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
        Questao questao = QuestaoMapper.toEntity(dto, opcoes, recursos, tags, questaoDerivada,professorRepository );
        questao.validarQuestao(); //valida a questão antes de salvar
        Questao salva = questaoRepository.save(questao);
        return QuestaoMapper.toDTO(salva);
    }

    public List<QuestaoDTO> buscarTodasQuestoes() {
        return questaoRepository.findAll().stream()
                .map(QuestaoMapper::toDTO)
                .collect(Collectors.toList());
    }

    //deletar opção por id
    public boolean deletarQuestaoPorId(long id) {
        if (questaoRepository.existsById(id)) {
            questaoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    //buscar opção por id
    public QuestaoDTO buscarPorId(int id) {
        Questao questao = questaoRepository.findById((long) id).orElse(null);
        if (questao == null) return null;
        return QuestaoMapper.toDTO(questao);
    }

    //adicionar recurso na questão
    public void adicionarRecursoNaQuestao(Long idQuestao, Long idRecurso) throws Exception {
        Questao questao = questaoRepository.findById(idQuestao)
                .orElseThrow(() -> new Exception("Questão não encontrada"));

        Recurso recurso = recursoRepository.findById(Math.toIntExact(idRecurso))
                .orElseThrow(() -> new Exception("Recurso não encontrado"));

        if (!questao.getConjRecursos().contains(recurso)) {
            questao.addRecurso(recurso);
            questaoRepository.save(questao);
        }
    }

    //remover recurso da questão
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

    //adicionar opção na questão (apenas opção existente)
    public void adicionarOpcaoNaQuestao(int idQuestao, int idOpcao) throws Exception {
        System.out.println("Buscando questão com ID: " + idQuestao);

        Questao questao = questaoRepository.findById((long) idQuestao)
                .orElseThrow(() -> new ModelException("Questão não encontrada"));

        Opcao opcao = opcaoRepository.findById((long) idOpcao)
                .orElseThrow(() -> new ModelException("Opção não encontrada"));

        //checa se tem duplicata, se não tem salva
        if (!questao.getConjOpcoes().contains(opcao)) {
            questao.addOpcao(opcao);
            questaoRepository.save(questao);
        }

    }

    //remover opcao da questão
    public void removerOpcaoDaQuestao(int idQuestao, int idOpcao) throws ModelException {
        Questao questao = questaoRepository.findById((long) idQuestao)
                .orElseThrow(() -> new ModelException("Questão não encontrada"));

        Opcao opcao = opcaoRepository.findById((long) idOpcao)
                .orElseThrow(() -> new ModelException("Opção não encontrado"));

        // aqui a gente nao apaga a opção do banco, só remove ele da questão mesmo
        boolean removido = questao.removeOpcao(opcao);
        if (!removido) {
            throw new ModelException("Opção não está vinculado a essa questao.");
        }

        questaoRepository.save(questao);
    }

    //adicionar tag da questãp
    public void adicionarTagsNaQuestao(int idQuestao, List<String> nomesTags) throws ModelException {
        Questao questao = questaoRepository.findById((long) idQuestao)
                .orElseThrow(() -> new ModelException("Questão não encontrada com ID: " + idQuestao));

        List<Tag> tags = tagRepository.findAllByTagNameIn(nomesTags);

        if (tags.size() != nomesTags.size()) {
            throw new ModelException("Uma ou mais tags não foram encontradas no banco.");
        }

        for (Tag tag : tags) {
            if (!questao.getConjTags().contains(tag)) {
                questao.addTag(tag);
            }
        }

        questaoRepository.save(questao);
    }

    //remover tag da questão
    public void removerTagDaQuestao(int idQuestao, List<String> nomesTags) throws ModelException {
        //procurar o id da questão
        Questao questao = questaoRepository.findById((long) idQuestao)
                .orElseThrow(() -> new ModelException("Questão não encontrado"));

        List<Tag> tags = tagRepository.findAllByTagNameIn(nomesTags);

        if (tags.isEmpty()) {
            throw new ModelException("Nenhuma tag válida foi encontrada para remoção.");
        }

        for (Tag tag : tags) {
            questao.removeTag(tag); // usa seu método da entidade
        }

        questaoRepository.save(questao);


    }

    public void adicionarQuestaoDerivadaEmQuestao(int idQuestao, List<Integer> idsQuestoesDerivadas) throws ModelException {
        Questao questaoPai = questaoRepository.findById((long) idQuestao)
                .orElseThrow(()-> new ModelException("Questão principal não encontrada"));

        if(idsQuestoesDerivadas == null || idsQuestoesDerivadas.isEmpty() ){
            throw new ModelException("Lista de IDs de questão derivada vazia ou nula");
        }

        for(Integer idQDerivada : idsQuestoesDerivadas){
            Questao questaoDerivada = questaoRepository.findById((long) idQDerivada)
                    .orElseThrow(()-> new ModelException("Questão derivada com ID" + idQDerivada + "não encontrado"));
            questaoPai.addQuestaoDerivada(questaoDerivada);
        }
        questaoRepository.save(questaoPai);
    }

    public void removerQuestaoDerivadaDeQuestao(int idQuestao, int idQuestaoDerivada) throws ModelException {
        Questao questaoPai = questaoRepository.findById((long) idQuestao)
                .orElseThrow(()-> new ModelException("Questão não encontrada."));

        Questao questaoDerivada = questaoRepository.findById((long) idQuestaoDerivada)
                .orElseThrow(()-> new ModelException("Questão Derivada não encontrada."));

        boolean removido = questaoPai.removeQuestaoDerivada(questaoDerivada);
        if(!removido){
            throw new ModelException("A questão derivada informada não esta associada a essa questão");
        }

        questaoRepository.save(questaoPai);

    }

    //update
    public void atualizarParcialQuestao(int idQuestao, QuestaoDTO dto) throws ModelException {
        Questao questao = questaoRepository.findById((long) idQuestao)
                .orElseThrow(() -> new ModelException("Questão não encontrada com ID: " + idQuestao));

        if (dto.getInstrucaoInicial() != null)
            questao.setInstrucaoInicial(dto.getInstrucaoInicial());

        if (dto.getSuporte() != null)
            questao.setSuporte(dto.getSuporte());

        if (dto.getComando() != null)
            questao.setComando(dto.getComando());

        if (dto.getNivel() != null)
            questao.setNivel(dto.getNivel());

        if (dto.getTipo() != null)
            questao.setTipo(dto.getTipo());

        if (dto.getValidada() != null)
            questao.setValidada(dto.getValidada());

        questaoRepository.save(questao);
    }


    public void importarQuestoesViaCsv(MultipartFile file) throws Exception {

        //buffer reader le o arquivo linha a linha
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            List<Questao> questoes = new ArrayList<>();

            String linha; //cada linha
            boolean primeiraLinha = true; //para pular o cabeçalho

            while ((linha = reader.readLine()) != null) { //enquanto ainda tiver linhas
                if (primeiraLinha) { // se for a primeira linha, pulamos e continuamos
                    primeiraLinha = false;
                    continue;
                }

                String[] dados = linha.split(","); //cada linha é separada por vírgula

                Questao questao = new Questao();
                questao.setInstrucaoInicial(dados[0].replace("\"", ""));
                questao.setSuporte(dados[1].replace("\"", ""));
                questao.setComando(dados[2].replace("\"", ""));
                questao.setNivel(NivelQuestao.valueOf(dados[3].trim()));
                questao.setTipo(TipoQuestao.valueOf(dados[4].trim()));
                questao.setValidada(Boolean.parseBoolean(dados[5].trim()));

                Professor professor = professorRepository.findByMatricula(Integer.parseInt(dados[6]))
                        .orElseThrow(() -> new ModelException("Professor não encontrado"));
                questao.setProfessorValidador(professor);

                questoes.add(questao);
            }

            questaoRepository.saveAll(questoes);
        } catch (IOException e) {
            throw new Exception("Erro ao ler o arquivo CSV", e);
        } catch (Exception e) {
            throw new Exception("Erro ao processar dados do CSV: " + e.getMessage(), e);
        }
    }
}
