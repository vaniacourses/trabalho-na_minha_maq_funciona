# Como Rodar o Sistema da Lanchonete Online

Este guia vai te ajudar a rodar o sistema da Lanchonete Online no seu computador.

## 1. Pré-requisitos

Antes de começar, certifique-se de ter instalado:
- Java JDK 11 ou superior
- PostgreSQL 14 ou superior
- Glassfish/Payara Server 6 ou superior
- Apache Ant (opcional, apenas se precisar recompilar o projeto)

## 2. Configure o Banco de Dados

### Credenciais do PostgreSQL
As credenciais do banco de dados estão configuradas no arquivo `src/java/br/com/aps/dao/Conexao.java`. 
Por padrão, o sistema usa:
- Usuário: postgres
- Senha: postgres
- Banco: lanchonete
- Host: localhost
- Porta: 5432

Se suas credenciais forem diferentes, você precisará alterar este arquivo antes de prosseguir.

### Criar o Banco de Dados `lanchonete`

1. Abra o terminal (ou prompt de comando).
2. Execute o comando:
   ```sh
   createdb -U postgres lanchonete
   ```
   (Substitua `postgres` pelo seu usuário do PostgreSQL.)

### Executar o Script do Banco

1. Abra o terminal (ou prompt de comando) e execute o script do banco:
   ```sh
   psql -U postgres -d lanchonete -f banco.sql
   ```
   (Substitua `postgres` pelo seu usuário do PostgreSQL.)

## 3. Inicie o Glassfish Server

1. Abra o terminal e execute:
   ```sh
   asadmin start-domain
   ```

## 4. Faça o Deploy da Aplicação

1. O arquivo WAR já está na pasta `dist/`
2. Execute o comando:
   ```sh
   asadmin deploy dist/APS-04.war
   ```

## 5. Acesse o Sistema

Abra o navegador e acesse:  
http://localhost:8080/APS-04

## 6. Informações Adicionais

### Painel Administrativo do Glassfish
- URL: http://localhost:4848
- Usuário padrão: admin
- Senha padrão: (vazia)

### Portas Utilizadas
- 8080: Aplicação web
- 4848: Painel administrativo do Glassfish
- 5432: PostgreSQL

### Recompilação do Projeto (se necessário)
Se precisar recompilar o projeto, use os comandos:
```sh
ant clean
ant
```

## 7. Solução de Problemas

### Banco de Dados
- Se o banco já existir, você pode ignorar o erro ao criar
- Se as tabelas já existirem, você pode ignorar os erros ao executar o script SQL

### Glassfish
- Se o servidor não iniciar, verifique se a porta 4848 está disponível
- Se o deploy falhar, verifique se o arquivo WAR existe na pasta dist/

### Aplicação
- Se a aplicação não abrir, verifique se o Glassfish está rodando
- Verifique se todas as portas necessárias estão disponíveis

## 8. Dúvidas?

Se tiver dúvidas, consulte a documentação do Java, Glassfish, PostgreSQL e Apache Ant, ou peça ajuda a alguém com mais experiência! 