package com.example.demo.config;

import io.netty.handler.logging.LogLevel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.logbook.Logbook;
import org.zalando.logbook.netty.LogbookClientHandler;
import reactor.netty.NettyPipeline;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

import java.nio.charset.Charset;
import java.util.function.Function;

@Configuration(proxyBeanMethods = false)
public class HttpConfiguration {

    @Bean
    HttpClient httpClient(Logbook logbook) {
        return HttpClient.create()
                .metrics(true, Function.identity())
                .doOnChannelInit((obs, ch, addr) ->
                        ch.pipeline()
                                .addFirst(TracingChannelInboundHandler.NAME, TracingChannelInboundHandler.INSTANCE)
                                .addBefore(NettyPipeline.HttpMetricsHandler, TracingChannelOutboundHandler.NAME,
                                        TracingChannelOutboundHandler.INSTANCE)
                                .addBefore(TracingChannelOutboundHandler.NAME, "logbook", new LogbookClientHandler(logbook)))
                .doOnConnected(c -> c.addHandlerFirst(AdvancedByteBufFormat.TEXTUAL.toLoggingHandler(
                        "reactor.netty.http.client.HttpClient", LogLevel.INFO, Charset.defaultCharset())))
                ;
    }

}
