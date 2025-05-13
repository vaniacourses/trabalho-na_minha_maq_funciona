# APS-04-Lanchonete-Online-em-Java

## Sobre
Com o objetivo de desenvolver a capacidade dos alunos e obter nota na disciplina APS (Atividades Práticas Supervisionadas), 
foi proposto um projeto de desenvolvimento de um sistema para uma lanchonete online, onde o administrador consiga controlar 
os pedidos da lanchonete e emitir relatórios. A lanchonete devera permitir o cadastro dos usuários, para que eles possam realizar seus pedidos, 
e o cadastro de produtos, que ficariam por parte do administrador. Após o cadastro,  cliente poderá utilizar os ingredientes cadastrados para 
criar seu lanche personalizado. O sistema deverá fazer o controle dos pedidos de forma que agrade os clientes, e controlar tambem o estoque de produtos.

## Tecnologias Utilizadas

O Sistema funciona com base em um Frontend Utilizando HTML 5, CSS3 e JavaScript, e um Backend baseado em Java Web utilizando-se do Servidor Glassfish 4 
e muito baseado no uso de Servlets para a Comunicação atraves de requisições. Além disso o Sistema utiliza das Bibliotecas gson-2.8.6 e json-20200518 
Para a manipulação de Arquivos JSON dentro do Código Java, e de um Banco de Dados PostgreSQL, do qual o Código base também se encontra no repositório.

## Alguns Screenshots

![alt text](https://i.ibb.co/BPn99jW/248f5162-df3a-4754-8ade-82b9784f94d8.jpg)
![alt text](https://i.ibb.co/GM3r7Dd/daf6e1f9-676e-4a27-9669-80036dc52cce.jpg)
![alt text](https://i.ibb.co/kXdFFq5/e378bda9-bcc8-4483-bb2f-f2143a79817e.jpg)
![alt text](https://i.ibb.co/z7kqx4x/a5a0e3f3-3605-4d3f-b2ba-f54c2ef76f18.jpg)
![alt text](https://i.ibb.co/C6kMZLW/c1bad7f9-c79a-4516-9d08-bc2548ee9880.jpg)
![alt text](https://i.ibb.co/2321674/8a74fb26-1db0-49df-b2d7-2479d0567a4e.jpg)
![alt text](https://i.ibb.co/2YSbvGZ/8d3386e3-d13b-4a42-b389-151fbadb1d77.jpg)

# Sistema de Lanchonete

Este é um sistema web para gerenciamento de uma lanchonete, permitindo clientes fazerem pedidos e funcionários gerenciarem produtos e pedidos.

## Pré-requisitos
- Docker instalado
- Docker Compose instalado

## Como Executar

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

## Rotas Disponíveis

### Usuário Comum (Cliente)
- `http://localhost:8080/view/home/home.html` - Página inicial
- `http://localhost:8080/view/menu/menu.html` - Cardápio com lanches e bebidas
- `http://localhost:8080/view/montarLanche/montarLanche.html` - Monte seu próprio lanche
- `http://localhost:8080/view/carrinho/carrinho.html` - Carrinho de compras
- `http://localhost:8080/view/cadastro/cadastro.html` - Cadastro de novo cliente
- `http://localhost:8080/view/login/login.html` - Login de cliente

### Administrador
- `http://localhost:8080/view/login/login_Funcionario.html` - Login de funcionário/admin
- `http://localhost:8080/view/painel/painel.html` - Painel de controle principal
  - Abrir/Fechar lanchonete
  - Cadastrar lanches
  - Cadastrar bebidas
  - Cadastrar ingredientes
  - Cadastrar funcionários
- `http://localhost:8080/view/estoque/estoque.html` - Gerenciamento de estoque
  - Controle de lanches
  - Controle de bebidas
  - Controle de ingredientes
- `http://localhost:8080/view/relatorio/relatorio.html` - Relatórios
  - Relatório de bebidas por pedidos
  - Relatório de lanches detalhado
  - Relatório de gastos