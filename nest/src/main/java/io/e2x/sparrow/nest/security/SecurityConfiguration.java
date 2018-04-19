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

import io.e2x.sparrow.nest.config.ConfigureLoader;
import io.e2x.sparrow.nest.config.SparrowConfiguration;
import io.e2x.sparrow.nest.config.SparrowConfigurationRepository;
import io.e2x.sparrow.nest.security.controller.OUserServices;
import io.e2x.sparrow.nest.security.model.OUserDetails;
import io.e2x.sparrow.nest.security.model.OUserDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.DefaultLoginPageConfigurer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.stream.Stream;

/**
 * Web安全配置，安全配置的第一道墙
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private OUserDetailRepository oUserDetailRepository;

    private ConfigureLoader configureLoader;

    @Autowired
    public SparrowConfigurationRepository s_config;

    /**
     * Web安全配置Constructor
     * 如果数据库里面没有Admin，Constructor会新建一个叫admin的用户，密码：{YWRtaW46VnJh.}. 请先自行修改admin密码.
     * @param oUserDetailRepository
     */
    @Autowired
    public SecurityConfiguration(OUserDetailRepository oUserDetailRepository,SparrowConfigurationRepository s_config){
        super();
        this.oUserDetailRepository = oUserDetailRepository;
        if(!oUserDetailRepository.existsByUsername("admin")){
            String[] roles = {"ADMIN","USER"};
            s_config.save(new SparrowConfiguration());
            this.s_config = s_config;
            configureLoader = new ConfigureLoader(s_config);
            oUserDetailRepository.save(new OUserDetails(configureLoader.getAdmin_name(),configureLoader.getAdmin_password(),true,true,true,true,roles));
        }
    }

    private static final String[] AUTH_WHITELIST = {

            // -- swagger ui
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v2/api-docs",
            "/webjars/**",
            "/oauth/*",
            "/static/**"
    };

    /**
     * 安全规则：
     * 1.白名单
     * 2.HttpMethod.OPTIONS
     * 3.正常的安全验证
     * 4.Disable csrf for REST
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .authorizeRequests()
                .antMatchers(AUTH_WHITELIST).permitAll()
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .anyRequest().authenticated().and()
                .formLogin().loginPage("/login").defaultSuccessUrl("/index").permitAll()
                .and().httpBasic()
                .and().csrf().disable().logout().logoutUrl("/logout").logoutSuccessUrl("/login")

        ;
    }

    /**
     * 密码的编码器，这个是安全系统必须提供的
     * @return
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    /**
     * 注入系统的UserService
     * @param auth
     * @throws Exception
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(getUserServices());
    }

    /**
     * 从MongoDB创建一个UserService实例
     * @return OUserServices
     */
    @Bean
    public OUserServices getUserServices(){
        return new OUserServices(oUserDetailRepository);
    }

}