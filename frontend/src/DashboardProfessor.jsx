import { useEffect, useState } from "react";
import { Editor } from "@tinymce/tinymce-react";

export default function DashboardProfessor() {
  const [professor, setProfessor] = useState(null);
  const [mostrarFormulario, setMostrarFormulario] = useState(false);
  const [enunciado, setEnunciado] = useState("");
  const [nivel, setNivel] = useState("BASICA");
  const [opcoes, setOpcoes] = useState([
    { conteudo: "", correta: false },
    { conteudo: "", correta: false },
  ]);

  useEffect(() => {
    const dados = localStorage.getItem("professor");
    if (dados) {
      setProfessor(JSON.parse(dados));
    }
  }, []);

  const adicionarOpcao = () => {
    setOpcoes([...opcoes, { conteudo: "", correta: false }]);
  };

  const atualizarOpcao = (index, campo, valor) => {
    const novas = [...opcoes];
    novas[index][campo] = valor;
    setOpcoes(novas);
  };

  const enviarQuestao = () => {
    const novaQuestao = {
      enunciado,
      nivel,
      opcoes,
      professorValidador: professor?.matricula,
    };
    console.log("Questão criada:", novaQuestao);
    alert("Questão simulada no console (integração futura)");
    setMostrarFormulario(false);
  };

  return (
    <div className="min-h-screen bg-[#fdfaf4] flex items-center justify-center font-sans">
      <div className="bg-[#fef4e8] border border-[#dfc8b5] shadow-lg rounded-lg p-8 flex gap-12">
        {/* Lado esquerdo: dados do professor */}
        <div className="w-64 bg-white p-4 rounded shadow relative">
          <div className="absolute -top-4 left-1/2 -translate-x-1/2 bg-[#d7bfa3] px-3 py-1 text-white rounded">
            Professor
          </div>
          {professor && (
            <div className="mt-6">
              <p><strong>Nome:</strong> {professor.nome}</p>
              <p><strong>Email:</strong> {professor.email}</p>
              <p><strong>Matrícula:</strong> {professor.matricula}</p>
            </div>
          )}
        </div>

        {/* Lado direito: opções e formulario */}
        <div className="flex flex-col gap-4 w-[600px]">
          <div className="bg-white p-4 rounded shadow">
            <h2 className="text-lg font-bold mb-2 text-[#5a3e2b]">Ações Rápidas</h2>
            <button
              onClick={() => setMostrarFormulario(true)}
              className="bg-[#a87c6f] text-white px-4 py-2 rounded hover:bg-[#946a5f]"
            >
              Adicionar nova questão
            </button>
          </div>

          {mostrarFormulario && (
            <div className="bg-[#fff] border border-[#ddd] p-4 rounded shadow">
              <h3 className="text-md font-bold text-[#5a3e2b] mb-2">Nova Questão</h3>
              <label className="block text-sm font-medium">Enunciado</label>
              <Editor
                apiKey="m4g95hooo3d5yju8qbccwha9ihsyjtg2ii7xyl83t13ffyru"
                value={enunciado}
                init={{ height: 200 }}
                onEditorChange={(nova) => setEnunciado(nova)}
              />

              <label className="block mt-4 text-sm font-medium">Nível</label>
              <select
                className="border rounded px-2 py-1"
                value={nivel}
                onChange={(e) => setNivel(e.target.value)}
              >
                <option value="BASICA">Básica</option>
                <option value="INTERMEDIARIA">Intermediária</option>
                <option value="AVANCADA">Avançada</option>
              </select>

              <div className="mt-4">
                <label className="block text-sm font-medium">Opções</label>
                {opcoes.map((op, idx) => (
                  <div key={idx} className="mb-2">
                    <Editor
                      apiKey="m4g95hooo3d5yju8qbccwha9ihsyjtg2ii7xyl83t13ffyru"
                      value={op.conteudo}
                      init={{ height: 100 }}
                      onEditorChange={(nova) => atualizarOpcao(idx, "conteudo", nova)}
                    />
                    <label className="inline-flex items-center gap-2 mt-1">
                      <input
                        type="checkbox"
                        checked={op.correta}
                        onChange={(e) => atualizarOpcao(idx, "correta", e.target.checked)}
                      />
                      Correta?
                    </label>
                  </div>
                ))}
                <button
                  onClick={adicionarOpcao}
                  className="text-sm text-blue-600 mt-2"
                >
                  + Adicionar opção
                </button>
              </div>

              <button
                onClick={enviarQuestao}
                className="mt-4 bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700"
              >
                Salvar questão
              </button>
            </div>
          )}

          <div className="bg-[#fffaf3] p-4 rounded shadow border border-dashed border-[#d3bda5]">
            <p className="text-sm text-[#5a3e2b]">
              Mais funcionalidades virão aqui, como consulta de questões, edição e dashboard de desempenho.
            </p>
          </div>
        </div>
      </div>
    </div>
  );
}