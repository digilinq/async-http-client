package com.eightbits.http.client;

import com.eightbits.http.client.properties.WebClientConfigurationProperties;
import com.eightbits.http.client.stub.WebClientConfiguration;
import com.eightbits.http.context.ContextInitializer;
import com.eightbits.http.wiremock.WireMockContextInitializer;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.web.reactive.function.client.WebClient;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureWireMock(port = 0, stubs = {"classpath:wiremockstubs/mappings"}, files = {"classpath:wiremockstubs"})
class WebClientCustomizerAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner();

    @BeforeEach
    void setUp() {

    }

    @Test
    void given_web_client_should_customize_web_client_when_compression_enabled() {
        ApplicationContextRunner applicationContextRunner = this.contextRunner
                .withInitializer(new WireMockContextInitializer())
                .withInitializer(ContextInitializer::loadTestProperties);

        applicationContextRunner
                .withConfiguration(AutoConfigurations.of(WebClientAutoConfiguration.class))
                .withUserConfiguration(WebClientConfiguration.class)
                .withUserConfiguration(WebClientCustomizerAutoConfiguration.class)
                .run(context -> {
                    String compression = context.getEnvironment().getProperty("http.async.webclient.compression.enabled");

                    assertThat(compression).isEqualTo("true");
                    assertThat(context).hasSingleBean(WebClientCustomizer.class);
                });
    }

    @Test
    void given_wiremock_initialized_when_call_endpoint_it_should_return() {
        this.contextRunner
                .withInitializer(new WireMockContextInitializer())
                .withInitializer(ContextInitializer::loadTestProperties)
                .withConfiguration(AutoConfigurations.of(WebClientAutoConfiguration.class))
                .withUserConfiguration(WebClientConfiguration.class)
                .withUserConfiguration(WebClientCustomizerAutoConfiguration.class)
                .run(context -> {
                    WireMockServer wireMockServer = context.getBean(WireMockServer.class);
                    assertThat(wireMockServer.isRunning()).isTrue();

                    WireMock.stubFor(WireMock.get("/api/v1/employees")
                            .willReturn(WireMock.ok("body")));
                    WebClient webClient = context.getBean(WebClient.class);
                    assertThat(webClient).isNotNull();

                    webClient.get().uri("/api/v1/employees").retrieve().bodyToMono(String.class).block();

                    WireMock.verify(
                            WireMock.getRequestedFor(WireMock.urlEqualTo("/api/v1/employees"))
                                    .withHeader("Accept-Encoding", WireMock.equalTo(WebClientConfigurationProperties.GZIP))
                    );
                });
    }
}
