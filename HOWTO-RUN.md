# Como Executar o Projeto

Este guia vai te ajudar a configurar e executar o projeto da lanchonete do zero.

## Pré-requisitos

1. Java 17 ou superior
2. PostgreSQL 12 ou superior
3. Payara Server 6

## Passo 1: Configurar o Banco de Dados

1. Abra o terminal e conecte ao PostgreSQL:
```bash
psql -U postgres
```

2. Crie o banco de dados:
```sql
CREATE DATABASE lanchonete ENCODING 'UTF-8' TEMPLATE template0;
```

3. Saia do PostgreSQL:
```sql
\q
```

4. Execute o script de criação das tabelas:
```bash
psql -U postgres -d lanchonete -f banco.sql
```

5. Crie o usuário administrador:
```bash
psql -U postgres -d lanchonete -c "INSERT INTO tb_funcionarios (nome, sobrenome, usuario, senha, cargo, salario, fg_ativo) VALUES ('Admin', 'Sistema', 'admin', MD5('admin123'), 'Administrador', 5000.00, 1);"
```

## Passo 2: Configurar o Payara Server

1. Inicie o Payara Server:
```bash
asadmin start-domain
```

2. Verifique se o servidor está rodando:
```bash
asadmin list-domains
```

## Passo 3: Compilar o Projeto

1. Compile o projeto usando o Apache Ant:
```bash
ant clean
ant
```

O arquivo WAR será gerado em `dist/APS-04.war`

## Passo 4: Deploy da Aplicação

1. Faça o deploy no Payara:
```bash
asadmin deploy dist/APS-04.war
```

## Passo 5: Acessar o Sistema

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

### Banco de Dados

Se precisar recriar o banco do zero:
```bash
# Conecte ao PostgreSQL
psql -U postgres

# Remova o banco se existir
DROP DATABASE IF EXISTS lanchonete;

# Saia do PostgreSQL
\q

# Volte ao Passo 1 para recriar o banco
```

### Servidor

Se o Payara não iniciar:
1. Verifique se a porta 8080 está livre:
```bash
lsof -i :8080
```

2. Se houver algum processo usando a porta, encerre-o:
```bash
kill -9 <PID>
```

3. Tente iniciar novamente:
```bash
asadmin start-domain
```

### Deploy

Se o deploy falhar:
1. Remova o deploy anterior:
```bash
asadmin undeploy APS-04
```

2. Limpe o cache do servidor:
```bash
asadmin stop-domain
rm -rf ~/.payara/cache/*
asadmin start-domain
```

3. Tente o deploy novamente:
```bash
asadmin deploy dist/APS-04.war
```

### Login

Se o login falhar:
1. Verifique se o usuário admin existe:
```bash
psql -U postgres -d lanchonete -c "SELECT usuario, senha FROM tb_funcionarios WHERE usuario = 'admin';"
```

2. Se necessário, recrie o usuário admin:
```bash
psql -U postgres -d lanchonete -c "DELETE FROM tb_funcionarios WHERE usuario = 'admin';"
psql -U postgres -d lanchonete -c "INSERT INTO tb_funcionarios (nome, sobrenome, usuario, senha, cargo, salario, fg_ativo) VALUES ('Admin', 'Sistema', 'admin', MD5('admin123'), 'Administrador', 5000.00, 1);"
```

## Estrutura do Projeto

```
APS-04/
├── src/
│   ├── java/
│   │   ├── Controllers/    # Controladores da aplicação
│   │   ├── DAO/           # Acesso ao banco de dados
│   │   ├── Models/        # Modelos de dados
│   │   └── Helpers/       # Classes auxiliares
│   └── web/
│       ├── view/          # Páginas HTML
│       ├── css/           # Estilos CSS
│       └── js/            # Scripts JavaScript
├── lib/                   # Bibliotecas externas
└── dist/                  # Arquivos compilados
```

## Portas Utilizadas
- 8080: Aplicação web
- 4848: Painel administrativo do Payara
- 5432: PostgreSQL

## Informações Adicionais

### Painel Administrativo do Payara
- URL: http://localhost:4848
- Usuário padrão: admin
- Senha padrão: (vazia)

### Recompilação do Projeto (se necessário)
Se precisar recompilar o projeto, use os comandos:
```sh
ant clean
ant
```

## Dúvidas?

Se tiver dúvidas, consulte a documentação do Java, Payara, PostgreSQL e Apache Ant, ou peça ajuda a alguém com mais experiência! 