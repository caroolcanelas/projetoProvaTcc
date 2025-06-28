export default function TagsInput({ tags, setTags }) {
  const handleChange = (index, field, value) => {
    const novas = [...tags];
    novas[index][field] = value;
    setTags(novas);
  };

  return (
    <div className="tags-container">
      <label className="label">Tags</label>
      {tags.map((tag, index) => (
        <div key={index} className="tag-linha">
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
  );
}