package com.hulkhiretech.payments.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.session.DisableEncodeUrlFilter;

import com.hulkhiretech.payments.security.ExceptionHandlerFilter;
import com.hulkhiretech.payments.security.HmacSha256Filter;
import com.hulkhiretech.payments.service.HmacSha256Service;
import com.hulkhiretech.payments.util.JsonUtil;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final HmacSha256Service HmacSha256Service;
	private final JsonUtil jsonUtil;
	
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        
    	http
        
            .csrf(csrf -> csrf.disable())
            
            .authorizeHttpRequests((authorize) -> authorize
                    .anyRequest().permitAll()
                )
        
           /* TODO: below is commented temporarily for the testing purpose
            * DONT COMMIT IT 
            
            .authorizeHttpRequests((authorize) -> authorize
                .anyRequest().authenticated()
            )
            
            .addFilterBefore(new ExceptionHandlerFilter(jsonUtil), 
					DisableEncodeUrlFilter.class)
            
            .addFilterAfter(new HmacSha256Filter(HmacSha256Service, jsonUtil), 
            		LogoutFilter.class)
            */
            
            .sessionManagement(session -> session
				.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
			);	
        
        

        return http.build();
    }
}
