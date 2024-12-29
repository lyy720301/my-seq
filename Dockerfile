# Build stage
FROM maven:3.9-amazoncorretto-17 AS builder

# Copy source code
COPY . /app/
WORKDIR /app

# Build the application
RUN mvn clean package -DskipTests

# Runtime stage
FROM amazoncorretto:17-alpine

# Install required packages including bash
RUN apk add --no-cache tzdata bash
ENV TZ=Asia/Shanghai

# Create app directory
WORKDIR /app

# Copy built artifacts from builder stage
COPY --from=builder /app/backend/target/*.jar app.jar
COPY --from=builder /app/interface/target/*.jar lib/
COPY --from=builder /app/consumer/target/*.jar lib/

# Copy wait-for-it script
COPY docker/wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh

# Expose ports
EXPOSE 9999 11111

# Set environment variables
ENV JAVA_OPTS="-Xms512m -Xmx512m -XX:+UseG1GC"

# Wait for MySQL and ZooKeeper to be ready before starting the application
ENTRYPOINT ["/wait-for-it.sh", "mysql-a:3306", "mysql-b:3306", "zookeeper:2181", "--timeout=60", "--", "java", "-jar", "app.jar"] 