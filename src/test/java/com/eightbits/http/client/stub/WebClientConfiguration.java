package com.eightbits.http.client.stub;

import com.eightbits.http.client.properties.WebClientConfigurationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;

@Configuration
@EnableConfigurationProperties(WebClientConfigurationProperties.class)
@ConditionalOnClass(WebClientAutoConfiguration.class)
public class WebClientConfiguration {

    private static final String API_KEY = "api-key";

    @Bean
    public WebClient webClient(@Autowired WebClient.Builder builder, WebClientConfigurationProperties properties) {
        return builder
                .baseUrl(properties.getBaseUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(API_KEY, properties.getApiKey())
                .clientConnector(clientHttpConnector())
                .build();
    }

    @Bean
    public ReactorClientHttpConnector clientHttpConnector()  {
        ConnectionProvider provider = ConnectionProvider.builder("custom")
                .maxIdleTime(Duration.ofSeconds(20))
                .evictInBackground(Duration.ofSeconds(120))
                .build();
        HttpClient httpClient = HttpClient.create(provider);
        return new ReactorClientHttpConnector(httpClient);
    }
}
