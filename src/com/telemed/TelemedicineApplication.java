package com.telemed;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
// Password encoder removed per request to store raw passwords
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class TelemedicineApplication {

    public static void main(String[] args) {
        SpringApplication.run(TelemedicineApplication.class, args);
        System.out.println("\n===========================================");
        System.out.println("🏥 Telemedicine Application Started");
        System.out.println("📍 Server running at: http://localhost:8080");
        System.out.println("===========================================\n");
    }

    // Password encoding removed: passwords will be stored as provided

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*");
            }
        };
    }
}
