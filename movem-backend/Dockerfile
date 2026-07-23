# ==========================================================
# movem-backend — multi-stage Dockerfile (Spring Boot 4.1 / Java 21)
# ==========================================================

# ---------- Stage 1: Build the jar with Maven ----------
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy only the POM first so dependencies are cached in their own layer.
# This means `docker build` won't re-download the internet every time
# you change a .java file — only when pom.xml changes.
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Now copy the source and build the real jar
COPY src ./src
RUN mvn clean package -DskipTests -B

# ---------- Stage 2: Slim runtime image ----------
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Run as a non-root user (good practice, and required by some
# cluster PodSecurity policies on GKE)
RUN addgroup --system spring && adduser --system --ingroup spring spring

COPY --from=build /app/target/movem-backend-0.0.1-SNAPSHOT.jar app.jar
RUN chown spring:spring app.jar
USER spring

EXPOSE 8080

# JAVA_OPTS is empty by default but can be overridden from the
# Deployment's env section (e.g. -XX:MaxRAMPercentage=75.0) without
# rebuilding the image.
ENV JAVA_OPTS=""
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar app.jar"]
