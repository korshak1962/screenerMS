FROM nginx:alpine
# Install diagnostic tools
RUN apt-get update && apt-get install -y \
    curl \
    netcat-openbsd \
    net-tools \
    procps \
    lsof \
    && rm -rf /var/lib/apt/lists/*
COPY src/main/resources/static /usr/share/nginx/html
COPY nginx.conf /etc/nginx/nginx.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]