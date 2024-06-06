# Contas a Pagar API

Uma API RESTful para gerenciar contas a pagar, permitindo operações CRUD, autenticação, e importação/exportação de dados via CSV.

## Índice

- [Visão Geral](#visão-geral)
- [Requisitos](#requisitos)
- [Instalação](#instalação)
- [Configuração](#configuração)
- [Endpoints](#endpoints)
    - [Autenticação](#autenticação)
    - [Contas a Pagar](#contas-a-pagar)
    - [Importação e Exportação CSV](#importação-e-exportação-csv)
- [Documentação da API](#documentação-da-api)


## Visão Geral

A API de Contas a Pagar permite gerenciar contas a pagar, incluindo operações de criação, leitura, atualização e exclusão (CRUD), autenticação de usuários com JWT, e funcionalidades de importação e exportação de dados em formato CSV.

## Requisitos

- Java 17 ou superior
- Spring Boot 2.6.2
- PostgreSQL
- Docker e Docker Compose

## Instalação

1. Clone o repositório:
   ```bash
   git clone https://github.com/BrothersInfo/contas-a-pagar-api.git
   cd contas-a-pagar-api

2. Construa o Projeto
   ```bash
   ./mvn clean installl
3. Execute os containers Docker:
   ```bash
   docker-compose up --build

## Configuração
- A configuração da aplicação é gerenciada através do arquivo application.properties.
   ```bash
   spring.datasource.url=jdbc:postgresql://postgres:5432/contasdb
   spring.datasource.username=postgres
   spring.datasource.password=postgres
   spring.jpa.hibernate.ddl-auto=validate
   spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
   spring.flyway.enabled=true
   spring.flyway.locations=classpath:db/migration

   security.jwt.token.secret-key=your-secret-key
   security.jwt.token.expire-length=3600000  # 1 hora em milissegundos

## Endpoints
### Autenticação
-   Login 
    - POST /api/auth/login
    - Descrição: Autentica um usuário e retorna um token JWT.
    - Requisição:
       ```bash
       {
        "username": "admin",
        "password": "admin"
       }
    - Resposta:
      ```bash
       {
        "username": "admin",
        "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
       }

### Contas a Pagar
-   Listar Contas
    - GET /api/bills
    - Descrição: Retorna uma lista de contas a pagar.
    

-   Obter Conta por ID
    - GET /api/bills/{id}
    - Descrição: Retorna uma conta específica pelo ID.


-   Criar Conta
    - POST /api/bills
    - Descrição: Cria uma nova conta a pagar.
    ```bash
    {
      "dataVencimento": "2024-06-06",
      "dataPagamento": "2024-06-06",
      "valor": 100.00,
      "descricao": "Pagamento de serviços",
      "situacao": "Paga"
    }

-   Atualizar Conta
    - PUT /api/bills/{id}
    - Descrição: Atualiza os detalhes de uma conta existente.


-   Excluir Conta
    - DELETE /api/bills/{id}
    - Descrição: Exclui uma conta específica pelo ID.


-   Filtrar Contas
    - GET /api/bills/find-by-filters
    - Descrição: Retorna uma página de contas filtradas por data de vencimento e descrição.
    - Parâmetros:
      - dataVencimento
      - descricao
      - page
      - size


-   Obter Total Pago Entre Datas
    - GET /api/bills/total-payed
    - Descrição: Retorna o valor total pago entre duas datas.
    - Parâmetros:
      - dataInicio
      - dataFim


-   Atualizar Situação da Conta
    - PUT /api/bills/{id}/status
    - Descrição: Atualiza a situação de uma conta pelo ID.
    - Parâmetros:
      - situacao


### Importação e Exportação CSV
-   Importar Contas via CSV
    - POST /api/bills/import
    - Descrição: Importa um lote de contas a pagar a partir de um arquivo CSV.
    

-   Exportar Contas para CSV
    - GET /api/bills/export
    - Descrição: Exporta todas as contas a pagar em formato CSV.

## Documentação da API

A documentação completa da API pode ser acessada via Swagger UI após iniciar a aplicação:

[Swagger UI](http://localhost:8080/swagger-ui/index.html)

OBS: Para o endpoint de importação de arquivo tem um modelo na raiz do projeto(contas-a-pagar.csv).
