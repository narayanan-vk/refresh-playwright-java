FROM mcr.microsoft.com/playwright/java:v1.51.0-noble

WORKDIR /app

# Copy the Maven project
COPY . .

# Install Maven if not already in the image
RUN apt-get update && \
    apt-get install -y maven

# Set environment variables with defaults
ENV TEST_BROWSER=chromium \
    TEST_HEADLESS=true \
    TEST_CI=true \
    TEST_TAGS=UI \
    TEST_TRACE=RETAIN_ON_FAILURE

## Run the tests
#CMD mvn test -Dgroups="${TEST_TAGS}"