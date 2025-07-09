import { useState } from "react";

export default function OpcaoQuestao({ opcoes, setOpcoes, modoEdicao, questaoId }) {
  const [novaOpcao, setNovaOpcao] = useState({ conteudo: "", correta: false });

  const handleChange = (index, field, value) => {
    const novas = [...opcoes];
    novas[index][field] = value;
    setOpcoes(novas);
  };

  const handleAdicionar = async () => {
    if (!novaOpcao.conteudo.trim()) return alert("Preencha o conteúdo da opção.");
    if (modoEdicao && questaoId) {
      try {
        const criar = await fetch("http://localhost:8080/api/opcao", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(novaOpcao),
        });
        const nova = await criar.json();

        const associar = await fetch(`http://localhost:8080/api/questao/${questaoId}/opcao/`, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(nova.id),
        });

        if (!associar.ok) throw new Error("Falha ao associar opção à questão.");

        setOpcoes([...opcoes, nova]);
        setNovaOpcao({ conteudo: "", correta: false });
        alert("Opção adicionada com sucesso!");
      } catch (err) {
        alert("Erro: " + err.message);
      }
    } else {
      setOpcoes([...opcoes, novaOpcao]);
      setNovaOpcao({ conteudo: "", correta: false });
    }
  };

  const handleRemover = async (index, idOpcao) => {
    if (modoEdicao && questaoId && idOpcao) {
      try {
        await fetch(`http://localhost:8080/api/questao/${questaoId}/opcao/${idOpcao}`, {
          method: "DELETE",
        });
      } catch (err) {
        console.error("Erro ao remover do backend:", err);
      }
    }
    const novas = opcoes.filter((_, i) => i !== index);
    setOpcoes(novas);
  };

  return (
    <div className="opcoes-container">
      <label className="label">Opções</label>
      {opcoes.map((opcao, index) => (
        <div key={index} className="opcao-bloco">
          <div className="opcao-linha">
            <input
              type="text"
              className="input flex-1"
              value={opcao.conteudo}
              onChange={(e) => handleChange(index, "conteudo", e.target.value)}
            />
            <label className="checkbox-label">
              <input
                type="checkbox"
                checked={opcao.correta}
                onChange={(e) => handleChange(index, "correta", e.target.checked)}
              />
              &nbsp;Correta
            </label>
            <button
              type="button"
              onClick={() => handleRemover(index, opcao.id)}
              style={{
                backgroundColor: "#e5e7eb",
                border: "none",
                borderRadius: "4px",
                padding: "4px 8px",
                fontWeight: "bold",
                color: "#444",
                cursor: "pointer",
              }}
              title="Remover opção"
            >
              x
            </button>
          </div>
        </div>
      ))}

      <div className="opcao-linha">
        <input
          type="text"
          className="input flex-1"
          placeholder="Nova opção"
          value={novaOpcao.conteudo}
          onChange={(e) => setNovaOpcao({ ...novaOpcao, conteudo: e.target.value })}
        />
        <label className="checkbox-label">
          <input
            type="checkbox"
            checked={novaOpcao.correta}
            onChange={(e) => setNovaOpcao({ ...novaOpcao, correta: e.target.checked })}
          />
          &nbsp;Correta
        </label>
        <button type="button" onClick={handleAdicionar} className="btn-add-opcao">
          + Adicionar opção
        </button>
      </div>
    </div>
  );
}
