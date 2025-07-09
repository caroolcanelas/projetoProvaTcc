import { useEffect, useState } from "react";
import { Editor } from "@tinymce/tinymce-react";
import TagsInput from "./TagsInput";
import OpcaoQuestao from "./OpcaoQuestao";
import { useNavigate } from "react-router-dom";

export default function FormNovaQuestao({ professor, onQuestaoSalva, modoEdicao = false, questaoInicial = null }) {
  const [instrucaoInicial, setInstrucaoInicial] = useState("");
  const [suporte, setSuporte] = useState("");
  const [comando, setComando] = useState("");
  const [nivel, setNivel] = useState("FACIL");
  const [tipo, setTipo] = useState("RESPOSTA_UNICA");
  const [validada, setValidada] = useState(false);
  const [opcoes, setOpcoes] = useState([{ conteudo: "", correta: false, conjRecursos: [] }]);
  const [tags, setTags] = useState([{ tagName: "", assunto: "" }]);

  const navigate = useNavigate();

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

    const tagsValidas = tags.filter(tag => tag.tagName.trim() !== "");

    if (modoEdicao && questaoInicial?.id) {
      const questaoId = questaoInicial.id;

      const dadosAtualizados = {
        instrucaoInicial,
        suporte,
        comando,
        nivel,
        tipo,
        validada,
      };

      const houveAlteracao =
        instrucaoInicial !== questaoInicial.instrucaoInicial ||
        suporte !== questaoInicial.suporte ||
        comando !== questaoInicial.comando ||
        nivel !== questaoInicial.nivel ||
        tipo !== questaoInicial.tipo ||
        validada !== questaoInicial.validada;

      if (!houveAlteracao) {
        alert("Nenhuma alteração nos dados principais da questão foi detectada.");
        navigate("/dashboard");
        return;
      }

      try {
        const patch = await fetch(`http://localhost:8080/api/questao/${questaoId}`, {
          method: "PATCH",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(dadosAtualizados),
        });

        if (!patch.ok) {
          const erro = await patch.text();
          alert("Erro ao atualizar dados da questão: " + erro);
          return;
        }

        alert("Questão atualizada com sucesso!");
        navigate("/dashboard");
      } catch (err) {
        alert("Erro na comunicação com o servidor: " + err.message);
      }
    } else {
      const novaQuestao = {
        instrucaoInicial,
        suporte,
        comando,
        nivel,
        tipo,
        validada,
        matriculaProfessorValidador: professor?.matricula,
        conjOpcoes: opcoesValidas,
        conjTags: tagsValidas,
        conjQuestoesDerivadas: [],
      };

      try {
        const response = await fetch("http://localhost:8080/api/questao", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(novaQuestao),
        });

        if (response.ok) {
          alert("Questão salva com sucesso!");
          navigate("/dashboard");
        } else {
          const erro = await response.text();
          alert("Erro ao salvar questão: " + erro);
        }
      } catch (err) {
        alert("Erro na comunicação com o servidor: " + err.message);
      }
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
      <input type="checkbox" checked={validada} onChange={(e) => setValidada(e.target.checked)} />

      <OpcaoQuestao opcoes={opcoes} setOpcoes={setOpcoes} modoEdicao={modoEdicao} questaoId={questaoInicial?.id} />
      <TagsInput tags={tags} setTags={setTags} modoEdicao={modoEdicao} questaoId={questaoInicial?.id} />

      <button onClick={enviarQuestao} className="btn-salvar">
        {modoEdicao ? "Salvar alterações" : "Salvar questão"}
      </button>
    </div>
  );
}
