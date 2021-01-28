package com.dtj503.lexicalanalyzer.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
/**
 * Configuration class used by Spring to set up Cross Origin Resource Sharing (CORS) and allow certain origins access
 * to the REST API endpoints.
 */
public class GlobalCorsConfig implements WebMvcConfigurer {

    private static String[] origins;

    @Autowired
    /**
     * Autowired constructor which sets the allowed Origins.
     * TODO add these origins to the environment variables and add them here through constructor value injection
     */
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
