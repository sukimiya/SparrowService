package io.e2x.sparrow.crud.mongod.vo.security;

import io.e2x.sparrow.crud.mongod.vo.utils.OAuthAuthorityUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
public class OAuthClientDetail implements ClientDetails {

    public Integer getId() {
        return id;
    }

    private Integer id;

    public OAuthClientDetail setClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public OAuthClientDetail setResourceIds(String... resourceIds) {
        this.resourceIds = Set.of(resourceIds);
        return this;
    }

    public OAuthClientDetail setSecretRequired(boolean secretRequired) {
        this.secretRequired = secretRequired;
        return this;
    }

    public OAuthClientDetail setClientSecret(String clientSecret) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        this.clientSecret = encoder.encode(clientSecret);
        return this;
    }

    public OAuthClientDetail setScoped(boolean scoped) {
        this.scoped = scoped;
        return this;
    }

    public OAuthClientDetail setScope(Set<String> scope) {
        this.scope = scope;
        return this;
    }

    public OAuthClientDetail setAuthorizedGrantTypes(Set<String> authorizedGrantTypes) {
        this.authorizedGrantTypes = authorizedGrantTypes;
        return this;
    }

    public OAuthClientDetail setRegisteredRedirectUri(Set<String> registeredRedirectUri) {
        this.registeredRedirectUri = registeredRedirectUri;
        return this;
    }

    public OAuthClientDetail setAuthorities(String... authorities) {
        this.authorities = OAuthAuthorityUtils.createAuthorityList(authorities);
        return this;
    }

    public OAuthClientDetail setAccessTokenValiditySeconds(Integer accessTokenValiditySeconds) {
        this.accessTokenValiditySeconds = accessTokenValiditySeconds;
        return this;
    }

    public OAuthClientDetail setRefreshTokenValiditySeconds(Integer refreshTokenValiditySeconds) {
        this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
        return this;
    }

    public OAuthClientDetail setAutoApprove(boolean autoApprove) {
        this.autoApprove = autoApprove;
        return this;
    }

    private String clientId;
    private Set<String> resourceIds;
    private boolean secretRequired;
    private String clientSecret;
    private boolean scoped;
    private Set<String> scope;
    private Set<String> authorizedGrantTypes;
    private Set<String> registeredRedirectUri;
    private List<GrantedAuthority> authorities;
    private Integer accessTokenValiditySeconds;
    private Integer refreshTokenValiditySeconds;
    private boolean autoApprove;
    private Map<String, Object> additionalInformation;

    public Boolean getEnabled() {
        return enabled;
    }

    public OAuthClientDetail setEnabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    private Boolean enabled = true;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    private String domain;

    public long getLastOp() {
        return lastOp;
    }

    public OAuthClientDetail setLastOp(long lastOp) {
        this.lastOp = lastOp;
        return this;
    }
    public OAuthClientDetail of(long time){
        this.lastOp = time;
        return this;
    }
    private long lastOp;

    public OAuthClientDetail(Integer id, String clientId, String clientSecret, String[] scope) {
        this.id = id;
        this.clientId = clientId;
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        this.clientSecret = encoder.encode(clientSecret);
        this.scope = Set.of(scope);
        this.autoApprove = true;
        this.accessTokenValiditySeconds = 7200;
        this.additionalInformation = Map.of();
        this.lastOp = System.currentTimeMillis();
        this.resourceIds = Set.of();
        this.authorities = OAuthAuthorityUtils.createAuthorityList("ROLE_TRUSTED_CLIENT");
        this.authorizedGrantTypes = Set.of("authorization_code","implicit");
        this.scoped=true;
        this.secretRequired=false;
        this.refreshTokenValiditySeconds=7200;


    }

    public OAuthClientDetail(Integer id) {
        this.id = id;
        this.clientId = clientId;
        this.resourceIds = resourceIds;
        this.secretRequired = secretRequired;
        this.clientSecret = clientSecret;
        this.scoped = scoped;
        this.scope = scope;
        this.authorizedGrantTypes = authorizedGrantTypes;
        this.registeredRedirectUri = registeredRedirectUri;
        this.authorities = authorities;
        this.accessTokenValiditySeconds = accessTokenValiditySeconds;
        this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
        this.autoApprove = autoApprove;
        this.additionalInformation = additionalInformation;
        this.lastOp = lastOp;
    }

    public OAuthClientDetail() {

    }

    @Override
    public String getClientId() {
        return clientId;
    }

    @Override
    public Set<String> getResourceIds() {
        return resourceIds;
    }

    @Override
    public boolean isSecretRequired() {
        return secretRequired;
    }

    @Override
    public String getClientSecret() {
        return clientSecret;
    }

    @Override
    public boolean isScoped() {
        return scoped;
    }

    @Override
    public Set<String> getScope() {
        return scope;
    }

    @Override
    public Set<String> getAuthorizedGrantTypes() {
        return authorizedGrantTypes;
    }

    @Override
    public Set<String> getRegisteredRedirectUri() {
        return registeredRedirectUri;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Integer getAccessTokenValiditySeconds() {
        return accessTokenValiditySeconds;
    }

    @Override
    public Integer getRefreshTokenValiditySeconds() {
        return refreshTokenValiditySeconds;
    }

    @Override
    public boolean isAutoApprove(String s) {
        return autoApprove;
    }

    @Override
    public Map<String, Object> getAdditionalInformation() {
        return additionalInformation;
    }

}
