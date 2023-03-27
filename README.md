Steps to reproduce:
1. run the service, run Zipkin using the [docker-compose.yml](stack/docker-compose.yml)
2. execute `curl -X GET http://127.0.0.1:8080/data` or `curl -X POST http://127.0.0.1:8080/data`
3. wiretap logs are missing tracing data