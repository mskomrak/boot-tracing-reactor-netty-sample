package com.example.demo.config;

import io.micrometer.observation.ObservationHandler;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.ObservationTextPublisher;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.handler.DefaultTracingObservationHandler;
import io.micrometer.tracing.propagation.Propagator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.netty.http.observability.ReactorNettyPropagatingSenderTracingObservationHandler;
import reactor.netty.observability.ReactorNettyTracingObservationHandler;

import static reactor.netty.Metrics.OBSERVATION_REGISTRY;

@Configuration(proxyBeanMethods = false)
public class ObservabilityConfiguration {

    @Bean
    @ConditionalOnProperty(value = "demo.debug", havingValue = "true")
    ObservationTextPublisher observationTextPublisher() {
        return new ObservationTextPublisher();
    }

    @Bean
    ObservationRegistry observationRegistry(Tracer tracer, Propagator propagator) {
        OBSERVATION_REGISTRY.observationConfig()
                .observationHandler(
                        new ObservationHandler.FirstMatchingCompositeObservationHandler(
                                new ReactorNettyPropagatingSenderTracingObservationHandler(tracer, propagator),
                                new ReactorNettyTracingObservationHandler(tracer),
                                new DefaultTracingObservationHandler(tracer)
                        )
                );
        return OBSERVATION_REGISTRY;
    }
}
