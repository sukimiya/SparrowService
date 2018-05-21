package io.e2x.sparrow.crud.mongod.vo.security;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

@Data
public class OAuthSimpleGrantedAuthority implements GrantedAuthority {
    private static final long serialVersionUID = 500L;
    private String role;
    public OAuthSimpleGrantedAuthority(String role) {
        Assert.hasText(role, "A granted authority textual representation is required");
        this.role = role;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else {
            return obj instanceof OAuthSimpleGrantedAuthority ? this.role.equals(((OAuthSimpleGrantedAuthority)obj).role) : false;
        }
    }

    @Override
    public String toString(){
        return getAuthority();
    }

    @Override
    public String getAuthority() {
        return this.role;
    }
}
