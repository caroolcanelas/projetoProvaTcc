import { useEffect, useState } from "react";

export default function TagsInput({ tags, setTags }) {
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

  const toggleModo = (index) => {
    const novoModo = [...modoCustom];
    novoModo[index] = !novoModo[index];
    setModoCustom(novoModo);

    if (!novoModo[index]) {
      handleChange(index, "tagName", "");
      handleChange(index, "assunto", "");
    }
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
                onChange={(e) => handleChange(index, "tagName", e.target.value)}
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
        </div>
      ))}

      <button
        type="button"
        onClick={() => {
          setTags([...tags, { tagName: "", assunto: "" }]);
          setModoCustom([...modoCustom, false]);
        }}
        className="btn-add-opcao"
      >
        + Adicionar tag
      </button>
    </div>
  );
}
