package com.miro.widgetapp;

import com.miro.widgetapp.repositories.DBWidgetRepository;
import com.miro.widgetapp.repositories.IWidgetRepository;
import com.miro.widgetapp.repositories.InMemoryRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WidgetApplication {

    @Value("${widget.repository}")
    private String repositoryToUse;

    public static void main(String[] args) {
        SpringApplication.run(WidgetApplication.class, args);
    }

    @Bean
    public IWidgetRepository getWidgetRepository() {
        if(repositoryToUse.equals("db")) {
            return new DBWidgetRepository();
        }
        return new InMemoryRepository();
    }
}
