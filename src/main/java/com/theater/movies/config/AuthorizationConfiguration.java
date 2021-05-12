package com.theater.movies.config;

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
                .antMatchers(HttpMethod.POST,"/dashboard/register").permitAll()
                .antMatchers(HttpMethod.GET,"/member/movies").permitAll()
                .antMatchers(HttpMethod.GET,"/member/artists").permitAll()
                .antMatchers(HttpMethod.GET,"/member/artist/**").permitAll()
                .antMatchers(HttpMethod.GET,"/member/movie/**").permitAll()
                .antMatchers(HttpMethod.GET,"/image/**").permitAll()
                .antMatchers(HttpMethod.GET,"/video/**").permitAll()
                .anyRequest().authenticated();
    }
}
