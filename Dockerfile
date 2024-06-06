FROM openjdk:17-jdk-slim

# Instala o postgresql-client para usar o pg_isready
RUN apt-get update && apt-get install -y postgresql-client && rm -rf /var/lib/apt/lists/*

VOLUME /tmp
COPY target/contas-a-pagar-1.0-SNAPSHOT.jar app.jar
COPY wait-for-postgres.sh /wait-for-postgres.sh
RUN chmod +x /wait-for-postgres.sh

ENTRYPOINT ["/wait-for-postgres.sh", "db", "5432", "--", "java", "-jar", "/app.jar"]
