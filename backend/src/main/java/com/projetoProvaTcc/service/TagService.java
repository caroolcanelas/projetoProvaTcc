package com.projetoProvaTcc.service;

import com.projetoProvaTcc.dto.TagDTO;
import com.projetoProvaTcc.entity.*;
import com.projetoProvaTcc.exception.ModelException;
import com.projetoProvaTcc.mapper.TagMapper;
import com.projetoProvaTcc.repository.QuestaoRepository;
import com.projetoProvaTcc.repository.TagRepository;
import com.projetoProvaTcc.repository.TopicoRepository;
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
public class TagService {

    @Autowired
    private TagRepository repository;

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private QuestaoRepository questaoRepository;

    @Transactional
    public TagDTO salvar(TagDTO dto) throws ModelException {

        List<Topico> topicos = topicoRepository.findAllByNomeIn(dto.getConjTopicosAderentes());

        List<Long> idsQuestoes = dto.getConjQuestoes() != null
                ? dto.getConjQuestoes().stream()
                .map(Integer::longValue)
                .collect(Collectors.toList())
                : List.of();

        List<Questao> questoes = questaoRepository.findAllById(idsQuestoes);

        Tag tag = TagMapper.toEntity(dto, topicos, questoes);
        Tag salva = repository.save(tag);
        return TagMapper.toDTO(salva);
    }

    public List<TagDTO> buscarTodasTags() {
        return repository.findAll().stream()
                .map(TagMapper::toDTO)
                .collect(Collectors.toList());
    }

    public TagDTO buscarPorId(int id) {
        return repository.findById((long) id)
                .map(TagMapper::toDTO)
                .orElse(null);
    }

    @Transactional
    public TagDTO atualizar(Long id, TagDTO dto) throws ModelException {
        Tag tag = repository.findById(id).orElse(null);
        if (tag == null) {
            return null;
        }

        // Atualiza os campos recebidos no map
        if (dto.getTagName() != null) {
            tag.setTagName(dto.getTagName());
        }

        if (dto.getAssunto() != null) {
            tag.setAssunto(dto.getAssunto());
        }

        // Atualiza os tópicos aderentes (por nome)
        if (dto.getConjTopicosAderentes() != null && !dto.getConjTopicosAderentes().isEmpty()) {
            List<String> nomesTopicos = dto.getConjTopicosAderentes();
            List<Topico> topicos = topicoRepository.findAllByNomeIn(nomesTopicos);
            tag.setConjTopicosAderentes(topicos);
        }

        // Atualiza as questões relacionadas (por ID)
        if (dto.getConjQuestoes() != null && !dto.getConjQuestoes().isEmpty()) {
            List<Long> idsQuestoes = dto.getConjQuestoes()
                    .stream()
                    .map(Integer::longValue)
                    .collect(Collectors.toList());
            List<Questao> questoes = questaoRepository.findAllById(idsQuestoes);
            tag.setConjQuestoes(questoes);
        }

        Tag salvo = repository.save(tag);
        return TagMapper.toDTO(salvo);
    }


    public boolean deletarTagPorId(long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional
    public void adicionarQuestaoNaTag(List<String> nomesTags, int idQuestao) throws ModelException {

        List<Tag> tags = repository.findAllByTagNameIn(nomesTags);
        if (tags.isEmpty()) {
            throw new ModelException("Nenhuma tag encontrada com os nomes fornecidos.");
        }
        Questao questao = questaoRepository.findById((long) idQuestao)
                .orElseThrow(() -> new ModelException("Questão não encontrada com ID: " + idQuestao));

        for (Tag tag : tags) {
            if (!tag.getConjQuestoes().contains(questao)) {
                tag.getConjQuestoes().add(questao);
                questao.getConjTags().add(tag);
                questaoRepository.save(questao);
            }
        }
    }

    @Transactional
    public void removerQuestaoDasTags(List<String> nomesTags, int idQuestao) throws ModelException {
        List<Tag> tags = repository.findAllByTagNameIn(nomesTags);
        if (tags.isEmpty()) {
            throw new ModelException("Nenhuma tag encontrada com os nomes fornecidos.");
        }

        Questao questao = questaoRepository.findById((long) idQuestao)
                .orElseThrow(() -> new ModelException("Questão não encontrada com ID: " + idQuestao));

        for (Tag tag : tags) {
            if (tag.getConjQuestoes().contains(questao)) {
                // Remove dos dois lados
                tag.getConjQuestoes().remove(questao);
                questao.getConjTags().remove(tag);

                questaoRepository.save(questao);
            }
        }
    }

    @Transactional
    public void adicionarTopicosNaTag(String nomeTag, List<String> nomesTopicos) throws ModelException {
        List<Tag> tags = repository.findAllByTagNameIn(List.of(nomeTag));
        if (tags.isEmpty()) {
            throw new ModelException("Tag não encontrada com o nome: " + nomeTag);
        }
        Tag tag = tags.get(0);

        List<Topico> topicos = topicoRepository.findAllByNomeIn(nomesTopicos);
        if (topicos.isEmpty()) {
            throw new ModelException("Nenhum tópico encontrado com os nomes fornecidos.");
        }

        for (Topico topico : topicos) {
            if (!tag.getConjTopicosAderentes().contains(topico)) {
                tag.getConjTopicosAderentes().add(topico);
            }
        }

        repository.save(tag);
    }

    @Transactional
    public void removerTopicosDaTag(String nomeTag, List<String> nomesTopicos) throws ModelException {
        List<Tag> tags = repository.findAllByTagNameIn(List.of(nomeTag));
        if (tags.isEmpty()) {
            throw new ModelException("Tag não encontrada com o nome: " + nomeTag);
        }
        Tag tag = tags.get(0);

        List<Topico> topicos = topicoRepository.findAllByNomeIn(nomesTopicos);
        if (topicos.isEmpty()) {
            throw new ModelException("Nenhum tópico encontrado com os nomes fornecidos.");
        }

        tag.getConjTopicosAderentes().removeAll(topicos);

        repository.save(tag);
    }



    public void importarTagsViaCsv(MultipartFile file) throws Exception {
        //buffer reader le o arquivo linha a linha
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            //inicia lista vazia
            List<Tag> tags = new ArrayList<>();

            String linha; //cada linha
            boolean primeiraLinha = true; //para pular o cabeçalho

            while ((linha = reader.readLine()) != null) { //enquanto ainda tiver linhas
                if (primeiraLinha) { // se for a primeira linha, pulamos e continuamos
                    primeiraLinha = false;
                    continue;
                }

                String[] dados = linha.split(","); //cada linha é separada por vírgula

                Tag tag = new Tag();
                tag.setTagName(dados[0].replace("\"", ""));
                tag.setAssunto(dados[1].replace("\"", ""));

                tags.add(tag);
            }

            repository.saveAll(tags);
        } catch (IOException e) {
            throw new Exception("Erro ao ler o arquivo CSV", e);
        } catch (Exception e) {
            throw new Exception("Erro ao processar dados do CSV: " + e.getMessage(), e);
        }

    }
}
