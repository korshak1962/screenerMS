FROM node:alpine

# Install serve
RUN npm install -g serve
# Install curl
RUN apk add --no-cache curl
# Copy your static files
COPY src/main/resources/static /app
# Rename HTML files
RUN for file in /app/*.html; do mv "$file" "${file%.html}"; done

EXPOSE 5000

# Start serve
CMD ["serve", "-s", "/app", "-l", "5000"]