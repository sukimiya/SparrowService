package io.e2x.sparrow.crud.mongod.vo.utils;

import io.e2x.sparrow.crud.mongod.vo.security.OAuthSimpleGrantedAuthority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.StringUtils;

import java.util.*;

public abstract class OAuthAuthorityUtils {
    public static final List<GrantedAuthority> NO_AUTHORITIES = Collections.emptyList();

    public OAuthAuthorityUtils() {
    }

    public static List<GrantedAuthority> commaSeparatedStringToAuthorityList(String authorityString) {
        return createAuthorityList(StringUtils.tokenizeToStringArray(authorityString, ","));
    }

    public static Set<String> authorityListToSet(Collection<? extends GrantedAuthority> userAuthorities) {
        Set<String> set;
        if(userAuthorities!=null){
            set = new HashSet(userAuthorities.size());
            Iterator var2 = userAuthorities.iterator();

            while(var2.hasNext()) {
                GrantedAuthority authority = (GrantedAuthority)var2.next();
                set.add(authority.getAuthority());
            }
        }else{
            set=new HashSet();
        }

        return set;
    }

    public static List<GrantedAuthority> createAuthorityList(String... roles) {
        List<GrantedAuthority> authorities = new ArrayList(roles.length);
        String[] var2 = roles;
        int var3 = roles.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            String role = var2[var4];
            authorities.add(new OAuthSimpleGrantedAuthority(role));
        }

        return authorities;
    }
}
