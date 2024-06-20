package com.eightbits.http.wiremock;

import com.eightbits.http.context.ContextInitializer;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class WireMockContextInitializerTest {
    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner();


    @Test
    void ensure_wiremock_is_running() {
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

    @Test
    void wiremock_endpoint_should_be_represented_in_the_properties() {
        this.contextRunner
                .withInitializer(new WireMockContextInitializer())
                .withInitializer(ContextInitializer::loadTestProperties).run(context -> {
                    assertThat(context.getEnvironment().getProperty("http.async.webclient.base-url")).isNotNull();
                    assertThat(context.getEnvironment().getProperty("http.async.webclient.base-url")).matches("http://localhost:\\d+");
                });
    }
}
