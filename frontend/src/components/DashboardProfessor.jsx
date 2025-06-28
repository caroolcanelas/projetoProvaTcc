import { useEffect, useState } from "react";
import FormNovaQuestao from "./FormNovaQuestao";
import ListaQuestoesSalvas from "./ListaQuestoesSalvas";
import "../styles/DashboardProfessor.css";

export default function DashboardProfessor() {
  const [professor, setProfessor] = useState(null);
  const [mostrarFormulario, setMostrarFormulario] = useState(false);
  const [mostrarQuestoes, setMostrarQuestoes] = useState(false);
  const [questoesSalvas, setQuestoesSalvas] = useState([]);

  useEffect(() => {
    const dados = localStorage.getItem("professor");
    if (dados) {
      setProfessor(JSON.parse(dados));
    }
  }, []);

  const buscarQuestoesSalvas = async () => {
    try {
      const response = await fetch("http://localhost:8080/api/questao");
      if (response.ok) {
        const data = await response.json();
        setQuestoesSalvas(data);
        setMostrarQuestoes(true);
      } else {
        const erro = await response.text();
        alert("Erro ao buscar questões: " + erro);
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
            <button
              onClick={() => {
                setMostrarFormulario(false);
                buscarQuestoesSalvas();
              }}
              className="btn-ver-questoes"
            >
              Ver questões salvas
            </button>
          </div>

          {mostrarFormulario && (
            <FormNovaQuestao
              professor={professor}
              onQuestaoSalva={() => setMostrarFormulario(false)}
            />
          )}

          {mostrarQuestoes && <ListaQuestoesSalvas questoes={questoesSalvas} />}

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
