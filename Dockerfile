# ---------- Build stage ----------
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /workspace

# First copy pom.xml and resolve deps for better layer caching
COPY pom.xml .
RUN mvn -B -q dependency:go-offline

# Then copy the source and build
COPY src ./src
RUN mvn -B -q clean package -DskipTests

# ---------- Runtime stage ----------
# Distroless Java 21, minimal and secure
FROM gcr.io/distroless/java21-debian12

# Cloud Run expects the app to listen on 8080
ENV SERVER_PORT=8080
EXPOSE 8080

# Non-root user (distroless provides nonroot)
USER nonroot:nonroot

WORKDIR /app

# Copy the fat jar from the builder stage
# If your jar name differs, adjust the pattern
COPY --from=builder /workspace/target/*.jar /app/app.jar

# Run the app
ENTRYPOINT ["java","-jar","/app/app.jar"]
