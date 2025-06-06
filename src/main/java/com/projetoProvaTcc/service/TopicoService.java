package com.projetoProvaTcc.service;

import com.projetoProvaTcc.dto.TopicoDTO;
import com.projetoProvaTcc.entity.Disciplina;
import com.projetoProvaTcc.entity.Tag;
import com.projetoProvaTcc.entity.Topico;
import com.projetoProvaTcc.exception.ModelException;
import com.projetoProvaTcc.mapper.TopicoMapper;
import com.projetoProvaTcc.repository.DisciplinaRepository;
import com.projetoProvaTcc.repository.TagRepository;
import com.projetoProvaTcc.repository.TopicoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;



@Service
public class TopicoService {

    @Autowired
    private TopicoRepository repository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private DisciplinaRepository disciplinaRepository;

    @Autowired
    private TopicoRepository topicoRepository;


    @Transactional
    public TopicoDTO salvar(TopicoDTO dto) throws ModelException {
        Disciplina disciplina = disciplinaRepository.findById(Long.valueOf(dto.getDisciplina()))
                .orElseThrow(() -> new ModelException("Disciplina não encontrada"));

        List<Topico> subTopicos = dto.getConjSubTopicos().stream()
                .map(id -> {
                    try {
                        return topicoRepository.findById(Long.valueOf(id))
                                .orElseThrow(() -> new ModelException("Subtópico " + id + " não encontrado"));
                    } catch (ModelException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());

        List<Tag> tags = tagRepository.findAllByTagNameIn(dto.getConjTags());

        Topico topico = TopicoMapper.toEntity(dto, disciplina, subTopicos, tags);

        Topico salvo = topicoRepository.save(topico);

        // Agora atualize o lado dono da relação
        for (Tag tag : tags) {
            tag.getConjTopicosAderentes().add(salvo); // LADO DONO
            tagRepository.save(tag); // salva a tabela intermediária
        }

        return TopicoMapper.toDTO(salvo);
    }

    public List<TopicoDTO> buscarTodosTopicos() {
        return repository.findAll().stream()
                .map(TopicoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public  TopicoDTO buscarPorId(int id) {
        Topico topico = repository.findByIdComTags((long) id);
        if (topico == null) return null;
        return TopicoMapper.toDTO(topico);
    }

    @Transactional
    public TopicoDTO atualizar(int id, TopicoDTO dto) throws ModelException {
        Topico existente = topicoRepository.findById((long) id)
                .orElseThrow(() -> new ModelException("Tópico com id " + id + " não encontrado."));

        // Atualizar campos simples se vierem no DTO
        if (dto.getNome() != null) {
            existente.setNome(dto.getNome());
        }
        if (dto.getConteudo() != null) {
            existente.setConteudo(dto.getConteudo());
        }
        if (dto.getNumOrdem() != null) {
            existente.setNumOrdem(dto.getNumOrdem());
        }

        // Atualizar Disciplina se fornecido
        if (dto.getDisciplina() != null) {
            Disciplina disciplina = disciplinaRepository.findById(Long.valueOf(dto.getDisciplina()))
                    .orElseThrow(() -> new ModelException("Disciplina não encontrada."));
            existente.setDisciplina(disciplina);
        }

        // Atualizar subtópicos se fornecidos
        if (dto.getConjSubTopicos() != null) {
            List<Topico> subTopicos = dto.getConjSubTopicos().stream()
                    .map(idSub -> {
                        try {
                            return topicoRepository.findById(Long.valueOf(idSub))
                                    .orElseThrow(() -> new ModelException("Subtópico " + idSub + " não encontrado"));
                        } catch (ModelException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .collect(Collectors.toList());
            existente.setConjSubTopicos(subTopicos);
        }

        // Atualizar tags se fornecidas
        if (dto.getConjTags() != null) {
            List<Tag> tags = tagRepository.findAllByTagNameIn(dto.getConjTags());
            existente.setConjTags(tags);
            for (Tag tag : tags) {
                if (!tag.getConjTopicosAderentes().contains(existente)) {
                    tag.getConjTopicosAderentes().add(existente);
                    tagRepository.save(tag);
                }
            }
        }

        Topico atualizado = topicoRepository.save(existente);
        return TopicoMapper.toDTO(atualizado);
    }

    public boolean deletarTopicoPorId(long id) throws ModelException {
        Topico topico = repository.findById(id).orElse(null);
        if (topico == null) {
            return false;
        }

        // Remove o topico da lista de cada tag que o referencia
        List<Tag> tagsRelacionadas = topico.getConjTags();
        for (Tag tag : tagsRelacionadas) {
            tag.removeTopicoAderente(topico); // Garante que a tag pare de referenciar o topico
        }

        // Agora sim pode remover
        repository.delete(topico);
        return true;
    }

    @Transactional
    public void adicionarSubTopicoEmTopico(int idTopico, List<Integer> idsSubTopicos) throws ModelException {
        Topico topicoPai = topicoRepository.findById((long) idTopico)
                .orElseThrow(() -> new ModelException("Tópico principal não encontrado."));

        if (idsSubTopicos == null || idsSubTopicos.isEmpty()) {
            throw new ModelException("Lista de IDs de subtópicos vazia ou nula.");
        }

        for (Integer idSub : idsSubTopicos) {
            Topico subTopico = topicoRepository.findById((long) idSub)
                    .orElseThrow(() -> new ModelException("Subtópico com ID " + idSub + " não encontrado"));
            topicoPai.addSubTopico(subTopico);
        }

        topicoRepository.save(topicoPai);
    }



    public void removerSubtopico(int idTopico, int idSubtopico) throws ModelException {
        Topico topicoPai = topicoRepository.findById((long) idTopico)
                .orElseThrow(() -> new ModelException("Tópico principal não encontrado."));

        Topico subTopico = topicoRepository.findById((long) idSubtopico)
                .orElseThrow(() -> new ModelException("Subtópico não encontrado."));

        boolean removido = topicoPai.removeSubTopico(subTopico);
        if (!removido) {
            throw new ModelException("O subtópico informado não está associado a esse tópico.");
        }

        topicoRepository.save(topicoPai);
    }

    // add e remove de conjTags:
    @Transactional
    public void adicionarTagsAoTopico(int idTopico, List<String> nomesTags) throws ModelException {
        Topico topico = topicoRepository.findById((long) idTopico)
                .orElseThrow(() -> new ModelException("Tópico não encontrado com ID: " + idTopico));

        List<Tag> tags = tagRepository.findAllByTagNameIn(nomesTags);

        if (tags.size() != nomesTags.size()) {
            throw new ModelException("Uma ou mais tags não foram encontradas no banco.");
        }

        for (Tag tag : tags) {
            if (!tag.getConjTopicosAderentes().contains(topico)) {
                tag.getConjTopicosAderentes().add(topico); // lado dono
                tagRepository.save(tag); // importante: salva o dono
            }
        }
    }

    public void removerTagDoTopico (int idTopico, List<String> nomeTag) throws ModelException {
        //procurar o id do topico
        Topico topico = repository.findById((long)idTopico)
                .orElseThrow(()-> new ModelException("Topico não encontrado"));

        List<Tag> tags = tagRepository.findAllByTagNameIn(nomeTag);

        if (tags.isEmpty()) {
            throw new ModelException("Nenhuma tag válida foi encontrada para remoção.");
        }

        for (Tag tag : tags) {
            topico.removeTag(tag); // usa seu método da entidade
        }

         topicoRepository.save(topico);

    }

}
