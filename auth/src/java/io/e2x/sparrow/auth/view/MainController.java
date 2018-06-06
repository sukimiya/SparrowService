/*
 * Project:sparrow auth
 * LastModified:18-5-21 下午6:17 by sukimiya
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

package io.e2x.sparrow.auth.view;


import io.e2x.sparrow.auth.view.event.AuthorityTypeSettingVO;
import io.e2x.sparrow.crud.mongod.repo.config.SparrowConfigurationRepository;
import io.e2x.sparrow.crud.mongod.repo.security.OAuthClientRepository;
import io.e2x.sparrow.crud.mongod.repo.security.OUserDetailRepository;
import io.e2x.sparrow.crud.mongod.repo.user.UnregistedUserRepository;
import io.e2x.sparrow.crud.mongod.vo.config.SparrowConfiguration;
import io.e2x.sparrow.crud.mongod.vo.security.OAuthClientDetail;
import io.e2x.sparrow.crud.mongod.vo.security.OUserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.*;

/**
 * Application home page and login.
 * ref:https://www.jianshu.com/p/a8e317e82425
 */
@Controller
@RequestMapping("/")
public class MainController {

    @Autowired
    public SparrowConfigurationRepository s_config;

    private final int LIST_PAGE_SIZE = 20;

    @Value("${resource.userid:spring-boot-application}")
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
    public Mono<String> root(final Model model){
        return getPages("/index.html",model);
    }

    @GetMapping("/login")
    public Mono<String> login(final Throwable throwable, @AuthenticationPrincipal Principal principal, final Model model){
        if(throwable!=null)
            model.addAttribute("error",throwable.getMessage());
        else
            model.addAttribute("error",null);
        return getPages("login.html",model);
    }
    @PreAuthorize("hasAuthority('GUARDER')")
    @GetMapping("/admin/index")
    public Mono<String> adminIndex(final Model model){
        Map<String,String> props = new HashMap<>(10);
        return oUserDetailRepository.count()
                .flatMap(numUser-> Mono.just(props.put("user",String.valueOf(numUser))))
                .and(oAuthClientRepository.count())
                .flatMap(numClient->Mono.just(props.put("client",String.valueOf(numClient))))
                .and(unregistedUserRepository.count())
                .flatMap(numUnreg->Mono.just(props.put("unreg",String.valueOf(numUnreg))))
                .then()
                .flatMap(res->getPages("/admin/adminhome.html",model.addAllAttributes(props)));
    }
    @PreAuthorize("hasAuthority('GUARDER')")
    @GetMapping("/admin/clients")
    public Mono<String> adminClients(final Model model){
        model.addAttribute("searchkey",null);
        List<OAuthClientDetail> listmap = new ArrayList<OAuthClientDetail>();
        return oAuthClientRepository.findAll().map(oAuthClientDetail -> listmap.add(oAuthClientDetail))
                .next()
                .then()
                .flatMap(res->{return getPages("/admin/adminclients.html",model);});
    }
    @PreAuthorize("hasAuthority('GUARDER')")
    @GetMapping("/admin/clientsSearch")
    public Mono<String> clientsSearch(@RequestParam("key") String key, final Model model){
        List<OAuthClientDetail> listmap =new  ArrayList<OAuthClientDetail>();
        model.addAttribute("listmap", listmap);
        model.addAttribute("searchkey",key);
        return oAuthClientRepository.findAllByClientIdOrDomainLike(key)
                .flatMap(oauthclient->Mono.just(listmap.add(oauthclient)))
                .then().flatMap(aVoid -> {return getPages("/admin/adminclients.html",model);});
    }
    @PreAuthorize("hasAuthority('GUARDER')")
    @PostMapping("/admin/client")
    public Mono<String> clientPut(@RequestParam("clientid") String clientid,@RequestParam("secret") String secret,@RequestParam("domain") String domain,final Model model){
        List<OAuthClientDetail> listmap =new  ArrayList<OAuthClientDetail>();
        model.addAttribute("listmap", listmap);
        model.addAttribute("searchkey","");
        String[] scope={"read","write"};
        return oAuthClientRepository.count().flatMap(count->{
            OAuthClientDetail oAuthClientDetail = new OAuthClientDetail(count.intValue(),clientid,secret,scope);
            oAuthClientDetail.setDomain(domain);
            return oAuthClientRepository.save(oAuthClientDetail);
        }).map(oAuthClientDetail -> {return oAuthClientRepository.findAll(Sort.by(Sort.Direction.DESC,"lastOp"));})
        .map(oAuthClientDetailFlux -> oAuthClientDetailFlux.map(listmap::add)).flatMap(res->{return getPages("/admin/adminclients.html",model);});
    }

    @PreAuthorize("hasAuthority('GUARDER')")
    @GetMapping("/admin/users")
    public Mono<String> allusers(@RequestParam(value = "page",required = false) String page,final Model model){
        int thepage = 0;
        if(page!=null&&Integer.valueOf(page)>0){
            thepage = Integer.valueOf(page);
        }
        Pageable pageable = new PageRequest(thepage,LIST_PAGE_SIZE,Sort.Direction.DESC,"id");
        List<OUserDetail> userDetailsList =new ArrayList<OUserDetail>();
        SparrowConfiguration config = getConfig();
        model.addAttribute("key",null);
        model.addAttribute("authorities",config.authoritiesTypes);
        model.addAttribute("listmap",userDetailsList);
        return oUserDetailRepository.findAll(Sort.by(Sort.Direction.DESC,"id"))
                .map(oUserDetail->{
                    userDetailsList.add(oUserDetail);
                    return Mono.just(oUserDetail);
                }).count()
                .flatMap(count ->{
                    model.addAttribute("listTotal",Math.floor(count/LIST_PAGE_SIZE));
                    model.addAttribute("listPage",page);
                    return getPages("/admin/adminusers.html",model);
                });
    }
    @PreAuthorize("hasAuthority('GUARDER')")
    @GetMapping("/admin/users/{username}/auth")
    public Mono<String> adminUserAuth(@PathVariable("username") String username,Model model){
        Mono<OUserDetail> userDetails = oUserDetailRepository.findByUsername(username);

        SparrowConfiguration config = getConfig();


        return userDetails.flatMap(userDetail -> {
            List<AuthorityTypeSettingVO> typelist = new ArrayList<AuthorityTypeSettingVO>();
            for(int i=0;i<config.authoritiesTypes.length;i++) {
                String atype = config.authoritiesTypes[i];
                AuthorityTypeSettingVO authset = new AuthorityTypeSettingVO();
                authset.setAuthority(atype);
                if(isContainAuth(userDetail.getAuthorities(),atype)){
                    authset.setEnabled(true);
                }else{
                    authset.setEnabled(false);
                }
                typelist.add(authset);
            }
            model.addAttribute("currentUser",userDetails);
            model.addAttribute("listmap",typelist);
            return getPages("/admin/usersetauth.html",model);
        });
    }
    private boolean isContainAuth(Collection<? extends GrantedAuthority> authorities, String auth){
        List<GrantedAuthority> auths = (List<GrantedAuthority>) authorities;
        for (GrantedAuthority authority: auths){
            if(authority.getAuthority().equals(auth)){
                return true;
            }
        }
        return false;
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
    private Flux<String> getPageFlux(String page, Model model){
        checkLogin(model);
        return Flux.just(page);
    }
    private Mono<String> getPages(String page, Model model){
        checkLogin(model);
        return Mono.just(page);
    }
    private SparrowConfiguration config;
    private SparrowConfiguration getConfig(){
        if(config==null)
            config = this.s_config.findAll().get(0);
        return config;
    }
}

