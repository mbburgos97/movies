package com.theater.movies.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
public class AuthorizationConfiguration extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.cors().and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST,"/api/register").permitAll()
                .antMatchers(HttpMethod.GET,"/api/movies").permitAll()
                .antMatchers(HttpMethod.GET,"/api/artists").permitAll()
                .antMatchers(HttpMethod.GET,"/api/artist/**").permitAll()
                .antMatchers(HttpMethod.GET,"/api/movie/**").permitAll()
                .anyRequest().authenticated();
    }
}
