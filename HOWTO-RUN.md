# Como Executar o Projeto

Este guia vai te ajudar a configurar e executar o projeto da lanchonete usando Docker.

## Pré-requisitos

1. Docker
2. Docker Compose

## Passo 1: Configurar o Ambiente

1. Clone o repositório e navegue até a pasta do projeto:
```bash
git clone <url-do-repositorio>
cd <nome-do-repositorio>
```

2. Crie o arquivo `.env` na raiz do projeto com as seguintes variáveis:
```bash
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres
POSTGRES_DB=lanchonete
PAYARA_ADMIN_PASSWORD=admin
```

## Passo 2: Iniciar a Aplicação

1. Inicie os containers usando Docker Compose:
```bash
docker-compose up -d
```

2. Aguarde alguns segundos para que todos os serviços iniciem completamente.

## Passo 3: Acessar o Sistema

1. Acesse a página inicial:
   - http://localhost:8080/APS-04/view/home/home.html

2. Para acessar como administrador:
   - Clique em "Login Funcionário"
   - Usuário: `admin`
   - Senha: `admin123`
   - URL: http://localhost:8080/APS-04/view/login/login_Funcionario.html

3. Para acessar como cliente:
   - Clique em "Cadastro" para criar uma conta
   - Ou use uma conta existente
   - URL: http://localhost:8080/APS-04/view/login/login.html

## Solução de Problemas

### Reiniciar os Containers

Se precisar reiniciar os containers:
```bash
docker-compose down
docker-compose up -d
```

### Verificar Logs

Para verificar os logs dos containers:
```bash
# Logs do Payara
docker-compose logs payara

# Logs do PostgreSQL
docker-compose logs postgres
```

### Acessar o Banco de Dados

Para acessar o banco de dados diretamente:
```bash
docker-compose exec postgres psql -U postgres -d lanchonete
```

## Portas Utilizadas
- 8080: Aplicação web
- 4848: Painel administrativo do Payara
- 5432: PostgreSQL

## Informações Adicionais

### Painel Administrativo do Payara
- URL: http://localhost:4848
- Usuário padrão: admin
- Senha: admin (definida no .env)

## Dúvidas?

Se tiver dúvidas, consulte a documentação do Docker e Docker Compose, ou peça ajuda a alguém com mais experiência! 