# Como Executar o Projeto

## Pré-requisitos
- Docker instalado
- Docker Compose instalado

## Passos para Execução

1. Clone o repositório:
```bash
git clone [URL_DO_REPOSITÓRIO]
cd [NOME_DO_DIRETÓRIO]
```

2. Execute o projeto usando Docker Compose:
```bash
docker-compose up --build
```

Este comando irá:
- Construir a imagem da aplicação
- Iniciar o container do PostgreSQL
- Executar o script de inicialização do banco de dados
- Iniciar a aplicação

## Acessando a Aplicação
- A aplicação estará disponível em: `http://localhost:8080`
- Página inicial: `http://localhost:8080/view/home/home.html`
- O banco de dados PostgreSQL estará disponível em:
  - Host: localhost
  - Porta: 5432
  - Banco: lanchonete
  - Usuário: postgres
  - Senha: postgres

## Credenciais Padrão
- Usuário admin: admin
- Senha admin: admin123

## Comandos Úteis

Para parar a aplicação:
```bash
docker-compose down
```

Para ver os logs:
```bash
docker-compose logs -f
```

Para reconstruir e reiniciar os containers:
```bash
docker-compose up --build --force-recreate
```

## Estrutura do Projeto
- `docker-compose.yml`: Configuração dos serviços Docker
- `Dockerfile`: Instruções para construir a imagem da aplicação
- `banco.sql`: Script de inicialização do banco de dados
- `src/`: Código fonte da aplicação
- `web/`: Arquivos da interface web