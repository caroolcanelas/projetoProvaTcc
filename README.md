# 🎓 Projeto Prova TCC

Sistema web completo desenvolvido para fins acadêmicos, com o objetivo de simular a criação, validação e organização de questões de prova por professores. O projeto utiliza **Java com Spring Boot** no backend e **React com TailwindCSS** no frontend, com foco em uma interface leve, visual artesanal e integração simples para demonstração.

---

## ✨ Funcionalidades

- Cadastro e login de professores
- Associação de questões com tópicos, tags e recursos (arquivos)
- Interface com estilo manual/artístico para fins didáticos e visuais
- Integração completa entre front e back-end em uma única aplicação

---

## 🧱 Tecnologias Utilizadas

### Backend (Java Spring Boot)
- Spring Web
- Spring Data JPA
- Hibernate
- MySQL
- Swagger OpenAPI
- Lombok

### Frontend (React)
- Vite
- Axios
- Tailwind CSS
- Estilo artesanal

---

## 📁 Estrutura de Diretórios

```
projetoProvaTcc/
├── backend/              # Projeto Spring Boot
│   └── src/main/resources/static/ ← onde o front compilado é embutido
├── frontend/             # Projeto React com Tailwind
│   └── src/Login.jsx     ← tela de login
```

---

## 🚀 Rodando o projeto localmente

### 1. Clonar o repositório

```bash
git clone https://github.com/caroolcanelas/projetoProvaTcc.git
cd projetoProvaTcc
```

---

### 2. Rodar o backend (Spring Boot)

```bash
cd backend
./mvnw spring-boot:run
```

Certifique-se de configurar corretamente o `application.properties` com os dados do seu banco MySQL e habilitar CORS, se necessário.

---

### 3. Rodar ou compilar o frontend

#### 👉 Desenvolvimento:

```bash
cd frontend
npm install
npm run dev
```

Acesse em: `http://localhost:5173`

#### 👉 Produção (embutir no Spring Boot):

```bash
cd frontend
npm run build
cp -r dist/* ../backend/src/main/resources/static/
```

Depois, acesse: `http://localhost:8080`

---

## 🛡️ CORS (Cross-Origin Resource Sharing)

Durante o desenvolvimento com front separado, adicione no `ProfessorController.java`:

```java
@CrossOrigin(origins = "http://localhost:5173")
```

Ou configure globalmente via `CorsConfig`.

---

## ✅ Status

- [x] Login funcionando
- [x] React + Tailwind embutido no Spring
- [x] API REST estruturada
- [ ] Dashboard pós-login
- [ ] Validações de campos avançadas (em andamento)

---

## 📚 Créditos

Desenvolvido por [@caroolcanelas](https://github.com/caroolcanelas) [@leticianunes](https://github.com/leticianunes8) e com apoio de orientadores.

---

## 📝 Licença

Este projeto é livre para fins educacionais.
