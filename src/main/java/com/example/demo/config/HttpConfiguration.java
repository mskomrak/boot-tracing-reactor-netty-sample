package com.example.demo.config;

import io.netty.handler.logging.LogLevel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.netty.NettyPipeline;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

import java.util.function.Function;

@Configuration(proxyBeanMethods = false)
public class HttpConfiguration {

    @Bean
    HttpClient httpClient() {
        return HttpClient.create()
                .metrics(true, Function.identity())
                .doOnChannelInit((obs, ch, addr) ->
                        ch.pipeline()
                                .addFirst(TracingChannelInboundHandler.NAME, TracingChannelInboundHandler.INSTANCE)
                                .addBefore(NettyPipeline.HttpMetricsHandler, TracingChannelOutboundHandler.NAME,
                                        TracingChannelOutboundHandler.INSTANCE))
                .wiretap("reactor.netty.http.client.HttpClient", LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL);
    }

}
