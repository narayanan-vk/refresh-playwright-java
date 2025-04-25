# refresh-playwright-java

[![Playwright Tests](https://github.com/narayanan-vk/refresh-playwright-java/actions/workflows/playwright.yml/badge.svg?branch=main)](https://github.com/narayanan-vk/refresh-playwright-java/actions/workflows/playwright.yml)

## View trace

```
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="show-trace test-results\com.neudesic.qa.AppTest.shouldCheckAddTodo-chromium\trace.zip
```

## Execute via pre-built image

```
podman run -p 3000:3000 --rm --init -it --workdir /home/pwuser --user pwuser mcr.microsoft.com/playwright:v1.51.0-noble /bin/sh -c "npx -y playwright@1.51.0 run-server --port 3000 --host 0.0.0.0"
```

If seeing weird errors when launching Chromium, try running your container with `--cap-add=SYS_ADMIN` when developing locally.