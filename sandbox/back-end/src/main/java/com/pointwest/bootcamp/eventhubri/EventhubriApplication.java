package com.pointwest.bootcamp.eventhubri;

import jakarta.servlet.Servlet;
import org.h2.server.web.JakartaWebServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class EventhubriApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventhubriApplication.class, args);
	}

	@Bean
    public ServletRegistrationBean<Servlet> h2servletRegistration() {
        // Uses JakartaWebServlet for Spring Boot 3+ / Jakarta EE compatibility
        ServletRegistrationBean<Servlet> registration = new ServletRegistrationBean<>(new JakartaWebServlet());
        registration.addUrlMappings("/h2-console/*");
        return registration;
    }
}
