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
import java.util.logging.Logger;

import io.e2x.sparrow.nest.config.SparrowConfiguration;
import io.e2x.sparrow.nest.config.SparrowConfigurationRepository;
import io.e2x.sparrow.nest.security.model.OAuthClientDetail;
import io.e2x.sparrow.nest.security.model.OAuthClientRepository;
import io.e2x.sparrow.nest.security.model.OUserDetailRepository;
import io.e2x.sparrow.nest.security.model.OUserDetail;
import io.e2x.sparrow.nest.users.UnregistedUserRepository;
import io.e2x.sparrow.nest.web.controller.event.AuthorityTypeSettingVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Application home page and login.
 * ref:https://www.jianshu.com/p/a8e317e82425
 */
@Controller
@RequestMapping("/")
public class MainController {

    Logger logger = Logger.getLogger(MainController.class.getName());

    @Autowired
    public SparrowConfigurationRepository s_config;

    private final int LIST_PAGE_SIZE = 20;

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

        List<OAuthClientDetail> listmap = oAuthClientRepository.findAllByClientIdLike(key);
        List<OAuthClientDetail> listmap2 = oAuthClientRepository.findAllByDomainLike(key);
        listmap.addAll(listmap2);
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
        model.addAttribute("searchkey","");
        model.addAttribute("listmap",oAuthClientRepository.findAll(Sort.by(Sort.Direction.DESC,"lastOp")));
        return getPages("/admin/adminclients.html",model);
    }

    @PreAuthorize("hasAuthority('GUARDER')")
    @GetMapping("/admin/users")
    public String allusers(@RequestParam(value = "page",required = false) String page,final Model model){
        int thepage = 0;
        if(page!=null&&Integer.valueOf(page)>0){
            thepage = Integer.valueOf(page);
        }
        Pageable pageable = new PageRequest(thepage,LIST_PAGE_SIZE,Sort.Direction.DESC,"id");
        Page<OUserDetail> users= oUserDetailRepository.findAll(pageable);
        List<OUserDetail> userDetailsList = users.getContent();
        SparrowConfiguration config = getConfig();
        model.addAttribute("authorities",config.authoritiesTypes);
        model.addAttribute("listmap",userDetailsList);
        model.addAttribute("listTotal",users.getTotalPages());
        model.addAttribute("listPage",users.getNumber());
        model.addAttribute("key",null);
        return getPages("/admin/adminusers.html",model);
    }
    @PreAuthorize("hasAuthority('GUARDER')")
    @GetMapping("/admin/users/{username}/auth")
    public String adminUserAuth(@PathVariable("username") String username,Model model){
        OUserDetail userDetails = oUserDetailRepository.findByUsername(username);
        SparrowConfiguration config = getConfig();
        List<AuthorityTypeSettingVO> typelist = new ArrayList<AuthorityTypeSettingVO>();
        for(int i=0;i<config.authoritiesTypes.length;i++){
            String atype = config.authoritiesTypes[i];
            AuthorityTypeSettingVO authset = new AuthorityTypeSettingVO();
            authset.setAuthority(atype);

            if(isContainAuth(userDetails.getAuthorities(),atype)){
                authset.setEnabled(true);
            }else{
                authset.setEnabled(false);
            }
            typelist.add(authset);

        }
        model.addAttribute("currentUser",userDetails);
        model.addAttribute("listmap",typelist);
        return getPages("/admin/usersetauth.html",model);
    }
    @PostMapping("sitereview.jsp")
    public String sitereview(@RequestParam("url") String url, final Model model){
        logger.info("sitereview: "+url);
        String[] strings = url.split(":9080");
        String path = strings[1];

        return getPages(path,model);
    }

    @PostMapping("v2x-service/terminal/data/report")
    public String v2xService(final Model model,
                             @RequestParam("latitude") String lat,
                             @RequestParam("longitude") String lng,
                             @RequestParam("speed") String speed,
                             @RequestParam("heading") String heading
                             ){
        logger.info("POST v2x-service/terminal/data/report:\nlat:"+lat+" lng:"+lng);
        return getPages("index.html",model);
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
    private String getPages(String page, Model model){
        checkLogin(model);
        return page;
    }
    private SparrowConfiguration config;
    private SparrowConfiguration getConfig(){
        if(config==null)
            config = this.s_config.findAll().get(0);
        return config;
    }
}

