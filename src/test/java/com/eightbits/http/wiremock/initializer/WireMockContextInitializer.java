package com.eightbits.http.wiremock.initializer;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.Options;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;

public class WireMockContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        WireMockServer wiremock = new WireMockServer(Options.DYNAMIC_PORT);
        wiremock.start();
        WireMock.configureFor(wiremock.port());

        applicationContext.getBeanFactory().registerSingleton("wiremock", wiremock);
        applicationContext.addApplicationListener(event -> {
            if (event instanceof ContextClosedEvent) {
                System.out.println("Stopping WireMock server");
                wiremock.stop();
                System.out.println("WireMock server stopped");
            }
        });
        System.setProperty("wiremock.server.port", String.valueOf(wiremock.port()));
    }
}
