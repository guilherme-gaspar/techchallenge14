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
- [Docker](https://www.docker.com/)

### 🐳 Rodando com Docker Compose

O projeto já possui um `Dockerfile` e um `docker-compose.yml` configurados para facilitar o build e a execução da aplicação junto ao banco de dados PostgreSQL.

### ▶️ Subindo os containers

```bash
docker compose up -d
```

---
## 📘 Endpoints Principais

| Método     | Endpoint                  | Descrição                                    | Autenticação |
|------------|---------------------------|----------------------------------------------|--------------|
| **POST**   | `/v1/login`               | Realiza login e retorna o token JWT          | Não          |
| **POST**   | `/v1/users`               | Cria um novo usuário                         | Não          |
| **GET**    | `/v1/users`               | Lista usuários ou busca por nome             | Sim          |
| **PATCH**  | `/v1/users/{id}`          | Atualiza parcialmente os dados de um usuário | Sim          |
| **PATCH**  | `/v1/users/{id}/password` | Altera a senha de um usuário                 | Sim          |
| **DELETE** | `/v1/users/{id}`          | Remove um usuário pelo ID                    | Sim          |

> 💡 **Nota:**  
> Para acessar os endpoints que requerem autenticação (`Sim` na coluna “Autenticação”), siga os passos abaixo:
>
> 1. **Cadastro do usuário:** antes de realizar login, é necessário criar uma conta usando o endpoint `/v1/users`.  
     > Exemplo de body para registro:
     >    ```json
>    {
>      "name": "Guilherme Gaspar",
>      "email": "guilherme123@gmail.com",
>      "password": "testef123",
>      "address": "Rua Exemplo, 123",
>      "login": "logs123f123in12s",
>      "roleId": 1
>    }
>    ```
     >
- `roleId = 1` → Cliente
>    - `roleId = 2` → Dono de restaurante
>
>
> 2. **Login:** utilize o endpoint `/v1/login` com o login e senha cadastrados.  
     > Esse endpoint possui um **post-script** configurado no Postman que salva automaticamente o token retornado na
     variável de ambiente `authToken`.
>
>
> 3. **Autenticação automática:** os endpoints que exigem autenticação já utilizam o header:
     >    ```http
>    Authorization: {{authToken}}
>    ```
     >    Assim, ao realizar o login com sucesso, o token é salvo e aplicado automaticamente nos próximos requests — *
     *não é necessário colar manualmente o token**.
>
>
> 4. **Ambiente do Postman:** certifique-se de **selecionar um ambiente ativo** antes de enviar as requisições.  
     > Se a variável `authToken` ainda não existir, o script do endpoint de login **a criará automaticamente na primeira
     execução**.
>
>
> 5. **Caso o token não seja aplicado automaticamente:**  
     > copie o token retornado pelo login e adicione manualmente no header das requisições:
     >    ```http
>    Authorization: <seu_token>
>    ```
>
> ⚙️ **Importante:** esse fluxo automatizado de autenticação **só funciona se você utilizar a collection Postman**
> disponível no repositório.  
> A collection pode ser importada diretamente a partir do arquivo [`collection.json`](./collection.json) localizado na
> raiz do projeto.
>
> Dessa forma, o fluxo de autenticação no Postman é totalmente automatizado:  
> **cadastre-se → faça login → os endpoints autenticados funcionarão automaticamente.**
