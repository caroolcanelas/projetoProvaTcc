import { useEffect, useState } from "react";
import { Editor } from "@tinymce/tinymce-react";
import "./DashboardProfessor.css";

export default function DashboardProfessor() {
  const [professor, setProfessor] = useState(null);
  const [mostrarFormulario, setMostrarFormulario] = useState(false);
  const [instrucaoInicial, setInstrucaoInicial] = useState("");
  const [suporte, setSuporte] = useState("");
  const [comando, setComando] = useState("");
  const [nivel, setNivel] = useState("FACIL");
  const [tipo, setTipo] = useState("RESPOSTA_UNICA");
  const [validada, setValidada] = useState(false);
  const [opcoes, setOpcoes] = useState([
    { conteudo: "", correta: false, conjRecursos: [] }
  ]);
const [tags, setTags] = useState([{ tagName: "", assunto: "" }]);


  useEffect(() => {
    const dados = localStorage.getItem("professor");
    if (dados) {
      setProfessor(JSON.parse(dados));
    }
  }, []);

  const enviarQuestao = async () => {
    const opcoesValidas = opcoes.filter(op => op.conteudo.trim() !== "");
    if (opcoesValidas.length === 0) {
      alert("Adicione pelo menos uma opção com conteúdo.");
      return;
    }

    const novaQuestao = {
      instrucaoInicial,
      suporte,
      comando,
      nivel,
      tipo,
      validada,
      matriculaProfessorValidador: professor?.matricula,
      conjOpcoes: opcoesValidas,
      conjQuestoesDerivadas: [],
        conjTags: tags.filter(tag => tag.tagName.trim() !== ""),
    };

    try {
      const response = await fetch("http://localhost:8080/api/questao", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(novaQuestao),
      });

      if (response.ok) {
        const data = await response.json();
        alert("Questão salva com sucesso!");
        console.log("Questão salva:", data);
        // resetar
        setMostrarFormulario(false);
        setInstrucaoInicial("");
        setSuporte("");
        setComando("");
        setNivel("FACIL");
        setTipo("RESPOSTA_UNICA");
        setValidada(false);
        setOpcoes([{ conteudo: "", correta: false }]);
        setTags([{ tagName: "", assunto: "" }]);
      } else {
        const erro = await response.text();
        alert("Erro ao salvar questão: " + erro);
      }
    } catch (err) {
      alert("Erro na comunicação com o servidor: " + err.message);
    }
  };

  return (
    <div className="dashboard-container">
      <div className="card">
        <div className="professor-info">
          <div className="professor-label">Professor</div>
          {professor && (
            <div className="professor-dados">
              <p><strong>Nome:</strong> {professor.nome}</p>
              <p><strong>Email:</strong> {professor.email}</p>
              <p><strong>Matrícula:</strong> {professor.matricula}</p>
            </div>
          )}
        </div>

        <div className="conteudo-formulario">
          <div className="form-actions">
            <h2 className="titulo">Ações Rápidas</h2>
            <button onClick={() => setMostrarFormulario(true)} className="btn-adicionar">
              Adicionar nova questão
            </button>
          </div>

          {mostrarFormulario && (
            <div className="form-nova-questao">
              <h3 className="titulo-secundario">Nova Questão</h3>

              <label className="label">Instrução Inicial</label>
              <Editor
                apiKey="m4g95hooo3d5yju8qbccwha9ihsyjtg2ii7xyl83t13ffyru"
                value={instrucaoInicial}
                init={{ height: 150 }}
                onEditorChange={(nova) => setInstrucaoInicial(nova)}
              />

              <label className="label">Suporte</label>
              <Editor
                apiKey="m4g95hooo3d5yju8qbccwha9ihsyjtg2ii7xyl83t13ffyru"
                value={suporte}
                init={{ height: 150 }}
                onEditorChange={(nova) => setSuporte(nova)}
              />

              <label className="label">Comando</label>
              <Editor
                apiKey="m4g95hooo3d5yju8qbccwha9ihsyjtg2ii7xyl83t13ffyru"
                value={comando}
                init={{ height: 150 }}
                onEditorChange={(nova) => setComando(nova)}
              />

              <label className="label">Nível</label>
              <select className="input" value={nivel} onChange={(e) => setNivel(e.target.value)}>
                <option value="FACIL">Fácil</option>
                <option value="INTERMEDIARIA">Intermediária</option>
                <option value="DIFICIL">Difícil</option>
              </select>

              <label className="label">Tipo</label>
              <select className="input" value={tipo} onChange={(e) => setTipo(e.target.value)}>
                <option value="RESPOSTA_UNICA">Resposta única</option>
                <option value="RESPOSTA_MULTIPLA">Resposta múltipla</option>
                <option value="ASSERCAO_RAZAO">Asserção razão</option>
              </select>

              <label className="label">Validada</label>
              <input
                type="checkbox"
                checked={validada}
                onChange={(e) => setValidada(e.target.checked)}
              />

              <div className="opcoes-container">
                <label className="label">Opções</label>
                {opcoes.map((opcao, index) => (
                  <div key={index} className="opcao-bloco">
                    <div className="opcao-linha">
                      <input
                        type="text"
                        className="input flex-1"
                        placeholder={`Opção ${index + 1}`}
                        value={opcao.conteudo}
                        onChange={(e) => {
                          const novas = [...opcoes];
                          novas[index].conteudo = e.target.value;
                          setOpcoes(novas);
                        }}
                      />
                      <label className="checkbox-label">
                        <input
                          type="checkbox"
                          checked={opcao.correta}
                          onChange={(e) => {
                            const novas = [...opcoes];
                            novas[index].correta = e.target.checked;
                            setOpcoes(novas);
                          }}
                        />
                        &nbsp;Correta
                      </label>
                    </div>

                    <div className="mt-2">
                      <label className="text-sm block">Recursos</label>
                      <input
                        type="file"
                        multiple
                        onChange={(e) => {
                          const novas = [...opcoes];
                          const arquivos = Array.from(e.target.files).map((file) => ({
                            nome: file.name,
                            tipo: file.type,
                            tamanho: file.size,
                            // conteúdo em base64 poderia ser adicionado aqui se necessário
                          }));
                          novas[index].conjRecursos = arquivos;
                          setOpcoes(novas);
                        }}
                      />
                      {opcao.conjRecursos && opcao.conjRecursos.length > 0 && (
                        <ul className="text-xs mt-1 ml-2 list-disc">
                          {opcao.conjRecursos.map((r, i) => (
                            <li key={i}>{r.nome} ({(r.tamanho / 1024).toFixed(1)} KB)</li>
                          ))}
                        </ul>
                      )}
                    </div>
                  </div>
                ))}



                <button
                  type="button"
                  onClick={() => setOpcoes([...opcoes, { conteudo: "", correta: false, conjRecursos: [] }])}
                  className="btn-add-opcao"
                >
                  + Adicionar opção
                </button>
              </div>


<div className="tags-container">
  <label className="label">Tags</label>
  {tags.map((tag, index) => (
    <div key={index} className="tag-linha">
      <input
        type="text"
        className="input"
        placeholder="Nome da tag"
        value={tag.tagName}
        onChange={(e) => {
          const novas = [...tags];
          novas[index].tagName = e.target.value;
          setTags(novas);
        }}
      />
      <input
        type="text"
        className="input"
        placeholder="Assunto relacionado"
        value={tag.assunto}
        onChange={(e) => {
          const novas = [...tags];
          novas[index].assunto = e.target.value;
          setTags(novas);
        }}
      />
    </div>
  ))}

  <button
    type="button"
    onClick={() => setTags([...tags, { tagName: "", assunto: "" }])}
    className="btn-add-opcao"
  >
    + Adicionar tag
  </button>
</div>


              <button onClick={enviarQuestao} className="btn-salvar">
                Salvar questão
              </button>
            </div>
          )}

          <div className="info-box">
            <p>
              Mais funcionalidades virão aqui, como consulta de questões, edição e dashboard de desempenho.
            </p>
          </div>
        </div>
      </div>
    </div>
  );
}
