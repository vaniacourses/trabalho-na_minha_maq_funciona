# APS-04 - Sistema de Lanchonete Online em Java

## 📋 Sobre o Projeto

Sistema web completo para gerenciamento de uma lanchonete online, desenvolvido como parte da disciplina APS (Atividades Práticas Supervisionadas). O sistema permite que administradores controlem pedidos e emitam relatórios, enquanto clientes podem se cadastrar, fazer pedidos personalizados e gerenciar suas compras.

### 🎯 Funcionalidades Principais
- **Cadastro de Usuários**: Sistema completo de registro e autenticação
- **Gestão de Produtos**: Cadastro e controle de lanches, bebidas e ingredientes
- **Pedidos Personalizados**: Clientes podem montar seus próprios lanches
- **Controle de Estoque**: Gestão automática de produtos
- **Relatórios**: Sistema completo de relatórios para administradores
- **Interface Responsiva**: Design moderno e adaptável

## 🚀 Como Executar

### Pré-requisitos
- Docker instalado
- Docker Compose instalado

### Passos para Execução

1. **Clone o repositório:**
```bash
git clone [URL_DO_REPOSITÓRIO]
cd trabalho-na_minha_maq_funciona
```

2. **Execute com Docker Compose:**
```bash
docker-compose up --build
```

### Acesso à Aplicação
- **URL Principal**: `http://localhost:8080`
- **Página Inicial**: `http://localhost:8080/view/home/home.html`
- **Banco de Dados**: PostgreSQL em `localhost:5432`

### Credenciais Padrão
- **Admin**: `admin` / `admin123`
- **Cliente Teste**: `teste@teste.com` / `123456`

## 📁 Estrutura do Projeto e Artefatos

### 📂 Código Fonte
- **`src/main/java/`**: Código fonte Java
  - **`Controllers/`**: Servlets e controladores
  - **`dao/`**: Camada de acesso a dados
  - **`Model/`**: Entidades do sistema
  - **`Helpers/`**: Classes utilitárias
- **`src/main/webapp/`**: Interface web
  - **`view/`**: Páginas HTML
  - **`assets/`**: Imagens e recursos
  - **`styles/`**: Arquivos CSS

### 🧪 Testes
- **`src/test/java/unit/`**: [Testes Unitários e Estruturais](./src/test/java/unit/)
  - Testes de cobertura de código
  - Testes de mutação
  - Testes de regras de negócio
- **`src/test/java/integration/`**: [Testes de Integração](./src/test/java/integration/)
  - Testes de persistência
  - Testes de fluxos completos
- **`src/test/java/selenium/`**: [Testes Funcionais](./src/test/java/selenium/)
  - Testes de interface
  - Testes end-to-end

### 📊 Relatórios de Qualidade

#### 📈 Cobertura de Código (JaCoCo)
- **Relatório HTML**: `target/site/jacoco/index.html`
- **Screenshots**: [📁 Pasta jacoco](./jacoco/)
  - [Cobertura Geral](./jacoco/jacoco.png)
  - [Cobertura Controllers](./jacoco/controllers.png)
  - [Cobertura DAO](./jacoco/dao.png)
  - [Cobertura Helpers](./jacoco/helpers.png)
  - [Cobertura Model](./jacoco/model.png)

#### 🧬 Mutation Testing (PIT)
- **Relatório HTML**: `target/pit-reports/`
- **Screenshots**: [📁 Pasta pit](./pit/)
  - [Mutation Score Controllers](./pit/controllers.png)
  - [Mutation Score DAO](./pit/dao.png)
  - [Mutation Score Helpers](./pit/helpers.png)

#### 🔍 Análise de Qualidade (SonarQube)
- **Screenshots**: [📁 Pasta sonar prints](./sonar%20prints/)
  - [Histórico de Qualidade](./sonar%20prints/history.png)
  - [Qualidade Geral - Antes](./sonar%20prints/overall%20-%20antes.png)
  - [Qualidade Geral - Depois](./sonar%20prints/overall%20-%20depois.png)
  - [Issue 1 - Antes](./sonar%20prints/issue1%20-%20antes.png)
  - [Issue 1 - Depois](./sonar%20prints/issue1%20-%20depois.png)
  - [Issue 2 - Antes](./sonar%20prints/issue2%20-%20antes.png)
  - [Issue 2 - Depois](./sonar%20prints/issue2%20-%20depois.png)
  - [Issue 3 - Antes](./sonar%20prints/issue3%20-%20antes.png)
  - [Issue 3 - Depois](./sonar%20prints/issue3%20-%20depois.png)
  - [Issue 4 - Antes](./sonar%20prints/issue4%20-%20antes.png)
  - [Issue 4 - Depois](./sonar%20prints/issue4%20-%20depois.png)

### 📚 Documentação

#### 📖 Documentos de Entrega
- **`docs/`**: [📁 Pasta de Documentação](./docs/)
  - [📄 Apresentação 1 - Testes CodeBurgers](./docs/Apresentação%201%20-%20Testes%20CodeBurgers.pdf)
  - [📄 Casos de Teste](./docs/Casos%20de%20Teste.pdf)
  - [📄 Entrega 2 - Trabalho QA](./docs/Entrega%202%20-%20trabalho%20QA.pptx)
  - [📄 Plano de Teste - 2025.1](./docs/Plano%20de%20Teste%20-%202025.1%20-%20Trab%20-%20na%20minha%20maq%20funciona.pdf)
  - [📄 Qualidade ISO25010](./docs/QUALIDADE_ISO25010.md)
  - [📄 Como Executar](./docs/HOWTO-RUN.md)

### ⚙️ Configuração e Deploy
- **`docker-compose.yml`**: Configuração dos serviços Docker
- **`Dockerfile`**: Instruções para construção da imagem
- **`banco.sql`**: Script de inicialização do banco de dados
- **`pom.xml`**: Configuração Maven e dependências
- **`sonar-project.properties`**: Configuração SonarQube

## 🧪 Execução de Testes

### Testes Unitários
```bash
mvn test
```

### Testes de Integração
```bash
mvn verify
```

### Testes Funcionais (Selenium)
```bash
# Requer servidor rodando em localhost:8080
mvn test -Dtest=SeleniumTestSuite -Dselenium.enabled=true
```

### Cobertura de Código
```bash
mvn clean test jacoco:report
```

### Mutation Testing
```bash
mvn org.pitest:pitest-maven:mutationCoverage
```

### Análise SonarQube
```bash
mvn sonar:sonar
```

## 🗂️ Rotas da Aplicação

### 👤 Área do Cliente
- **Página Inicial**: `http://localhost:8080/view/home/home.html`
- **Cardápio**: `http://localhost:8080/view/menu/menu.html`
- **Montar Lanche**: `http://localhost:8080/view/montarLanche/montarLanche.html`
- **Carrinho**: `http://localhost:8080/view/carrinho/carrinho.html`
- **Cadastro**: `http://localhost:8080/view/cadastro/cadastro.html`
- **Login**: `http://localhost:8080/view/login/login.html`

### 👨‍💼 Área Administrativa
- **Login Funcionário**: `http://localhost:8080/view/login/login_Funcionario.html`
- **Painel Principal**: `http://localhost:8080/view/painel/painel.html`
- **Gestão de Estoque**: `http://localhost:8080/view/estoque/estoque.html`
- **Relatórios**: `http://localhost:8080/view/relatorio/relatorio.html`

## 🔧 Comandos Úteis

### Docker
```bash
# Parar aplicação
docker-compose down

# Ver logs
docker-compose logs -f

# Reconstruir containers
docker-compose up --build --force-recreate
```

### Desenvolvimento
```bash
# Limpar e compilar
mvn clean compile

# Executar todos os testes
mvn clean verify

# Gerar relatórios completos
mvn clean test jacoco:report org.pitest:pitest-maven:mutationCoverage
```

## 📋 Checklist de Artefatos Entregues

### ✅ Código Fonte
- [x] Controllers (Servlets)
- [x] DAO (Data Access Objects)
- [x] Model (Entidades)
- [x] Helpers (Utilitários)
- [x] Interface Web (HTML/CSS/JS)

### ✅ Testes
- [x] Testes Unitários
- [x] Testes de Integração
- [x] Testes Funcionais (Selenium)
- [x] Testes de Mutação

### ✅ Relatórios de Qualidade
- [x] Cobertura de Código (JaCoCo)
- [x] Mutation Testing (PIT)
- [x] Análise SonarQube

### ✅ Documentação
- [x] Plano de Testes
- [x] Casos de Teste
- [x] Relatórios de Qualidade
- [x] Manual de Execução

### ✅ Configuração
- [x] Docker Compose
- [x] Scripts de Banco
- [x] Configuração Maven
- [x] Configuração SonarQube

---

## 👥 Equipe
Desenvolvido como parte da disciplina APS - Atividades Práticas Supervisionadas

## 📄 Licença
Este projeto é parte de um trabalho acadêmico