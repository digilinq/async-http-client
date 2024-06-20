package com.eightbits.http.context;

import org.springframework.boot.test.context.assertj.AssertableApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.ResourcePropertySource;

import java.io.IOException;
import java.util.Map;

public class ContextInitializer {
    public static void loadTestProperties(ConfigurableApplicationContext applicationContext) {
        System.out.printf("Loading test properties%n");
        try {
            GenericApplicationContext genericContext = (GenericApplicationContext) applicationContext;
            genericContext.getEnvironment().getPropertySources()
                    .addFirst(new ResourcePropertySource("classpath:application-test.properties"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties from application-test.properties", e);
        }
    }

    public static void printProperties(AssertableApplicationContext context) {
        // Get the environment
        ConfigurableEnvironment environment = context.getEnvironment();

        // Iterate through property sources and print properties
        for (PropertySource<?> propertySource : environment.getPropertySources()) {
            if (propertySource.getSource() instanceof Map) {
                Map<String, Object> source = (Map<String, Object>) propertySource.getSource();
                for (Map.Entry<String, Object> entry : source.entrySet()) {
                    System.out.println(entry.getKey() + "=" + entry.getValue());
                }
            }
        }
    }
}
