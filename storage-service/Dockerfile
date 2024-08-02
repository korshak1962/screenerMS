FROM openjdk:21-jdk-slim
#FROM cafapi/opensuse-jre21:latest
# Install diagnostic tools
RUN apt-get update && apt-get install -y \
    curl \
    netcat-openbsd \
    net-tools \
    procps \
    lsof \
    && rm -rf /var/lib/apt/lists/*
VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]