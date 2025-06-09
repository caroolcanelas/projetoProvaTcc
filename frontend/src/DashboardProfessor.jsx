import { useEffect, useState } from "react";

export default function DashboardProfessor() {
  const [professor, setProfessor] = useState(null);

  useEffect(() => {
    const dados = localStorage.getItem("professor");
    if (dados) {
      setProfessor(JSON.parse(dados));
    }
  }, []);

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

        {/* Lado direito: opções */}
        <div className="flex flex-col gap-4 w-96">
          <div className="bg-white p-4 rounded shadow">
            <h2 className="text-lg font-bold mb-2 text-[#5a3e2b]">Ações Rápidas</h2>
            <button className="bg-[#a87c6f] text-white px-4 py-2 rounded hover:bg-[#946a5f]">
              Adicionar nova questão
            </button>
          </div>

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
