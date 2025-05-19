# refresh-playwright-java

[![Playwright Tests](https://github.com/narayanan-vk/refresh-playwright-java/actions/workflows/playwright.yml/badge.svg?branch=main)](https://github.com/narayanan-vk/refresh-playwright-java/actions/workflows/playwright.yml)

[![pages-build-deployment](https://github.com/narayanan-vk/refresh-playwright-java/actions/workflows/pages/pages-build-deployment/badge.svg)](https://github.com/narayanan-vk/refresh-playwright-java/actions/workflows/pages/pages-build-deployment)

[![Build Status](https://dev.azure.com/NVKTestsStuff/JavaTestFramework/_apis/build/status%2Fnarayanan-vk.refresh-playwright-java?branchName=main)](https://dev.azure.com/NVKTestsStuff/JavaTestFramework/_apis/build/status%2Fnarayanan-vk.refresh-playwright-java?branchName=main)

## Parallel execution
Refer to [Parallel execution using GitHub Actions](/docs/PARALLEL-EXECUTION.md) to know more.

## View trace

```
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="show-trace test-results\com.neudesic.qa.AppTest.shouldCheckAddTodo-chromium\trace.zip
```

## Run tests via pre-built image

```
podman run -p 3000:3000 --rm --init -it --workdir /home/pwuser --user pwuser mcr.microsoft.com/playwright:v1.51.0-noble /bin/sh -c "npx -y playwright@1.51.0 run-server --port 3000 --host 0.0.0.0"
```

If seeing weird errors when launching Chromium, try running your container with `--cap-add=SYS_ADMIN` when developing locally.

## Run tests in container

### Build image

```
podman build -t playwright-tests:local .
```

### Run container

```
podman run --rm -e TEST_BROWSER=firefox -e TEST_HEADLESS=true -e TEST_TAGS=UI -e TEST_TRACE=ON -e TEST_CI=true -v "$(pwd)/test-results:/app/target" playwright-tests:local
```

## Run tests in local k8 using minikube

### Setup minikube

Assuming minikube is setup.
```
minikube start
```

Dashboard for visualisation.
```
minikube dashboard
```

### Build image directly in minikube

```
& minikube -p minikube docker-env --shell powershell | Invoke-Expression
```

```
podman build -t playwright-tests:local .
```

### Create a job to run tests

Delete the job before applying in case it exist.
```
kubectl delete jobs playwright-runner
```

```
 kubectl apply -f playwright-job.yaml
```

## Reporting

### Report portal [Deprecated]

Setup report portal in JUnit5 following this guide
> https://github.com/reportportal/agent-java-junit5#readme

Setup report portal in kubernetes following this guide 
> https://reportportal.io/docs/installation-steps/DeployWithKubernetes

Access report portal
```
minikube service -n ingress-nginx ingress-nginx-controller
```
