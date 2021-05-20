package com.theater.movies.config;

import com.theater.movies.util.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
public class AuthorizationConfiguration extends ResourceServerConfigurerAdapter {

    @Value("${file.folder}")
    private String folderPath;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        FileUtil.UPLOAD_FOLDER = folderPath;

        http.cors().and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST,"/dashboard/register").permitAll()
                .antMatchers(HttpMethod.GET,"/member/movies").permitAll()
                .antMatchers(HttpMethod.GET,"/member/movie/**").permitAll()
                .antMatchers(HttpMethod.GET,"/member/artists").permitAll()
                .antMatchers(HttpMethod.GET,"/member/artist/**").permitAll()
                .antMatchers(HttpMethod.GET,"/member/logos").permitAll()
                .antMatchers(HttpMethod.GET,"/member/logo/**").permitAll()
                .antMatchers(HttpMethod.GET,"/image/**").permitAll()
                .antMatchers(HttpMethod.GET,"/video/**").permitAll()
                .anyRequest().authenticated();
    }
}
