# Usar a imagem oficial do Maven com Java 17
FROM maven:3.9-eclipse-temurin-17 AS build

# Definir o diretório de trabalho
WORKDIR /app

# Copiar o arquivo pom.xml
COPY pom.xml .

# Baixar as dependências
RUN mvn dependency:go-offline

# Copiar o código fonte
COPY src ./src

# Compilar a aplicação
RUN mvn package -DskipTests

# Imagem final
FROM tomcat:10.1-jdk17-temurin

# Remover aplicações padrão do Tomcat
RUN rm -rf /usr/local/tomcat/webapps/*

# Copiar o arquivo WAR compilado para o Tomcat
COPY --from=build /app/target/*.war /usr/local/tomcat/webapps/ROOT.war

# Expor a porta 8080
EXPOSE 8080

# Comando para iniciar o Tomcat
CMD ["catalina.sh", "run"]