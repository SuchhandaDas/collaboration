package com.test.collaboration;

import com.test.collaboration.filter.JWTFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CollaborationApplication {

	public static void main(String[] args) {
		SpringApplication.run(CollaborationApplication.class, args);
	}

	@Bean
	public FilterRegistrationBean<JWTFilter> jwtFilter(JWTFilter filter) {
		FilterRegistrationBean<JWTFilter> reg = new FilterRegistrationBean<>();
		reg.setFilter(filter);
		reg.addUrlPatterns("/*");
		return reg;
	}


}
