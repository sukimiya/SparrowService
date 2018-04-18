/*
 * Project:sparrow nest
 * LastModified:18-4-10 下午8:42 by lily
 *
 * Copyright (C) 2018.  e2x.io
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package io.e2x.sparrow.nest.security;

import io.e2x.sparrow.nest.security.controller.OUserServices;
import io.e2x.sparrow.nest.security.model.OUserDetails;
import io.e2x.sparrow.nest.security.model.OUserDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.DefaultLoginPageConfigurer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.stream.Stream;

/**
 * This is example class by <a href="http://sgdev-blog.blogspot.jp/2016/04/spring-oauth2-with-jwt-sample.html">sgdev-blog</>
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private OUserDetailRepository oUserDetailRepository;

//    @Bean
//    CommandLineRunner initUserDetails(OUserDetailRepository oUserDetailRepository){
//
//            return args -> Stream.of("admin,password,true,true,true,false,ADMIN USER","user,password,true,true,true,false,USER","sukimiya,19547047,true,true,true,true,ADMIN USER GUARDER")
//                    .map(user ->user.split(","))
//                    .forEach(user->oUserDetailRepository.save(new OUserDetails(user[0],user[1],Boolean.parseBoolean(user[2]),Boolean.parseBoolean(user[3]),Boolean.parseBoolean(user[4]),Boolean.parseBoolean(user[6]),user[6].split(" "))));
//
//    }

    @Autowired
    public SecurityConfiguration(OUserDetailRepository oUserDetailRepository){
        super();
        this.oUserDetailRepository = oUserDetailRepository;
    }

    private static final String[] AUTH_WHITELIST = {

            // -- swagger ui
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v2/api-docs",
            "/webjars/**",
            "/oauth/*",
            "/static/**",
            "/login"
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .authorizeRequests()
//                .antMatchers(AUTH_WHITELIST).permitAll()
//                .antMatchers(HttpMethod.OPTIONS).permitAll()
//                .anyRequest().authenticated()
//                .and().httpBasic()
//                .and().csrf().disable();
        http
                .authorizeRequests()
                .antMatchers(AUTH_WHITELIST).permitAll()
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .anyRequest().authenticated().and()
                .formLogin().loginPage("/login").defaultSuccessUrl("/index")
                .and().httpBasic()
                .and().csrf().and().logout().logoutUrl("/logout").logoutSuccessUrl("/login")

        ;
    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public OUserServices getUserServices(){
        return new OUserServices(oUserDetailRepository);
    }

}