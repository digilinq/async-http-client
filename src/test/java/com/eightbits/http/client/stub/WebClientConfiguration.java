package com.eightbits.http.client.stub;

import com.eightbits.http.client.properties.WebClientConfigurationProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;

@AutoConfiguration(after = WebClientAutoConfiguration.class)
@EnableConfigurationProperties(WebClientConfigurationProperties.class)
@ConditionalOnBean(WebClientAutoConfiguration.class)
public class WebClientConfiguration {

    private static final String API_KEY = "api-key";

    @Bean
    @ConditionalOnMissingBean
    public WebClient webClient(WebClient.Builder builder, WebClientConfigurationProperties properties) {
        return builder
                .baseUrl(properties.getBaseUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(API_KEY, properties.getApiKey())
                .clientConnector(clientHttpConnector())
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    public ReactorClientHttpConnector clientHttpConnector() {
        ConnectionProvider provider = ConnectionProvider.builder("custom")
                .maxIdleTime(Duration.ofSeconds(20))
                .evictInBackground(Duration.ofSeconds(120))
                .build();
        HttpClient httpClient = HttpClient.create(provider);
        return new ReactorClientHttpConnector(httpClient);
    }
}
