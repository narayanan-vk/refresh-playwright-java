FROM mcr.microsoft.com/playwright/java:v1.51.0-noble

WORKDIR /app

# Copy the Maven project
COPY pom.xml .
COPY src ./src
COPY junit-platform.properties .

# Install dependencies and build the project
RUN apt-get update && \
    apt-get install -y maven && \
    mvn dependency:go-offline

# Command to run the tests and ensure XML reports are generated
CMD ["mvn", "test", "-Dsurefire.useFile=true"]