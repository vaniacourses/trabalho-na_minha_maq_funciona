FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copiar arquivos do projeto
COPY pom.xml .
COPY src/java src/main/java
COPY web src/main/webapp

# Executar build
RUN mvn clean package

FROM tomcat:10.1-jdk17-temurin
COPY --from=build /app/target/*.war $CATALINA_HOME/webapps/ROOT.war
EXPOSE 8080
CMD ["catalina.sh", "run"]
