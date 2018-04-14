/*
 * Project:sparrow nest
 * LastModified:18-4-12 下午10:40 by lily
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

import io.e2x.sparrow.nest.security.model.OAuthSimpleGrantedAuthority;
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
