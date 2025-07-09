import { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

export default function Login() {
  const [email, setEmail] = useState("");
  const [senha, setSenha] = useState("");
  const [mensagem, setMensagem] = useState("");
  const navigate = useNavigate();

  const fazerLogin = async (e) => {
    e.preventDefault();

    try {
      // 1. Faz o login
      const resposta = await axios.post("http://localhost:8080/api/professor/login", {
        email,
        senha,
      });

      if (resposta.status === 200) {
        // 2. Busca todos os professores
        const res = await axios.get("http://localhost:8080/api/professor");
        const professores = res.data;

        // 3. Filtra pelo e-mail informado no login
        const professorLogado = professores.find((p) => p.email === email);

        if (professorLogado) {
          localStorage.setItem("professor", JSON.stringify(professorLogado));
          navigate("/dashboard");
        } else {
          setMensagem("Professor não encontrado no banco.");
        }
      }
    } catch (erro) {
      setMensagem("Erro: " + (erro.response?.data || erro.message));
    }
  };


  return (
    <div className="min-h-screen bg-[#fdfaf4] flex items-center justify-center font-sans">
      <div className="flex flex-col gap-8 items-center bg-[#fef4e8] p-8 rounded-lg shadow-md border border-[#dfc8b5] relative">
        <div className="absolute -top-4 left-1/2 -translate-x-1/2 bg-[#d7bfa3] px-4 py-1 text-white rounded shadow">
          Login do Sistema
        </div>

        <form onSubmit={fazerLogin} className="flex flex-col gap-4 w-64">
          <label className="text-[#5a3e2b] font-medium">Email</label>
          <input
            className="border-b-2 bg-transparent outline-none"
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />

          <label className="text-[#5a3e2b] font-medium">Senha</label>
          <input
            className="border-b-2 bg-transparent outline-none"
            type="password"
            value={senha}
            onChange={(e) => setSenha(e.target.value)}
            required
          />

          <button className="bg-[#a87c6f] hover:bg-[#946a5f] text-white py-2 rounded mt-2">
            Entrar
          </button>
        </form>

        {mensagem && <p className="text-sm text-[#8a4b4b]">{mensagem}</p>}
      </div>
    </div>
  );
}
