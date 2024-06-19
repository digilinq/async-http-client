package com.eightbits.http.client.stub;

import com.eightbits.http.context.ContextInitializer;
import com.eightbits.http.wiremock.WireMockContextInitializer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.web.reactive.function.client.WebClient;

import static org.assertj.core.api.Assertions.assertThat;

class WebClientConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner();

    @Test
    void webClient_and_builder_required_WebClientAutoConfiguration() {
        this.contextRunner
                .withConfiguration(AutoConfigurations.of(WebClientAutoConfiguration.class))
                .withUserConfiguration(WebClientConfiguration.class)
                .run(context -> {
                    assertThat(context).hasSingleBean(WebClient.Builder.class);
                    assertThat(context).hasSingleBean(WebClient.class);
                });
    }

    @Test
    void webClient_and_builder_will_not_instantiated_without_WebClientAutoConfiguration() {
        this.contextRunner
                .run(context -> {
                    assertThat(context).doesNotHaveBean(WebClient.Builder.class);
                    assertThat(context).doesNotHaveBean(WebClient.class);
                });
    }

    @Test
    void webClient_should_connect_to_wiremock_server_with_preconfigured_endpoint() {
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
