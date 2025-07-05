import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import FormNovaQuestao from "./FormNovaQuestao";
import "../styles/dashboardProfessor.css";

export default function EditarQuestao() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [questao, setQuestao] = useState(null);
  const [professor, setProfessor] = useState(null);
  const [carregando, setCarregando] = useState(true);

  useEffect(() => {
    const dados = localStorage.getItem("professor");
    if (dados) {
      setProfessor(JSON.parse(dados));
    }
  }, []);

  useEffect(() => {
    const carregar = async () => {
      try {
        const resposta = await fetch(`http://localhost:8080/api/questao/${id}`);
        if (resposta.ok) {
          const dados = await resposta.json();
          setQuestao(dados);
        } else {
          alert("Erro ao buscar questão para edição.");
          navigate("/dashboard");
        }
      } catch (erro) {
        alert("Erro na comunicação com o servidor.");
        navigate("/dashboard");
      } finally {
        setCarregando(false);
      }
    };
    carregar();
  }, [id, navigate]);

  const salvarEdicao = async (questaoEditada) => {
    try {
      const resposta = await fetch(`http://localhost:8080/api/questao/${id}`, {
        method: "PATCH",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(questaoEditada)
      });

      if (resposta.ok) {
        alert("Questão atualizada com sucesso!");
        navigate("/dashboard");
      } else {
        const erro = await resposta.text();
        alert("Erro ao atualizar questão: " + erro);
      }
    } catch (err) {
      alert("Erro na comunicação com o servidor: " + err.message);
    }
  };

  if (carregando) return <p>Carregando questão...</p>;
  if (!questao) return null;

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
            <h2 className="titulo">Editar Questão</h2>
            <button onClick={() => navigate("/dashboard")} className="btn-adicionar">
              Voltar para dashboard
            </button>
          </div>

          <div className="questao-card">
            <FormNovaQuestao
              questaoInicial={questao}
              modoEdicao={true}
              onQuestaoSalva={salvarEdicao}
            />
          </div>
        </div>
      </div>
    </div>
  );
}
