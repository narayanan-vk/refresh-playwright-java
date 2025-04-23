# refresh-playwright-java

[![Playwright Tests](https://github.com/narayanan-vk/refresh-playwright-java/actions/workflows/playwright.yml/badge.svg?branch=main)](https://github.com/narayanan-vk/refresh-playwright-java/actions/workflows/playwright.yml)

## View trace

``mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="show-trace test-results\com.neudesic.qa.AppTest.shouldCheckAddTodo-chromium\trace.zip``