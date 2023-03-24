package com.example.demo.config;

import io.netty.handler.logging.LogLevel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

import java.util.function.Function;

@Configuration(proxyBeanMethods = false)
public class HttpConfiguration {

    @Bean
    HttpClient httpClient() {
        return HttpClient.create()
                .metrics(true, Function.identity())
//        same issue as wiretap logger
//                .doOnConnected(c -> c.addHandlerFirst(AdvancedByteBufFormat.TEXTUAL.toLoggingHandler(
//                        "reactor.netty.http.client.HttpClient", LogLevel.INFO, Charset.defaultCharset())));
                .wiretap("reactor.netty.http.client.HttpClient", LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL);
//        zalando logbook also does not include trace and span id in the logs when added as dependency and applied
    }

}
