# Assembleia de Votação

## Descrição

Este projeto implementa uma aplicação fullstack para gerenciamento de assembleias, permitindo o cadastro e consulta de associados, criação de pautas, abertura de sessões de votação, registro de votos e extração dos resultados consolidados. O projeto é dividido em backend (API REST) e frontend (interface web).

A aplicação foi desenvolvida utilizando as seguintes tecnologias: Spring Boot, Java 21, Spring Data JPA, PostgreSQL, Lombok, SLF4J, Swagger, JUnit, Mockito, React e Docker.

## Regras de Negócio

- Pautas, associados e sessões devem existir antes do registro de votos.
- Cada associado pode votar apenas uma vez em cada pauta.
- Uma pauta pode possuir apenas uma sessão de votação aberta por vez.
- A sessão de votação possui um tempo de duração configurável (1 minuto como padrão) e é encerrada automaticamente ao finalizar.
- Votos só podem ser registrados enquanto a sessão estiver aberta.
- O resultado da votação pode ser consultado a qualquer momento e retorna o status da sessão (`OPEN` ou `CLOSED`) e a contagem dos votos.
- Em caso de empate, a pauta é considerada rejeitada.
- O CPF do associado é validado no cadastro.

## Tecnologias Aplicadas

- **Linguagem de Programação:** Java 21
- **Framework Backend:** Spring Boot
- **Persistência:** Spring Data JPA
- **Banco de Dados:** PostgreSQL
- **Injeção de Código:** Lombok
- **Ferramenta de Monitoramento (Log):** SLF4J
- **Documentação da API:** Swagger
- **Ferramentas de Testes:** JUnit e Mockito
- **Container:** Docker e Docker Compose
- **Frontend:** React (Vite)

## Pré-Requisitos

- Java 21 - JDK 21
- Docker instalado com suporte ao Docker Compose
- Node.js e npm (para o frontend)

## Inicialização

### Subindo o Banco de Dados

No diretório `backend`, execute `docker compose up -d` para iniciar o PostgreSQL necessário para a aplicação em segundo plano.

### Iniciando o Backend

Com o banco de dados em execução, no diretório `backend`, inicie a aplicação executando `./mvnw spring-boot:run`, que irá compilar e rodar a API usando Maven e Spring Boot.

A aplicação utiliza o PostgreSQL iniciado pelo Docker Compose (`localhost:5432`) como banco de dados padrão.

### Iniciando o Frontend

No diretório `frontend`, execute `npm install` e depois `npm run dev`. A interface fica disponível em `http://localhost:5173`.

### Testes da API

A API pode ser acessada e testada diretamente pelo Swagger UI em `http://localhost:8080/swagger-ui.html` ou importando o arquivo DesafioVotacao.postman_collection.json no Postman ou Insomnia para execução das requisições..

## Rodando os testes

No diretório `backend`, execute: `./mvnw test`. Os testes de integração usam um banco H2 em memória.

## Escolhas Técnicas

### Arquitetura

A escolha da arquitetura da API foi o padrão de camadas (controller, service, repository), pois esta é uma abordagem padrão de projetos Spring Boot, onde as responsabilidades são separadas e bem definidas.

### Versionamento

Optou-se por utilizar a versão na URL (`/api/v1`), com separação de pacotes por versão (`controller.api.v1`), facilitando a manutenção de múltiplas versões em paralelo no futuro. O prefixo de versão foi centralizado numa classe de constantes (`ApiPaths`), usada tanto pelos controllers quanto pelos testes — assim, uma eventual migração para `/api/v2` exige alteração em um único lugar, em vez de espalhada por toda a base de código.

### Persistência

A aplicação utiliza PostgreSQL como banco de dados, executado via Docker Compose. A escolha foi baseada na boa integração com Spring Boot e simplicidade de configuração utilizando containers.

### Status da Sessão
O status da sessão não é armazenado no banco. A aplicação calcula se a sessão está aberta ou encerrada comparando a data de término com o momento da consulta, evitando a necessidade de processos adicionais para atualizar esse estado.

### Testes

Os testes unitários foram implementados utilizando JUnit e Mockito para validar as regras de negócio de forma isolada.

Os testes de integração utilizam H2 em memória, evitando dependências externas durante a execução da suíte de testes.

### Validação de CPF

A validação de CPF foi implementada através de uma anotação customizada (`@ValidCpf`), utilizando o algoritmo de dígito verificador diretamente na aplicação. Como a validação necessária é simples e pontual, optou-se por uma implementação própria em vez de adicionar uma dependência externa.
### Documentação

A utilização do Swagger foi adotada devido à simplicidade de implementação e à facilidade de visualização e teste dos endpoints da API.

### Monitoramento (Logs)

O SLF4J foi escolhido por fornecer uma abstração de logging que garante registros consistentes e padronizados para depuração e monitoramento da aplicação.

### Integração Externa (Tarefa Bônus)

Foi implementada uma API fake de validação de CPF (`CPFValidatorService`) para simular a comunicação com um serviço externo. A resposta indica aleatoriamente se o associado está apto ou não a votar, mantendo essa integração isolada do fluxo principal de votação.

O endpoint disponibilizado é:

```http
GET /client/cpf/validate/{cpf}
```

### Performance (Tarefa Bônus)

- A apuração do resultado e a validação de voto duplicado são realizadas diretamente no banco de dados utilizando consultas de agregação (`COUNT`) e existência (`EXISTS`), evitando o processamento dos votos em memória.
- Em um cenário com grande volume de dados, colunas frequentemente utilizadas nas consultas, como `associate_id`, `session_id` e `agenda_id`, podem receber índices específicos para melhorar o desempenho.
- Os endpoints de listagem retornam todos os registros. Em um cenário com grande volume de dados, poderia ser implementada uma paginação com `Pageable`.
- Testes de carga não foram implementados nesse projeto.