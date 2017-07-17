package com.panda.web;

import com.panda.domain.Greet;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Panda.HuangWei.
 * @since 2017-07-16 14:25.
 */
@RestController
public class GreetingController {
    private static final String template = "Hello, %s!";

    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/greeting")
    public Greet greeting() {
        return new Greet(counter.incrementAndGet(), String.format(template, "greeting"));
    }

    @GetMapping("/hello")
    public Greet hello() {
        return new Greet(counter.incrementAndGet(), String.format(template, "hello"));
    }
}
