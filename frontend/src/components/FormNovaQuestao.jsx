
import { useEffect, useState } from "react";
import { Editor } from "@tinymce/tinymce-react";
import OpcaoQuestao from "./OpcaoQuestao";
import TagsInput from "./TagsInput";

export default function FormNovaQuestao({ professor, onQuestaoSalva, modoEdicao = false, questaoInicial = null }) {
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
    if (modoEdicao && questaoInicial) {
      setInstrucaoInicial(questaoInicial.instrucaoInicial || "");
      setSuporte(questaoInicial.suporte || "");
      setComando(questaoInicial.comando || "");
      setNivel(questaoInicial.nivel || "FACIL");
      setTipo(questaoInicial.tipo || "RESPOSTA_UNICA");
      setValidada(questaoInicial.validada || false);
      setOpcoes(
        questaoInicial.conjOpcoes && questaoInicial.conjOpcoes.length > 0
          ? questaoInicial.conjOpcoes
          : [{ conteudo: "", correta: false, conjRecursos: [] }]
      );
      setTags(
        questaoInicial.conjTags && questaoInicial.conjTags.length > 0
          ? questaoInicial.conjTags
          : [{ tagName: "", assunto: "" }]
      );
    }
  }, [modoEdicao, questaoInicial]);

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
      conjTags: tags.filter(tag => tag.tagName.trim() !== "")
    };

    try {
      if (modoEdicao) {
        onQuestaoSalva(novaQuestao);
      } else {
        const response = await fetch("http://localhost:8080/api/questao", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(novaQuestao),
        });

        if (response.ok) {
          alert("Questão salva com sucesso!");
          onQuestaoSalva();
        } else {
          const erro = await response.text();
          alert("Erro ao salvar questão: " + erro);
        }
      }
    } catch (err) {
      alert("Erro na comunicação com o servidor: " + err.message);
    }
  };

  return (
    <div className="form-nova-questao">
      <h3 className="titulo-secundario">{modoEdicao ? "Editar Questão" : "Nova Questão"}</h3>

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

      <OpcaoQuestao opcoes={opcoes} setOpcoes={setOpcoes} />
      <TagsInput tags={tags} setTags={setTags} />

      <button onClick={enviarQuestao} className="btn-salvar">
        {modoEdicao ? "Salvar alterações" : "Salvar questão"}
      </button>
    </div>
  );
}