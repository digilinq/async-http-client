package com.eightbits.http.context;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.support.ResourcePropertySource;

import java.io.IOException;

public class ContextInitializer {
    public static void loadTestProperties(ConfigurableApplicationContext applicationContext) {
        try {
            GenericApplicationContext genericContext = (GenericApplicationContext) applicationContext;
            genericContext.getEnvironment().getPropertySources()
                    .addFirst(new ResourcePropertySource("classpath:application-test.properties"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties from application-test.properties", e);
        }
    }
}
