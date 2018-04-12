/*
 * Project:sparrow nest
 * LastModified:18-4-11 下午10:55 by lily
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

package io.e2x.sparrow.nest.security.repo;

import io.e2x.sparrow.nest.security.OAuthAuthorityUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OAuthClientDetail implements ClientDetails {

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

    public OAuthClientDetail(Integer id, String clientId, String clientSecret, Set<String> scope) {
        this.id = id;
        this.clientId = clientId;
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        this.clientSecret = encoder.encode(clientSecret);
        this.scope = scope;
        this.autoApprove = true;
        this.accessTokenValiditySeconds = 7200;
        this.additionalInformation = Map.of();
        this.lastOp = new Date().getTime();
        this.resourceIds = Set.of();
        this.authorities = OAuthAuthorityUtils.createAuthorityList("ROLE_TRUSTED_CLIENT");
        this.authorizedGrantTypes = Set.of("authorization_code","implicit");
        this.scoped=true;
        this.secretRequired=false;
        this.refreshTokenValiditySeconds=7200;


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
