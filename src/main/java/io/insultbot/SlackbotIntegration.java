package io.insultbot;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class SlackbotIntegration {

    public static void main(String... args) {
        new SpringApplicationBuilder(SlackbotIntegration.class)
                .run(args);
    }
}
