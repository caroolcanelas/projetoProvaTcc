package com.projetoProvaTcc.service;

import com.projetoProvaTcc.dto.DisciplinaDTO;
import com.projetoProvaTcc.entity.Disciplina;
import com.projetoProvaTcc.entity.Topico;
import com.projetoProvaTcc.exception.ModelException;
import com.projetoProvaTcc.mapper.DisciplinaMapper;
import com.projetoProvaTcc.repository.DisciplinaRepository;
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
public class DisciplinaService {


    @Autowired
    private DisciplinaRepository repository;

    @Autowired
    private TopicoRepository topicoRepository;

    //service precisa pegar as informações vindas pelo endpoint e atraves do JpaRepository, salvar no banco
    //anotação para se caso der certo, salva a info no banco, se falhar ao final, tudo que foi feito no metodo é desfeito
    @Transactional
    public DisciplinaDTO salvar(DisciplinaDTO dto) throws ModelException {
        // Transforma lista de IDs de tópicos para lista de entidades Topico
        List<Topico> topicos = dto.getConjTopicos().stream()
                .map(id -> {
                    try {
                        return topicoRepository.findById(id.longValue())
                                .orElseThrow(() -> new ModelException("Tópico com ID " + id + " não encontrado"));
                    } catch (ModelException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());

        Disciplina disciplina = DisciplinaMapper.toEntity(dto, topicos);

        topicos.forEach(t -> {
            try {
                t.setDisciplina(disciplina);
            } catch (ModelException e) {
                throw new RuntimeException(e);
            }
        });
        disciplina.setConjTopicos(topicos);

        Disciplina salva = repository.save(disciplina);
        return DisciplinaMapper.toDTO(salva);
    }

    public List<DisciplinaDTO> buscarTodasDisciplinas() {
        return repository.findAllWithTopicos().stream()
                .map(DisciplinaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public DisciplinaDTO atualizar(int idAtual, DisciplinaDTO dto) throws ModelException {
        Disciplina disciplina = repository.findById((long) idAtual)
                .orElseThrow(() -> new ModelException("Disciplina com ID " + idAtual + " não encontrada"));

        // Atualiza o código
        if (dto.getCodigo() != null) {
            disciplina.setCodigo(dto.getCodigo());
        }

        if (dto.getNome() != null) {
            disciplina.setNome(dto.getNome());
        }

        if (dto.getNumCreditos() != null) {
            disciplina.setNumCreditos(dto.getNumCreditos());
        }

        if (dto.getObjetivoGeral() != null) {
            disciplina.setObjetivoGeral(dto.getObjetivoGeral());
        }

        if (dto.getConjTopicos() != null) {
            List<Topico> topicos = dto.getConjTopicos().stream()
                    .map(tid -> {
                        try {
                            return topicoRepository.findById(Long.valueOf(tid))
                                    .orElseThrow(() -> new ModelException("Tópico com ID " + tid + " não encontrado"));
                        } catch (ModelException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .collect(Collectors.toList());

            // Atualiza o vínculo de cada tópico com a disciplina
            for (Topico t : topicos) {
                t.setDisciplina(disciplina);
            }

            disciplina.setConjTopicos(topicos);
        }

        Disciplina salva = repository.save(disciplina);
        return DisciplinaMapper.toDTO(salva);
    }


    public boolean deletarDisciplinaPorId(Long id) {
        if (repository.existsById((long) Math.toIntExact(id))) {
            repository.deleteById((long) Math.toIntExact(id));
            return true;
        }
        return false;
    }

    public void adicionarTopicoNaDisciplina(int idDisciplina, Topico topico) throws ModelException {
        //encontra a disciplina pelo id
        Disciplina disciplina = repository.findById((long) idDisciplina)
                .orElseThrow(() -> new ModelException("Disciplina não encontrada"));

        //confere se o tópico já existe no banco
        if (topico.getId() != 0) {
            Topico topicoExistente = topicoRepository.findById((long) topico.getId())
                    .orElseThrow(() -> new ModelException("Tópico com ID " + topico.getId() + " não encontrado"));

            // Impede que a gente relacione o mesmo tópico duas vezes
            if (disciplina.getConjTopicos().contains(topicoExistente)) {
                throw new ModelException("Tópico já está associado a esta disciplina.");
            }

            //se o topico já existe adiciona na disciplina
            topicoExistente.setDisciplina(disciplina);
            disciplina.addTopico(topicoExistente);
        } else {
            // se não existe ele cria um tópico novo
            topico.setDisciplina(disciplina);
            disciplina.addTopico(topico);
        }

        //salva no banco
        repository.save(disciplina);
    }

    public void removerTopicoDaDisciplina(int idDisciplina, int idTopico) throws ModelException {
        Disciplina disciplina = repository.findById((long) idDisciplina)
                .orElseThrow(() -> new ModelException("Disciplina não encontrada"));

        Topico topico = topicoRepository.findById((long)idTopico)
                .orElseThrow(() -> new ModelException("Tópico não encontrado"));

        disciplina.removeTopico(topico); // remove topico

        repository.save(disciplina); // salva a alteracao
    }


    public DisciplinaDTO buscarPorId(int id) {
        Disciplina disciplina = repository.findById((long) id).orElse(null);
        if (disciplina == null) return null;
        return DisciplinaMapper.toDTO(disciplina);
    }

    //import em batch
    public void importarDisiplinasViaCsv(MultipartFile file) throws Exception {

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))){
            List<Disciplina> disciplinas = new ArrayList<>();

            String linha;
            boolean primeiraLinha = true;

            while ((linha = reader.readLine()) != null) {
                if (primeiraLinha) {
                    primeiraLinha = false;
                    continue;
                }


                String[] dados = linha.split(",");

                Disciplina disciplina = new Disciplina();
                disciplina.setCodigo(dados[0].replace("\"", ""));
                disciplina.setNome(dados[1].replace("\"", ""));
                disciplina.setNumCreditos(Integer.parseInt(dados[2].replace("\"", "")));
                disciplina.setObjetivoGeral(dados[3].replace("\"", ""));

                disciplinas.add(disciplina);
            }

            repository.saveAll(disciplinas);
        } catch (IOException e){
            throw new Exception("Erro ao ler o arquivo CSV", e);
        } catch (Exception e){
            throw new Exception("Erro ao processar dados do CSV: " + e.getMessage(), e);

        }

    }
}
