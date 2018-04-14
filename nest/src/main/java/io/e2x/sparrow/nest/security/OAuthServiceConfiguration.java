/*
 * Project:sparrow nest
 * LastModified:18-4-10 下午8:33 by lily
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

import io.e2x.sparrow.nest.security.model.OAuthClientDetail;
import io.e2x.sparrow.nest.security.controller.OAuthClientDetailsServices;
import io.e2x.sparrow.nest.security.model.OAuthClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.Set;
import java.util.stream.Stream;

@Configuration
@EnableAuthorizationServer
public class OAuthServiceConfiguration extends AuthorizationServerConfigurerAdapter {

    @Value("${resource.id:spring-boot-application}")
    private String resourceId;

    @Value("${access_token.validity_period:3600}")
    int accessTokenValiditySeconds = 3600;

    private AuthenticationManager authenticationManager;
    private OAuthClientRepository clientRepository;

    @Autowired
    public OAuthServiceConfiguration(OAuthClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter(){
        return new JwtAccessTokenConverter();
    }


    @Bean
    CommandLineRunner initData(OAuthClientRepository oAuthClientRepository){
        //clientRepository = oAuthClientRepository;
        return args -> Stream.of("100001,clientNo1,65EADF92D174C67A03EAB015A8416F6F,read write","100002,clientNo2,65EADF92D174C67A03EAB015A8416F6F,read,write")
                .map(tpl -> tpl.split(","))
                .forEach(tpl ->
                        oAuthClientRepository.save(new OAuthClientDetail(Integer.parseInt(tpl[0]),tpl[1],tpl[2], Set.of(tpl[3].split(" ")))
                                .setResourceIds(resourceId)
                                .setAuthorities("ROLE_TRUSTED_CLIENT")
                        ));
    }

    @Bean
    public OAuthClientDetailsServices getClientDetails(){
        return new OAuthClientDetailsServices(clientRepository);
    }

    public OAuthServiceConfiguration(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {

        oauthServer.tokenKeyAccess("isAnonymous() || hasAuthority('ROLE_TRUSTED_CLIENT')")
                .checkTokenAccess("hasAuthority('ROLE_TRUSTED_CLIENT')");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(getClientDetails()).build();
        //JdbcTokenStore
        //JdbcClientDetailsServiceBuilder
        //https://github.com/spring-projects/spring-security-oauth/blob/master/spring-security-oauth2/src/test/resources/schema.sql
        //clients.inMemory()
//                .withClient("normal-app")
//                .authorizedGrantTypes("authorization_code", "implicit").autoApprove(true)
//                .authorities("ROLE_CLIENT")
//                .scopes("read", "write")
//                .resourceIds(resourceId)
//                .accessTokenValiditySeconds(accessTokenValiditySeconds)
//                .and()
//                .withClient("trusted-app")
//                .authorizedGrantTypes("client_credentials", "password")
//                .authorities("ROLE_TRUSTED_CLIENT")
//                .scopes("read", "write")
//                .resourceIds(resourceId)
//                .accessTokenValiditySeconds(accessTokenValiditySeconds)
//                .secret("secret").and().build();
    }
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .authenticationManager(this.authenticationManager)
                .accessTokenConverter(accessTokenConverter());
    }
}
