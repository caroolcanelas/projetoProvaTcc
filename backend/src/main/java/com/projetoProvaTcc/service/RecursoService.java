package com.projetoProvaTcc.service;

import com.projetoProvaTcc.dto.RecursoDTO;
import com.projetoProvaTcc.entity.Opcao;
import com.projetoProvaTcc.entity.Recurso;
import com.projetoProvaTcc.exception.ModelException;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class RecursoService {

    @Autowired
    private RecursoRepository recursoRepository;

    @Autowired
    private OpcaoRepository opcaoRepository;

    public RecursoDTO salvarArquivo(MultipartFile file) throws IOException, ModelException {
        byte[] conteudo = file.getBytes();

        Recurso recurso = new Recurso();
        recurso.setConteudo(conteudo); // aqui já valida se está vazio

        recurso = recursoRepository.save(recurso);

        return new RecursoDTO(recurso.getId()); // só retorna ID
    }

    public byte[] buscarConteudoPorId(int id) throws Exception {
        Recurso recurso = recursoRepository.findById(id)
                .orElseThrow(() -> new Exception("Recurso não encontrado"));
        return recurso.getConteudo();
    }

    public boolean deletarRecursoPorId(int id) throws ModelException {
        if (recursoRepository.existsById(id)) {
            recursoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    //update
    public void atualizarParcialRecurso(MultipartFile arquivo, int idRecurso) throws ModelException {
        Recurso recurso = recursoRepository.findById(idRecurso)
                .orElseThrow(() -> new ModelException("Recurso não encontrada com ID: " + idRecurso));

        try {
            if (arquivo != null && !arquivo.isEmpty()) {
                recurso.setConteudo(arquivo.getBytes());
            }

            recursoRepository.save(recurso);

        } catch (IOException e) {
            throw new ModelException("Erro ao processar o arquivo: " + e.getMessage());
        }
    }

    //Importar múltiplos recursos (arquivos binários + metadados) em lote a partir de:
    //Um CSV (contendo metadados e referências aos arquivos).
    //Um ZIP (contendo os arquivos binários em si).

    //Extrai arquivos do ZIP
    //Lê o arquivo ZIP e mapeia cada arquivo interno em um Map<String, byte[]> (nome do arquivo → conteúdo binário).
    @Transactional
    public void importarRecursosViaCsv(MultipartFile csvFile, MultipartFile zipFile) throws Exception {
        // 1. Extrai arquivos do ZIP para um mapa (nome -> bytes)
        Map<String, byte[]> arquivosDoZip = extrairArquivosDoZip(zipFile);

        // 2. Processa o CSV linha a linha
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(csvFile.getInputStream()))) {
            List<Recurso> recursos = new ArrayList<>();
            boolean primeiraLinha = true;
            String linha;

            while ((linha = reader.readLine()) != null) {
                if (primeiraLinha) {
                    primeiraLinha = false;
                    continue; // Ignora cabeçalho
                }

                String[] dados = linha.split(","); // Formato: nome_arquivo_no_zip,tipo,opcional:opcao_id
                String nomeArquivo = dados[0].trim();
                byte[] conteudo = arquivosDoZip.get(nomeArquivo);

                if (conteudo == null) {
                    throw new ModelException("Arquivo '" + nomeArquivo + "' não encontrado no ZIP");
                }

                // Cria e configura o recurso
                Recurso recurso = new Recurso();
                recurso.setConteudo(conteudo);

                // Opcional: associa a uma Opcao (se especificado no CSV)
                if (dados.length > 2 && !dados[2].isBlank()) {
                    int opcaoId = Integer.parseInt(dados[2].trim());
                    Opcao opcao = opcaoRepository.findById((long) opcaoId)
                            .orElseThrow(() -> new ModelException("Opção não encontrada: " + opcaoId));
                    recurso.setOpcao(opcao);
                }

                recursos.add(recurso);
            }

            recursoRepository.saveAll(recursos);
        }
    }

    // Método auxiliar para extrair arquivos do ZIP
    private Map<String, byte[]> extrairArquivosDoZip(MultipartFile zipFile) throws IOException {
        Map<String, byte[]> arquivos = new HashMap<>();
        try (ZipInputStream zipIn = new ZipInputStream(zipFile.getInputStream())) {
            ZipEntry entry;
            while ((entry = zipIn.getNextEntry()) != null) {
                if (!entry.isDirectory()) {
                    arquivos.put(entry.getName(), zipIn.readAllBytes());
                }
            }
        }
        return arquivos;
    }

}