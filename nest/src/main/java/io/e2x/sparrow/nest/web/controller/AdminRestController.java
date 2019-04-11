/*
 * Project:sparrow nest
 * LastModified:18-4-17 下午4:48 by sukimiya
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

import com.fasterxml.jackson.annotation.JsonProperty;
import io.e2x.sparrow.nest.config.SparrowConfigurationRepository;
import io.e2x.sparrow.nest.security.model.*;
import io.e2x.sparrow.nest.users.UserInfoRepository;
import io.e2x.sparrow.nest.users.vo.UserCurrency;
import io.e2x.sparrow.nest.users.vo.UserInformations;
import io.e2x.sparrow.nest.users.vo.UserSocialInformations;
import io.e2x.sparrow.nest.web.controller.event.AdminSettingUserAuthEvent;
import io.e2x.sparrow.nest.web.controller.event.AdministratorHomeEvent;
import io.e2x.sparrow.nest.web.controller.event.ResultEventTypes;
import io.e2x.sparrow.nest.web.events.OAuthClientDetailSetEvent;
import io.e2x.sparrow.nest.web.events.UserDetailAddEvent;
import io.e2x.sparrow.nest.web.events.UserDetailEditEvent;
import io.swagger.annotations.Api;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Api(value = "/nest",tags = "admin Services")
@RestController
@RequestMapping(value = "/nest")
public class AdminRestController {

    @Autowired
    public SparrowConfigurationRepository s_config;

    @Value("${resource.id:spring-boot-application}")
    private String resourceId;

    private OAuthClientRepository oAuthClientRepository;
    private OUserDetailRepository oUserDetailRepository;
    private UserInfoRepository userInfoRepository;

    private final int LIST_PAGE_SIZE=20;

    public AdminRestController(OAuthClientRepository oAuthClientRepository, OUserDetailRepository oUserDetailRepository,UserInfoRepository userInfoRepository) {
        this.userInfoRepository = userInfoRepository;
        this.oAuthClientRepository = oAuthClientRepository;
        this.oUserDetailRepository = oUserDetailRepository;
    }

    @GetMapping("adminall")
    public AdministratorHomeEvent adminHomeAll(){

        AdministratorHomeEvent adminHomeEvent = new AdministratorHomeEvent();
        Pageable pageable = new PageRequest(0,LIST_PAGE_SIZE);
        adminHomeEvent.setClients(oAuthClientRepository.findAll());
        adminHomeEvent.setUsers(oUserDetailRepository.findAll());
        return adminHomeEvent;
    }
    @PreAuthorize("hasAuthority('GUARDER')")
    @GetMapping("clientsbypage/{page}")
    public Page<OAuthClientDetail> getClients(@PathVariable("page") Integer page){
        if(page<=0) page=0;
        Pageable pageable = new PageRequest(page,LIST_PAGE_SIZE,Sort.Direction.DESC,"id");
        return oAuthClientRepository.findAll(pageable);
    }
    @PreAuthorize("hasAuthority('GUARDER')")
    @RequestMapping(method = RequestMethod.POST, value = "getUserByUsername")
    public UserDetailEditEvent getUserInfoByUsername(@RequestParam("username") String username){
        OUserDetail userDetail = oUserDetailRepository.findByUsername(username);
        if(userDetail != null){
            UserInformations userInformations = userInfoRepository.findUserInformationsByUserDetailId(userDetail.getId());
            if(userInformations != null){
                UserSocialInformations userSocialInformations = userInformations.getUserSocialInformations();
                return new UserDetailEditEvent(userSocialInformations.getFirstname(),
                        userSocialInformations.getLastname(),userDetail.getUsername(),
                        "",
                        userSocialInformations.getEmail(),
                        userDetail.getId());
            }
        }
        return null;
    }
    @PreAuthorize("hasAuthority('GUARDER')")
    @GetMapping("usersbypage/{page}")
    public Page<OUserDetail> getUsers(@PathVariable("page") Integer page){
        if(page<=0) page=0;
        Pageable pageable = new PageRequest(page,LIST_PAGE_SIZE,Sort.Direction.DESC,"id");
        return oUserDetailRepository.findAll(pageable);
    }
    @PreAuthorize("hasAuthority('GUARDER')")
    @PostMapping(value = "user/add")
    public UserDetailAddEvent useradd(@RequestBody UserDetailAddEvent user){
        if(user.username!=null&&user.password!=null&user.email!=null){
            String[] roles = {"USER"};
            if(oUserDetailRepository.existsByUsername(user.username)){
                return null;
            }
            OUserDetail newUserDetails = new OUserDetail(user.username,UUID.randomUUID().toString(), user.password,user.enabled,true,true,true,roles);
            oUserDetailRepository.save(newUserDetails);
            String id = newUserDetails.getId();
            UserInformations userInformations = new UserInformations(newUserDetails, new UserCurrency(0,0),new UserSocialInformations(user.firstname,user.lastname,"",user.email,"",""));
            userInfoRepository.save(userInformations);
            return new UserDetailAddEvent(userInformations.getUserSocialInformations().getFirstname(),
                    userInformations.getUserSocialInformations().getLastname(),
                    newUserDetails.getUsername(),null,
                    userInformations.getUserSocialInformations().getEmail(),
                    true,
                    newUserDetails.getId());
        }
        return null;
    }
//    @PreAuthorize("hasAuthority('GUARDER')")
//    @PostMapping(value = "client/{clientid}",consumes = "application/json")
//    public OAuthClientDetail postClientById(@PathVariable("clientid") String clientid,@JsonProperty("secret") String secret){
//        if (secret==null) secret = clientid;
//        Integer id = UUID.randomUUID().hashCode();
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        String[] scope = {"read","write"};
//        OAuthClientDetail oAuthClientDetail = new OAuthClientDetail(id,clientid,encoder.encode(secret),scope);
//        oAuthClientDetail.setResourceIds(resourceId);
//        oAuthClientDetail.setAuthorities("TRUSTED_CLIENT");
//        oAuthClientRepository.save(oAuthClientDetail);
//        return oAuthClientDetail;
//    }
    @PreAuthorize("hasAuthority('GUARDER')")
    @GetMapping("client/{clientid}")
    public OAuthClientDetail getClientById(@PathVariable("clientid") String clientid){
        return oAuthClientRepository.findByClientId(clientid);
    }
    @PreAuthorize("hasAuthority('GUARDER')")
    @RequestMapping(method = RequestMethod.POST, value = "adminSetClientActive",consumes = "application/json")
    public Map<String,String> adminSetClientActive(@RequestBody OAuthClientDetailSetEvent jdoc, @JsonProperty(value = "isenabled") String enabled, @JsonProperty(value = "clientid") String clientid){
        OAuthClientDetail oclient= oAuthClientRepository.findByClientId(jdoc.clientid);
        oclient.setEnabled(jdoc.enabled);
        oAuthClientRepository.save(oclient);
        Map<String,String> returnmap = new HashMap<String,String>();
        returnmap.put("clientId",oclient.getClientId());
        returnmap.put("isenabled",oclient.getEnabled().toString());
        return returnmap;
    }
    @PreAuthorize("hasAuthority('GUARDER')")
    @RequestMapping(method = RequestMethod.POST, value = "adminSetUserAuth",consumes = "application/json")
    public ResultEventTypes adminSetUserAuth(@RequestBody AdminSettingUserAuthEvent jdoc){

        OUserDetail user = oUserDetailRepository.findByUsername(jdoc.username);
        if(user!=null){

                user.getAuthorities();
                for(GrantedAuthority auth : user.getAuthorities()){
                    if(auth.getAuthority() == jdoc.authority){
                        return ResultEventTypes.OK.setReason("Authority exist.");
                    }
                }
            if(jdoc.enabled) {
                user.addARoleWithName(jdoc.authority);
            }else {
                user.removeARoleWithName(jdoc.authority);
            }
            oUserDetailRepository.save(user);

            return ResultEventTypes.OK;
        }

        return ResultEventTypes.UNKWON_ERROR;
    }
    @PreAuthorize("hasAuthority('GUARDER')")
    @PostMapping(value = "user/edit")
    public ResultEventTypes adminEditUser(@RequestBody UserDetailEditEvent jdoc){
        OUserDetail user = oUserDetailRepository.findByUsername(jdoc.username);
        UserInformations userInformations = userInfoRepository.findUserInformationsByUserDetailId(user.getId());
        if(user!=null){
            user.setPassword(jdoc.password);
            userInformations.getUserSocialInformations().setFirstname(jdoc.firstname);
            userInformations.getUserSocialInformations().setLastname(jdoc.lastname);
            userInformations.getUserSocialInformations().setEmail(jdoc.email);
            userInfoRepository.save(userInformations);
            oUserDetailRepository.save(user);
            return ResultEventTypes.OK;
        }
        return ResultEventTypes.UNKWON_ERROR;
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("hello")
    public String hello(){
        return "hello";
    }
}
