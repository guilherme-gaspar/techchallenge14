# 🧩 TechChallenge14 API

API RESTful desenvolvida como parte do projeto **TechChallenge14**, responsável pelo gerenciamento de usuários e autenticação (login, criação, atualização e exclusão de usuários).

---

## 🚀 Tecnologias Utilizadas

- **Java 21**
- **Spring Boot 3**
- **Spring Data JPA**
- **PostgreSQL**
- **Docker / Docker Compose**
- **OpenAPI 3.1 (Swagger)**

---

## 📦 Como Executar o Projeto

### 🔧 Pré-requisitos
- [Java 21+](https://adoptium.net/)
- [Maven 3.9+](https://maven.apache.org/)
- [Docker](https://www.docker.com/)

---

## 🐳 Rodando com Docker Compose

O projeto já possui um `Dockerfile` e um `docker-compose.yml` configurados para facilitar o build e a execução da aplicação junto ao banco de dados PostgreSQL.

### ▶️ Subindo os containers

```bash
docker compose up --build
```

---
## 📘 Endpoints Principais


| Método     | Endpoint                  | Descrição                                   |
| ---------- | ------------------------- | ------------------------------------------- |
| **POST**   | `/v1/login`               | Realiza login e retorna o token          |
| **POST**   | `/v1/users`               | Cria um novo usuário                        |
| **GET**    | `/v1/users`               | Lista usuários ou busca por nome            |
| **PATCH**  | `/v1/users/{id}`          | Atualiza parcialmente os dados de um usuário |
| **PATCH**  | `/v1/users/{id}/password` | Altera a senha de um usuário                |
| **DELETE** | `/v1/users/{id}`          | Remove um usuário pelo ID                   |
