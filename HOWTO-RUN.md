# Como Executar o Projeto

Este guia vai te ajudar a configurar e executar o projeto da lanchonete.

## Pré-requisitos

1. Apache Ant
2. Java 17
3. PostgreSQL 12
4. Payara 6

## Passo 1: Configurar o Ambiente

### 1. Clone o Repositório
```bash
git clone <url-do-repositorio>
cd <nome-do-repositorio>
```

### 2. Instale as Dependências

#### Apache Ant

   ```bash
   sudo apt update
   sudo apt install ant -y
   ```

#### Java 17

1. Instale o OpenJDK 17:

   ```bash
   sudo apt install openjdk-17-jdk -y
   ```

2. Verifique a instalação:
   ```bash
   java -version
   ```

#### PostgreSQL 12

1. Instale o PostgreSQL:

   ```bash
   sudo apt install postgresql postgresql-contrib -y
   ```

2. Verifique se o serviço está rodando:

   ```bash
   sudo systemctl status postgresql
   ```

3. Configure o usuário e o banco:

   ```bash
   sudo -i -u postgres
   psql
   ```

   No console do PostgreSQL:

   ```sql
   ALTER USER postgres PASSWORD 'postgres';
   CREATE DATABASE lanchonete;
   \q
   exit
   ```

#### Payara 6

1. Baixe o Payara:

   ```bash
   wget https://www.payara.fish/software/downloads/payara-platform-community-edition/payara-6.2025.4.zip
   ```

2. Extraia o arquivo:

   ```bash
   sudo unzip payara-6.2025.4.zip -d /opt
   ```

3. Verifique se o Payara está funcionando:

   ```bash
   /opt/payara6/bin/asadmin start-domain
   ```

## Passo 2: Configurar o Banco de Dados

1. Certifique-se de que o PostgreSQL está rodando:

   ```bash
   sudo systemctl start postgresql
   ```

2. Teste a conexão com o banco:

   ```bash
   psql -U postgres -d lanchonete
   ```

3. Criar um usuário administrador do sistema:

   ```bash
   psql -U postgres -d lanchonete -c "INSERT INTO tb_funcionarios (nome, sobrenome, usuario, senha, cargo, salario, fg_ativo) VALUES ('Admin', 'Sistema', 'admin', MD5('admin123'), 'Administrador', 5000.00, 1);"
   ```

## Passo 3: Build e Deploy da Aplicação

### 1. Compile o Projeto com Ant

1. Limpe e compile o projeto:

   ```bash
   ant clean && ant
   ```

2. O arquivo WAR será gerado no diretório `dist`.

### 2. Implante o WAR no Payara

1. Inicie o server do Payara

   ```bash
   asadmin start-domain
   ```

2. Inicie o deploy da app

   ```bash
   asadmin deploy dist/APS-04.war
   ```

## Passo 4: Acessar o Sistema

1. Acesse a página inicial:

   - URL: [http://localhost:8080/APS-04/view/home/home.html](http://localhost:8080/APS-04/view/home/home.html)

2. Para acessar como administrador:

   - Clique em "Login Funcionário"
   - Usuário: `admin`
   - Senha: `admin123`
   - URL: [http://localhost:8080/APS-04/view/login/login_Funcionario.html](http://localhost:8080/APS-04/view/login/login_Funcionario.html)

3. Para acessar como cliente:

   - Clique em "Cadastro" para criar uma conta
   - Ou use uma conta existente
   - URL: [http://localhost:8080/APS-04/view/login/login.html](http://localhost:8080/APS-04/view/login/login.html)

## Solução de Problemas

### Reiniciar o Payara
Se o Payara não estiver funcionando corretamente, reinicie-o:

```bash
asadmin restart-domain
```
### Verificar Logs

Para verificar os logs do Payara:

```bash
cat /opt/payara6/glassfish/domains/domain1/logs/server.log
```

Para verificar os logs do PostgreSQL:

```bash
sudo journalctl -u postgresql
```

### Testar Conexão com o Banco

Certifique-se de que o banco está acessível:

```bash
psql -U postgres -d lanchonete
```

## Portas Utilizadas

- **8080**: Aplicação web
- **4848**: Painel administrativo do Payara
- **5432**: PostgreSQL

## Informações Adicionais

### Painel Administrativo do Payara

- URL: [http://localhost:4848](http://localhost:4848)
- Usuário padrão: `admin`
- Senha: `admin` (definida no `.env` ou configuração padrão)

Agora o ambiente está configurado e pronto para rodar o projeto!