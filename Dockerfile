FROM bellsoft/liberica-openjdk-alpine-musl:17.0.14 AS build
WORKDIR /app
RUN apk update && \
    apk add --no-cache maven
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests
FROM bellsoft/liberica-openjdk-alpine-musl:17.0.14
WORKDIR /app
COPY --from=build /app/target/wallet-ops-1.0-SNAPSHOT.jar /app/wallet-ops.jar
EXPOSE 9090
ENTRYPOINT ["java", "-jar", "/app/wallet-ops.jar"]
