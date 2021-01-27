package com.dtj503.lexicalanalyzer.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class GlobalCorsConfig implements WebMvcConfigurer {

    private static String[] origins;

    @Autowired
    public GlobalCorsConfig() {
        origins = new String[]{"http://localhost:3000", "http://localhost:5000"};
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/v1/analyse*")
                .allowedMethods(HttpMethod.POST.toString())
                .allowedOrigins(origins);
    }

}
