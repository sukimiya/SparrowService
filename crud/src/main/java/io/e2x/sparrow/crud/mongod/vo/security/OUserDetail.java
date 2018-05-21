package io.e2x.sparrow.crud.mongod.vo.security;

import io.e2x.sparrow.crud.mongod.vo.utils.OAuthAuthorityUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;
import org.springframework.data.couchbase.core.mapping.id.GenerationStrategy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
public class OUserDetail implements UserDetails {

    public String getId() {
        return id;
    }

    @Id
    @GeneratedValue(strategy= GenerationStrategy.UNIQUE)
    private String id;
    private String username;
    private String password;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;

    public void setAuthorities(List<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    private List<GrantedAuthority> authorities;

    public OUserDetail(){

    }
    public OUserDetail(String username){
        this.username = username;
    }
    public OUserDetail(String username, String password, boolean enabled, boolean accountNonExpired, boolean accountNonLocked, boolean credentialsNonExpired, String[] authorities) {
        this.username = username;
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        this.password = encoder.encode(password);
        this.enabled = enabled;
        this.authorities = OAuthAuthorityUtils.createAuthorityList(authorities);
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }
    public void addARoleWithName(String role){
        this.authorities.add(new OAuthSimpleGrantedAuthority(role));
    }
    public void removeARoleWithName(String role){
        OAuthSimpleGrantedAuthority removeauth = null;
        for(GrantedAuthority auth:this.authorities){
            if(auth.getAuthority().equals(role)){
                removeauth = (OAuthSimpleGrantedAuthority) auth;
                break;
            }
        }
        if(removeauth!=null)
            this.authorities.remove(removeauth);
    }
    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}