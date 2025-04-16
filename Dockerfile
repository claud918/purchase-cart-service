FROM openjdk:17-jdk-slim

WORKDIR /mnt

RUN apt-get update && apt-get install -y maven

COPY . /mnt

RUN mvn clean package -DskipTests

EXPOSE 9090

CMD ["java", "-jar", "target/purchase-cart-service-0.0.1-SNAPSHOT.jar", "--server.port=9090"]
