package com.test.collaboration;

import com.test.collaboration.filter.JWTFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

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

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http
				.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/auth/**").permitAll()
						.anyRequest().authenticated()
				)
				.build();
	}


}
