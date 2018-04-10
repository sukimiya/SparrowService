/*
 * Project:sparrow sparrowwebconfiguration
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

package io.e2x.sparrow.sparrowwebconfiguration;

import io.e2x.sparrow.sparrowwebconfiguration.client.ClientResources;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;

public class SSOFilter extends OAuth2ClientAuthenticationProcessingFilter {

    private OAuth2RestTemplate template;
    private OAuth2ClientContext clientContext;
    private ClientResources clientResources;
    private String path;

    public SSOFilter(OAuth2ClientContext clientContext,ClientResources clientResources, String path){
        super(path);
        this.path = path;
        this.clientContext = clientContext;
        this.clientResources = clientResources;
        initFilter();
    }

    void initFilter(){
        template = new OAuth2RestTemplate(clientResources.getClient(), clientContext);
        this.setRestTemplate(template);
        UserInfoTokenServices tokenServices = new UserInfoTokenServices(clientResources.getResource().getUserInfoUri(),clientResources.getClient().getClientId());
        tokenServices.setRestTemplate(template);
        setTokenServices(tokenServices);
    }
}
