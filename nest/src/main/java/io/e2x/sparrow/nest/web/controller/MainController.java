/*
 * Project:sparrow nest
 * LastModified:18-4-14 上午4:20 by lily
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

package io.e2x.sparrow.nest.web.controller;


import java.security.Principal;
import java.time.Instant;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.e2x.sparrow.nest.security.model.OAuthClientDetail;
import io.e2x.sparrow.nest.security.model.OAuthClientRepository;
import io.e2x.sparrow.nest.security.model.OUserDetailRepository;
import io.e2x.sparrow.nest.users.UnregistedUserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

/**
 * Application home page and login.
 * ref:https://www.jianshu.com/p/a8e317e82425
 */
@Controller
@RequestMapping("/")
public class MainController {

    @Value("${resource.id:spring-boot-application}")
    private String resourceId;

    private OAuthClientRepository oAuthClientRepository;
    private OUserDetailRepository oUserDetailRepository;
    private UnregistedUserRepository unregistedUserRepository;

    public MainController(OAuthClientRepository oAuthClientRepository, OUserDetailRepository oUserDetailRepository, UnregistedUserRepository unregistedUserRepository) {
        this.oAuthClientRepository = oAuthClientRepository;
        this.oUserDetailRepository = oUserDetailRepository;
        this.unregistedUserRepository = unregistedUserRepository;
    }

    @GetMapping({"/", "/index", "/home"})
    public String root(final Model model){
        return getPages("/index.html",model);
    }

    @GetMapping("/login")
    public String login(final Throwable throwable, @AuthenticationPrincipal Principal principal, final Model model){
        if(throwable!=null)
            model.addAttribute("error",throwable.getMessage());
        else
            model.addAttribute("error",null);
        return getPages("login.html",model);
    }
    @PreAuthorize("hasAuthority('GUARDER')")
    @GetMapping("/admin/index")
    public String adminIndex(final Model model){
        long numUser = oUserDetailRepository.count();
        long numClient = oAuthClientRepository.count();
        long numUnreg = unregistedUserRepository.count();
        Map<String,String> props = new HashMap<>(10);
        props.put("user",String.valueOf(numUser));
        props.put("client",String.valueOf(numClient));
        props.put("unreg",String.valueOf(numUnreg));
        model.addAttribute("summery", props);
        return getPages("/admin/adminhome.html",model);
    }
    @PreAuthorize("hasAuthority('GUARDER')")
    @GetMapping("/admin/clients")
    public String adminClients(final Model model){
        List<OAuthClientDetail> listmap = oAuthClientRepository.findAll();
        model.addAttribute("listmap", listmap);
        model.addAttribute("searchkey",null);
        Date.from(Instant.ofEpochSecond(9948484)).toLocaleString();
        return getPages("/admin/adminclients.html",model);
    }
    @PreAuthorize("hasAuthority('GUARDER')")
    @GetMapping("/admin/clientsSearch")
    public String clientsSearch(@RequestParam("key") String key, final Model model){

        List<OAuthClientDetail> listmap = oAuthClientRepository.findByClientIdLikeAndDomainLike(key);
        model.addAttribute("listmap", listmap);
        model.addAttribute("searchkey",key);
        Date.from(Instant.ofEpochSecond(9948484)).toLocaleString();
        return getPages("/admin/adminclients.html",model);
    }
    @PreAuthorize("hasAuthority('GUARDER')")
    @PostMapping("/admin/client")
    public String clientPut(@RequestParam("clientid") String clientid,@RequestParam("secret") String secret,@RequestParam("domain") String domain,final Model model){
        Integer id = (int) oAuthClientRepository.count();
        String[] scope={"read","write"};
        OAuthClientDetail oAuthClientDetail = new OAuthClientDetail(id,clientid,secret,scope);
        oAuthClientDetail.setDomain(domain);
        oAuthClientRepository.save(oAuthClientDetail);
        return getPages("/admin/adminclients.html",model);
    }

    @GetMapping("testpage")
    public String getBootstrapTest(){
        return "/bootstraptest.html";
    }


    private boolean checkLogin(Model model){
        Object principal= SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal!=null){
            model.addAttribute("isAuthenticated",true);
            return true;
        }
        model.addAttribute("isAuthenticated",false);
        return false;
    }
    private String getPages(String page, Model model){
        checkLogin(model);
        return page;
    }
}

