# Build Frontend
FROM node:22 AS frontend-build
WORKDIR /app/frontend
COPY frontend/book-n-fly/package.json frontend/book-n-fly/package-lock.json ./
# Install dependencies first to enable layer caching
RUN npm install 
COPY frontend/book-n-fly/ ./
RUN npm run build

# Build Backend
FROM jelastic/maven:3.9.9-temurinjdk-21.0.6-almalinux-9 AS backend-build
WORKDIR /app/backend
COPY backend/booknfly/pom.xml ./
RUN mvn dependency:go-offline
COPY backend/booknfly ./
RUN mvn clean package -DskipTests

# Final Image
FROM jelastic/maven:3.9.9-temurinjdk-21.0.6-almalinux-9
WORKDIR /app
COPY --from=backend-build /app/backend/target/*.jar app.jar
COPY --from=frontend-build /app/frontend/dist /app/static

# Environment variables
ENV API_KEY=${API_KEY}
ENV API_URL=${API_URL}

# Expose port
EXPOSE 8080

# Run Spring Boot application
CMD ["java", "-jar", "app.jar"]
