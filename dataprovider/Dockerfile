# ========= Builder =========
FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /workspace

COPY pom.xml .
COPY common/pom.xml common/pom.xml
COPY dataprovider/pom.xml dataprovider/pom.xml
COPY datagatherer/pom.xml datagatherer/pom.xml
RUN mvn -B -q -DskipTests dependency:go-offline

COPY common/ common/
COPY dataprovider/ dataprovider/
COPY datagatherer/ datagatherer/

RUN mvn -B -DskipTests -pl dataprovider -am clean package
RUN set -eux; \
    JAR="$(ls dataprovider/target/*.jar | grep -Ev '(original|plain|sources|javadoc)')" ; \
    cp "$JAR" /workspace/dataprovider/target/app.jar

# ========= Runtime =========
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /workspace/dataprovider/target/app.jar /app/app.jar
ENV SPRING_PROFILES_ACTIVE=prod
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
