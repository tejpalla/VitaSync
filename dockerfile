# Build the application
./gradlew clean package

# Build Docker image
docker build -t vitasync-app .

# Run with Docker Compose
docker-compose up -d

# Check logs
docker-compose logs -f vitasync-app