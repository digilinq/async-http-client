package com.eightbits.http.client;

import com.eightbits.http.client.properties.WebClientConfigurationProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;

@AutoConfiguration
@EnableConfigurationProperties(WebClientConfigurationProperties.class)
public class WebClientCustomizerAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "http.async.webclient.compression", name = "enabled", havingValue = "true")
    public WebClientCustomizer webClientCustomizer(WebClientConfigurationProperties properties) {
        return builder -> builder
                .defaultHeader(HttpHeaders.ACCEPT_ENCODING, properties.getCompression().getAlgorithm());
    }
}
