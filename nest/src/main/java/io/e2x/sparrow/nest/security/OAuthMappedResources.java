
/*
 * Project:sparrow nest
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

package io.e2x.sparrow.nest.security;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.codehaus.jackson.annotate.JsonProperty;
import org.springframework.boot.actuate.autoconfigure.cloudfoundry.Token;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.UserDetailsManagerConfigurer;
import org.springframework.security.config.core.userdetails.UserDetailsMapFactoryBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.*;

@Api(value = "/scr",tags = "Authorisation Services")
@RestController
@RequestMapping("/scr")
public class OAuthMappedResources {

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value="user", method= RequestMethod.GET)
    public String helloUser() {
        return "hello user";
    }

    @GetMapping("userdetail")
    public Object userdetail(){
        return SecurityContextHolder.getContext().getAuthentication().getDetails();
    }

    @ApiOperation(value = "hello admin", notes = "This is a test method need been remove")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value="admin", method=RequestMethod.GET)
    public String helloAdmin() {
        return "hello admin";
    }

    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @RequestMapping(value="client", method=RequestMethod.GET)
    public String helloClient() {
        return "hello user authenticated by normal client";
    }

    @PreAuthorize("hasRole('ROLE_TRUSTED_CLIENT')")
    @RequestMapping(value="trusted_client", method=RequestMethod.GET)
    public String helloTrustedClient() {
        return "hello user authenticated by trusted client";
    }

    @RequestMapping(value="principal", method=RequestMethod.GET)
    public Object getPrincipal() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal;
    }

    @RequestMapping(value="roles", method=RequestMethod.GET)
    public Object getRoles() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities();
    }


    @PreAuthorize("hasRole('ROLE_GUARDER')")
    @GetMapping("localstring")
    public String localStr(){
        return "Hello!";
    }

}