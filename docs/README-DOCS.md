# 🧩 Documentação da API — TechChallenge14

Este documento descreve os principais endpoints da API e como realizar testes rápidos diretamente pelo terminal utilizando **cURL**.

A collection Postman completa está disponível em:  
📂 [`docs/collection.json`](./collection.json)

---

## 🚀 Estrutura da Collection

A collection contém todas as rotas da aplicação, organizadas nas seguintes seções:

| Grupo | Descrição |
|--------|------------|
| 🧾 **Cadastro de usuário** | Criação de novos usuários (válidos e inválidos) |
| ✏️ **Atualização de usuário** | Edição de dados de perfil e senha |
| 🔍 **Recupera usuários** | Busca por nome ou listagem geral |
| 🗑️ **Deleta usuário** | Remoção de registros |
| 🔐 **Atualização de senha** | Alteração de senha com token JWT |

---

## 🧾 Cadastro de Usuário — `POST /v1/users`

### ✅ Sucesso

```bash
curl -X POST http://localhost:8080/v1/users   -H "Content-Type: application/json"   -d '{
    "name": "Usuário Teste",
    "email": "usuario.teste@teste.com",
    "password": "senha123",
    "address": "Rua Teste 100",
    "login": "usuarioteste1",
    "roleId": 1
  }'
```

**Resposta (201):**
```json
{
  "id": 1,
  "name": "Usuário Teste",
  "email": "usuario.teste@teste.com",
  "login": "usuarioteste1",
  "role": "CLIENT"
}
```

### ❌ Erro — e-mail inválido

```bash
curl -X POST http://localhost:8080/v1/users   -H "Content-Type: application/json"   -d '{
    "name": "Teste Email",
    "email": "emailinvalido",
    "password": "teste123",
    "address": "Rua 1",
    "login": "testeemail",
    "roleId": 1
  }'
```

**Resposta (400):**
```json
{ "message": "E-mail inválido. Verifique o formato e tente novamente." }
```

---

## 🔐 Login — `POST /v1/login`

### ✅ Sucesso

```bash
curl -X POST http://localhost:8080/v1/login   -H "Content-Type: application/json"   -d '{
    "login": "usuarioteste1",
    "password": "senha123"
  }'
```

**Resposta (200):**
```json
{ "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6..." }
```

### ❌ Erro — senha incorreta

```bash
curl -X POST http://localhost:8080/v1/login   -H "Content-Type: application/json"   -d '{
    "login": "usuarioteste1",
    "password": "senha_errada"
  }'
```

**Resposta (401):**
```json
{ "message": "Credenciais inválidas. Verifique login e senha." }
```

---

## ✏️ Atualização de Usuário — `PATCH /v1/users/{id}`

### ✅ Sucesso

```bash
curl -X PATCH http://localhost:8080/v1/users/1   -H "Authorization: Bearer <token>"   -H "Content-Type: application/json"   -d '{
    "name": "Usuário Atualizado",
    "email": "usuario.novo@teste.com",
    "address": "Rua Nova 200",
    "login": "usuarioteste1",
    "roleId": 2
  }'
```

**Resposta (200):**
```json
{
  "id": 1,
  "name": "Usuário Atualizado",
  "email": "usuario.novo@teste.com",
  "role": "RESTAURANT_OWNER"
}
```

### ❌ Erro — token inválido

```bash
curl -X PATCH http://localhost:8080/v1/users/1   -H "Authorization: Bearer token_invalido"   -H "Content-Type: application/json"   -d '{ "name": "Novo Nome" }'
```

**Resposta (401):**
```json
{ "message": "Acesso negado: token ausente ou inválido." }
```

---

## 🔍 Recuperar Usuário — `GET /v1/users?name={name}`

### ✅ Sucesso

```bash
curl -X GET "http://localhost:8080/v1/users?name=Usuário Teste"   -H "Authorization: Bearer <token>"
```

**Resposta (200):**
```json
[
  {
    "id": 1,
    "name": "Usuário Teste",
    "email": "usuario.teste@teste.com"
  }
]
```

### ❌ Erro — token ausente

```bash
curl -X GET "http://localhost:8080/v1/users?name=Usuário Teste"
```

**Resposta (401):**
```json
{ "message": "Token ausente. Faça login e tente novamente." }
```

---

## 🗑️ Deletar Usuário — `DELETE /v1/users/{id}`

### ✅ Sucesso

```bash
curl -X DELETE http://localhost:8080/v1/users/1   -H "Authorization: Bearer <token>"
```

**Resposta (204):**
```
(no content)
```

### ❌ Erro — usuário inexistente

```bash
curl -X DELETE http://localhost:8080/v1/users/999   -H "Authorization: Bearer <token>"
```

**Resposta (404):**
```json
{ "message": "Usuário não encontrado." }
```

---

## 🔑 Atualizar Senha — `PATCH /v1/users/{id}/password`

### ✅ Sucesso

```bash
curl -X PATCH http://localhost:8080/v1/users/1/password   -H "Authorization: Bearer <token>"   -H "Content-Type: application/json"   -d '{ "newPassword": "novaSenha123" }'
```

**Resposta (200):**
```json
{ "message": "Senha atualizada com sucesso." }
```

### ❌ Erro — senha vazia

```bash
curl -X PATCH http://localhost:8080/v1/users/1/password   -H "Authorization: Bearer <token>"   -H "Content-Type: application/json"   -d '{ "newPassword": "" }'
```

**Resposta (400):**
```json
{ "message": "A nova senha não pode ser vazia." }
```

---

## 📚 Referências

- 🌐 [Swagger UI - Disponível se rodar o projeto](http://localhost:8080/swagger-ui/index.html)
- 🏠 [Voltar ao README principal](../README.md)
