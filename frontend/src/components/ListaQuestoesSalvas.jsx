import "../styles/dashboardProfessor.css";
import { useNavigate } from "react-router-dom";

export default function ListaQuestoesSalvas({ questoes }) {
  const navigate = useNavigate();

  return (
    <div className="lista-questoes">
      <h3 className="titulo-secundario">Questões Salvas</h3>
      {questoes.length === 0 ? (
        <p>Nenhuma questão cadastrada.</p>
      ) : (
        <div className="grid-questoes">
          {questoes.map((q, index) => (
            <div key={q.id || index} className="questao-card">
              <div className="questao-campo">
                <strong>Instrução:</strong>
                <div dangerouslySetInnerHTML={{ __html: q.instrucaoInicial }} />
              </div>

              <div className="questao-campo">
                <strong>Suporte:</strong>
                <div dangerouslySetInnerHTML={{ __html: q.suporte }} />
              </div>

              <div className="questao-campo">
                <strong>Comando:</strong>
                <div dangerouslySetInnerHTML={{ __html: q.comando }} />
              </div>

              {q.conjOpcoes && q.conjOpcoes.length > 0 && (
                <div className="questao-campo">
                  <strong>Opções:</strong>
                  <ul className="questao-lista">
                    {q.conjOpcoes.map((op, i) => (
                      <li key={i} style={{ display: 'flex', alignItems: 'center', gap: '6px' }}>
                        <span>{op.conteudo}</span>
                        {op.correta && <span style={{ fontWeight: 'bold', color: '#2e7d32' }}>(correta)</span>}
                      </li>
                    ))}
                  </ul>
                </div>
              )}

              {(q.nivel || q.tipo || typeof q.validada === 'boolean' || q.conjTags?.length > 0) && (
                <div className="questao-meta">
                  <small><strong>Nível:</strong> {q.nivel}</small>
                  <small><strong>Tipo:</strong> {q.tipo}</small>
                  <small><strong>Validada:</strong> {q.validada ? "Sim" : "Não"}</small>
                  {q.conjTags && q.conjTags.length > 0 && (
                    <small>
                      <strong>Tags:</strong> {q.conjTags.map((tag, i) => `${tag.tagName} (${tag.assunto})`).join(', ')}
                    </small>
                  )}
                </div>
              )}

              <div className="questao-actions" style={{ display: 'flex', justifyContent: 'flex-end' }}>
                              <button
                                className="btn-editar"
                                onClick={() => navigate(`/editar-questao/${q.id}`)}
                                style={{
                                  backgroundColor: '#4a90e2',
                                  color: 'white',
                                  border: 'none',
                                  borderRadius: '6px',
                                  padding: '6px 14px',
                                  cursor: 'pointer'
                                }}
                              >
                                Editar questão
                              </button>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
