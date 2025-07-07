import React from 'react';
import ReactDOM from 'react-dom/client';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import App from './App.jsx';
import EditarQuestao from './components/EditarQuestao.jsx'
import DashboardProfessor from './components/DashboardProfessor.jsx';
import './index.css';


ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<App />} />
        <Route path="/dashboard" element={<DashboardProfessor />} />
        <Route path="/editar-questao/:id" element={<EditarQuestao />} />
      </Routes>
    </BrowserRouter>
  </React.StrictMode>
);

