export default function OpcaoQuestao({ opcoes, setOpcoes }) {
  const handleChange = (index, field, value) => {
    const novas = [...opcoes];
    novas[index][field] = value;
    setOpcoes(novas);
  };

  const handleRecursos = (index, files) => {
    const arquivos = Array.from(files).map((file) => ({
      nome: file.name,
      tipo: file.type,
      tamanho: file.size,
    }));
    const novas = [...opcoes];
    novas[index].conjRecursos = arquivos;
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
              placeholder={`Opção ${index + 1}`}
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
          </div>

          <div className="mt-2">
            <label className="text-sm block">Recursos</label>
            <input
              type="file"
              multiple
              onChange={(e) => handleRecursos(index, e.target.files)}
            />
            {opcao.conjRecursos?.length > 0 && (
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
  );
}