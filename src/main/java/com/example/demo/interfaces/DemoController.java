package com.example.demo.interfaces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/data")
public class DemoController {

    private static final Logger log = LoggerFactory.getLogger(DemoController.class);

    private final DemoClient demoCLient;

    DemoController(DemoClient demoCLient) {
        this.demoCLient = demoCLient;
    }

    @GetMapping
    public Mono<String> get() {
        return this.demoCLient.fetchData()
                .doOnNext(s -> log.info("Returning data from controller"));
    }

    @PostMapping
    public Mono<String> post() {
        return this.demoCLient.submitData()
                .doOnNext(s -> log.info("Returning data from controller"));
    }
}
