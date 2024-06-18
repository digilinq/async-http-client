package com.eightbits.http.client.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "http.async.webclient")
public class WebClientConfigurationProperties {

    public static class HttpCompression {
        public static final String GZIP = "gzip";
        public static final String DEFLATE = "deflate";

        private Boolean enabled = true;
        private String algorithm = GZIP;

        public Boolean getEnabled() {
            return enabled;
        }

        public void setEnabled(Boolean enabled) {
            this.enabled = enabled;
        }

        public String getAlgorithm() {
            return algorithm;
        }

        public void setAlgorithm(String algorithm) {
            this.algorithm = algorithm;
        }
    }

    private String baseUrl;
    private HttpCompression compression;
    private String apiKey;

    public HttpCompression getCompression() {
        return compression;
    }

    public void setCompression(HttpCompression compression) {
        this.compression = compression;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}

