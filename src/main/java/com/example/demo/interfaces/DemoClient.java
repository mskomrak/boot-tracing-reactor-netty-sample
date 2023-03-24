package com.example.demo.interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Component
public class DemoClient {
    private static final Logger log = LoggerFactory.getLogger(DemoClient.class);

    private final HttpClient httpClient;

    private final ObjectMapper objectMapper;

    DemoClient(HttpClient httpClient, ObjectMapper objectMapper) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    public Mono<String> fetchData() {
        log.info("Fetching data from client");
        return httpClient.get()
                .uri("https://httpbin.org/get")
                .responseSingle((httpClientResponse, byteBufMono) -> byteBufMono.asString());
    }

    public Mono<String> submitData() {
        log.info("Submitting data from client with hardcoded data");
        return httpClient.post()
                .uri("https://httpbin.org/post")
                .send((httpClientRequest, outbound) -> {
                    try {
                        ByteBuf byteBuf = Unpooled.wrappedBuffer(objectMapper.writeValueAsBytes(new DemoMessage("Hello world!")));
                        return outbound.send(Mono.just(byteBuf)).then();
                    } catch (JsonProcessingException e) {
                        return Mono.error(e);
                    }
                })
                .responseSingle((httpClientResponse, byteBufMono) -> byteBufMono.asString());
    }

    record DemoMessage(String message) {
    }
}
