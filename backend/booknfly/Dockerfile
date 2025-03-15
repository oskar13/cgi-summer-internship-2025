FROM maven:3.9.9-eclipse-temurin-21 AS backend-build
WORKDIR /app
COPY pom.xml ./
RUN mvn dependency:go-offline
COPY . ./
RUN mvn clean package -DskipTests

# Final Runtime Image
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=backend-build /app/target/*.jar app.jar

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]