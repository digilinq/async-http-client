package com.eightbits.http.wiremock;

import com.eightbits.http.wiremock.initializer.WireMockContextInitializer;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class WireMockInitializerTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner();

    @Test
    void custom_context_initializer() {
        this.contextRunner
                .withInitializer(new WireMockContextInitializer())
                .run(context -> {
                    assertThat(context.getEnvironment().getProperty("wiremock.server.port")).isNotNull();
                    System.out.printf("WireMock server is running on port %s%n", context.getEnvironment().getProperty("wiremock.server.port"));
                    assertThat(context.getBean("wiremock")).isNotNull();
                    WireMockServer wiremock = context.getBean(WireMockServer.class);
                    assertThat(wiremock.isRunning()).isTrue();
                });
    }
}
