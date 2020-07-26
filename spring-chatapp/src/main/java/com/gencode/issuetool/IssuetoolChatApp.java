package com.gencode.issuetool;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class IssuetoolChatApp {
	@Value( "${cors_url}" )
    private String corsUrl;

	public static void main(String[] args) {
		SpringApplication.run(IssuetoolChatApp.class, args);
	}
	// Enable CORS globally
//	@Bean
//	public WebMvcConfigurer corsConfigurer() {
//		return new WebMvcConfigurer() {
//			@Override
//			public void addCorsMappings(CorsRegistry registry) {
//				registry.addMapping("**")
//				.allowedOrigins(corsUrl.split(","));
//			}
//		};
//	}
}
