# 🎓 Projeto Prova TCC

Sistema web completo desenvolvido para fins acadêmicos, com o objetivo de simular a criação, validação e organização de questões de prova por professores. O projeto utiliza Java com Spring Boot no backend e React com TailwindCSS no frontend, com foco em uma interface leve, visual artesanal e integração simples para demonstração.

---

## 🧠 Funcionalidades

- Cadastro de Professores
- Login de Professores
- Criação de Questões
- Criação de Tópicos e Subtópicos
- Associação de Tags
- Gerenciamento de Opções de Resposta
- Upload de Recursos (imagens, áudios, etc.) vinculados às opções
- Validação de Questões
- API documentada com Swagger
- Importar em batch para o banco

---

## 🏗️ Estrutura do Projeto

```
projetoProvaTcc/
├── backend/               # Back-end Spring Boot
│   └── src/
│       └── main/
│           ├── java/      # Código Java
│           └── resources/ # Configurações e estáticos
├── frontend/              # Front-end React + Vite + Tailwind
│   └── src/
```

---

## 🚀 Como rodar o projeto

### 🖥️ Backend (Spring Boot)

1. Acesse a pasta `backend/`
2. Configure o arquivo `application.properties` com seu banco de dados.
3. Execute:
```bash
./mvnw spring-boot:run
```

A API estará disponível em `http://localhost:8080`.

### 🌐 Frontend (React)

1. Acesse a pasta `frontend/`
2. Instale as dependências:
```bash
npm install
```
3. Rode o projeto:
```bash
npm run dev
```

---

## 🔗 Build de Front para Backend

Após rodar `npm run build` no frontend, copie o conteúdo da pasta `dist/` para:

```
/backend/src/main/resources/static/
```

---

## 🛡️ CORS (Cross-Origin Resource Sharing)

Durante o desenvolvimento com front separado, adicione na classe necessária:

```java
@CrossOrigin(origins = "http://localhost:5173")
```

Ou configure globalmente via uma classe `CorsConfig`.

---

## 📑 Documentação da API

A documentação interativa da API está disponível após rodar o backend em:

```
http://localhost:8080/swagger-ui/index.html
```

---

## ✅ Status

- ✅ Login funcionando
- ✅ React + Tailwind embutido no Spring
- ✅ API REST estruturada
- ✅ Dashboard pós-login
- ✅ Import em batch para arquivos csv
- ⚙️ Validações de campos avançadas (em andamento)

---

## 🗂️ Exemplos de JSON (Input)

### 🧑‍🏫 Professor

```json
{
  "nome": "Carolina Canelas",
  "email": "carol@exemplo.com",
  "senha": "123456",
  "matricula": 1001
}
```

### 📄 Questão

```json
{
  "instrucaoInicial": "Leia o texto abaixo...",
  "suporte": "Imagem ou texto de apoio",
  "comando": "Responda corretamente",
  "nivel": "FACIL",
  "tipo": "RESPOSTA_UNICA",
  "validada": false,
  "conjTags": [1, 2],
  "conjOpcoes": [10, 11],
  "matriculaProfessorValidador": 1001
}
```

### 🏷️ Tag

```json
{
  "tagName": "Matemática",
  "assunto": "Ciências matemáticas"
}
```

### 📚 Tópico

```json
{
  "numOrdem": 4,
  "nome": "Algebra",
  "conteudo": "Estudos de algebra",
}
```

### 🔘 Opção

```json
{
  "conteudo": "A resposta é 2",
  "correta": true,
  "conjRecursos": []
}
```

### 📚 Disciplina

```json
{
  "codigo": "123",
  "nome": "Álgebra 1",
  "numCreditos": 4,
  "objetivoGeral": "Introduzir tópicos e conhecimentos base de álgebra",
  "conjTopicos": []
}
```

### 📎 Recurso (Via FormData)

- Campo `arquivo`: Upload de arquivo (imagem, áudio, etc.)
- Campo `id`: (opcional) ID do recurso caso seja atualização

---

## 🏗️ Tecnologias Utilizadas

- Backend: Java + Spring Boot + Spring Data JPA + Swagger + Lombok
- Frontend: React + Vite + Tailwind CSS + Axios + TinyMCE
- Banco de Dados: MySQL (ou outro relacional)
- Build: Maven

---

## 📚 Créditos

Desenvolvido por [@caroolcanelas](https://github.com/caroolcanelas), [@leticianunes](https://github.com/) e com apoio de orientadores.

---

## 📝 Licença

Este projeto é livre para fins educacionais.

---
