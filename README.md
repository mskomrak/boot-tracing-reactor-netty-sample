Steps to reproduce:
1. run the service, run Zipkin using the [docker-compose.yml](stack/docker-compose.yml)
2. execute `curl -X POST http://127.0.0.1:8080/data`
3. observe the logs - traceId is present
   * logbook logger (`org.zalando.logbook.Logbook`) statements present for both read and write along with tracing data
   * reactor-netty log (`reactor.netty.http.client.HttpClient`) statement present for all events along with tracing data
4. execute another `curl -X POST http://127.0.0.1:8080/data` (almost immediately following the previous one)
   * reactor-netty log (`reactor.netty.http.client.HttpClient`) statement present, but traceId missing for some events (e.g. flush, read)
   * logbook logger (`org.zalando.logbook.Logbook`) statements missing
5. wait some time (e.g. several minutes), execute another `curl -X POST http://127.0.0.1:8080/data`
    * logbook logger (`org.zalando.logbook.Logbook`) statements present for both read and write along with tracing data
    * reactor-netty logs (`reactor.netty.http.client.HttpClient`) statement present for all events along with tracing data