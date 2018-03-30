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
