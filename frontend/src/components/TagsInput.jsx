import { useEffect, useState } from "react";

export default function TagsInput({ tags, setTags, modoEdicao, questaoId }) {
  const [tagsDisponiveis, setTagsDisponiveis] = useState([]);
  const [modoCustom, setModoCustom] = useState(tags.map(() => false));

  useEffect(() => {
    fetch("http://localhost:8080/api/tag")
      .then((res) => res.json())
      .then((data) => setTagsDisponiveis(data))
      .catch((err) => console.error("Erro ao carregar tags:", err));
  }, []);

  const handleChange = (index, field, value) => {
    const novas = [...tags];
    novas[index][field] = value;
    setTags(novas);
  };

  const adicionarNovaTagCustom = () => {
    setTags([...tags, { tagName: "", assunto: "" }]);
    setModoCustom([...modoCustom, true]);
  };

  const toggleModo = (index) => {
    const novoModo = [...modoCustom];
    if (!modoCustom[index]) {
      adicionarNovaTagCustom();
    } else {
      novoModo[index] = false;
      setModoCustom(novoModo);
      handleChange(index, "tagName", "");
      handleChange(index, "assunto", "");
    }
  };

  const handleAdicionar = () => {
    setTags([...tags, { tagName: "", assunto: "" }]);
    setModoCustom([...modoCustom, false]);
  };

  const handleSalvarTag = async (index, tag) => {
    if (!tag.tagName.trim()) return;

    try {
      let tagFinal = tag;

      if (modoCustom[index]) {
        if (!tag.tagName.trim() || !tag.assunto.trim()) {
          alert("Preencha tanto o nome da tag quanto o assunto.");
          return;
        }

        const novaTag = {
          tagName: tag.tagName.trim(),
          assunto: tag.assunto.trim(),
        };

        const resp = await fetch("http://localhost:8080/api/tag", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(novaTag),
        });

        if (!resp.ok) throw new Error("Erro ao criar nova tag");

        tagFinal = await resp.json();
      }

      if (modoEdicao && questaoId && tagFinal.tagName) {
        const respAssoc = await fetch(`http://localhost:8080/api/tag/add-questao/${questaoId}`, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify([tagFinal.tagName]),
        });

        if (!respAssoc.ok) throw new Error("Erro ao associar tag à questão");
      }

      const novas = [...tags];
      novas[index] = tagFinal;
      setTags(novas);
      alert("Tag associada com sucesso!");
    } catch (err) {
      console.error("Erro ao salvar/associar tag:", err);
      alert("Erro ao salvar/associar tag");
    }
  };

  const handleRemover = async (index, tag) => {
    if (modoEdicao && questaoId && tag.tagName) {
      try {
        await fetch(`http://localhost:8080/api/tag/remove-questao/${questaoId}`, {
          method: "DELETE",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify([tag.tagName]),
        });
      } catch (err) {
        console.error("Erro ao remover tag do backend:", err);
      }
    }
    const novas = tags.filter((_, i) => i !== index);
    setTags(novas);
    setModoCustom(modoCustom.filter((_, i) => i !== index));
  };

  return (
    <div className="tags-container">
      <label className="label">Tags</label>

      {tags.map((tag, index) => (
        <div key={index} className="tag-linha">
          {!modoCustom[index] ? (
            <>
              <select
                className="input"
                value={tag.tagName}
                onChange={(e) => {
                  const selecionada = tagsDisponiveis.find(t => t.tagName === e.target.value);
                  handleChange(index, "tagName", selecionada?.tagName || "");
                  handleChange(index, "assunto", selecionada?.assunto || "");
                }}
              >
                <option value="">Selecione uma tag existente</option>
                {tagsDisponiveis.map((t) => (
                  <option key={t.id} value={t.tagName}>
                    {t.tagName} - {t.assunto}
                  </option>
                ))}
              </select>
              <button type="button" className="btn-add-opcao" onClick={() => toggleModo(index)}>
                + Nova tag
              </button>
            </>
          ) : (
            <>
              <input
                type="text"
                className="input"
                placeholder="Nome da tag"
                value={tag.tagName}
                onChange={(e) => handleChange(index, "tagName", e.target.value)}
              />
              <input
                type="text"
                className="input"
                placeholder="Assunto relacionado"
                value={tag.assunto}
                onChange={(e) => handleChange(index, "assunto", e.target.value)}
              />
              <button type="button" className="btn-add-opcao" onClick={() => toggleModo(index)}>
                Cancelar
              </button>
            </>
          )}

          <button
            type="button"
            onClick={() => handleRemover(index, tag)}
            style={{
              backgroundColor: "#e5e7eb",
              border: "none",
              borderRadius: "4px",
              padding: "4px 8px",
              fontWeight: "bold",
              color: "#444",
              cursor: "pointer",
            }}
            title="Remover tag"
          >
            x
          </button>

          <button
            type="button"
            className="btn-add-opcao"
            onClick={() => handleSalvarTag(index, tag)}
          >
            Salvar tag
          </button>
        </div>
      ))}

      <button type="button" onClick={handleAdicionar} className="btn-add-opcao">
        + Adicionar tag existente
      </button>
    </div>
  );
}
