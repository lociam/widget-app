package com.miro.widgetapp;

import com.miro.widgetapp.repositories.IWidgetRepository;
import com.miro.widgetapp.repositories.InMemoryRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WidgetApplication {

    public static void main(String[] args) {
        SpringApplication.run(WidgetApplication.class, args);
    }

    @Bean
    public IWidgetRepository getWidgetRepository() {
        return new InMemoryRepository();
    }
}
