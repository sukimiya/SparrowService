package io.e2x.sparrow.sparrowwebconfiguration.client;

import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;

public class ClientResources {

    @NestedConfigurationProperty
    private AuthorizationCodeResourceDetails client = new AuthorizationCodeResourceDetails();
    AuthorizationCodeResourceDetails

    @NestedConfigurationProperty
    private ResourceServerProperties resource = new ResourceServerProperties();
    ResourceServerProperties

    public AuthorizationCodeResourceDetails getClient() {
        return client;
    }

    public ResourceServerProperties getResource() {
        return resource;
    }
}