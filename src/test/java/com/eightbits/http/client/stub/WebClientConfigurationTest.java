package com.eightbits.http.client.stub;

import com.eightbits.http.context.ContextInitializer;
import com.eightbits.http.wiremock.WireMockContextInitializer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.web.reactive.function.client.WebClient;

class WebClientConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner();

    @Test
    void WebClient_Builder_will_be_configured_via_WebClientAutoConfiguration() {
        this.contextRunner
                .withConfiguration(AutoConfigurations.of(WebClientAutoConfiguration.class))
                .withUserConfiguration(WebClientConfiguration.class)
                .run(context -> {
                    Assertions.assertThat(context).hasSingleBean(WebClient.class);
                });
    }

    @Test
    void webClient_should_connect_to_wiremock_server(){
        this.contextRunner
                .withInitializer(new WireMockContextInitializer())
                .withInitializer(ContextInitializer::loadTestProperties)
                .withConfiguration(AutoConfigurations.of(WebClientAutoConfiguration.class))
                .withUserConfiguration(WebClientConfiguration.class)
                .run(context -> {
                    WebClient webClient = context.getBean(WebClient.class);
//                    Assertions.assertThat(webClient.baseUrl().toString()).isEqualTo("http://localhost:" + context.getEnvironment().getProperty("wiremock.server.port"));
                });
    }
}
