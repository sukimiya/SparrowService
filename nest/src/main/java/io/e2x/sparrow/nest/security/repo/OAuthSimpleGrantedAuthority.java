/*
 * Project:sparrow nest
 * LastModified:18-4-12 下午10:32 by lily
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

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
