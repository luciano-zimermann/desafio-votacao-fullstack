## Executando o projeto

O frontend deve ser executado junto com o backend.

1. Garanta que o backend esteja rodando na porta `8080`:

```bash
docker-compose up -d
./mvnw spring-boot:run
```

2. Instale as dependências e execute o frontend:

```bash
npm install
npm run dev
```

3. Acesse:

```
http://localhost:5173
```

## Observação

O backend já possui a configuração de CORS necessária para permitir as chamadas do frontend em ambiente local.