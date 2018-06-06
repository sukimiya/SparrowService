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

import com.fasterxml.jackson.annotation.JsonProperty;
import io.e2x.sparrow.auth.error.UserExceptionTypes;
import io.e2x.sparrow.auth.error.UserRegistError;
import io.e2x.sparrow.auth.view.event.AdminSettingUserAuthEvent;
import io.e2x.sparrow.auth.view.event.ResultEventTypes;
import io.e2x.sparrow.auth.view.event.StandardResponseEvent;
import io.e2x.sparrow.auth.view.events.OAuthClientDetailSetEvent;
import io.e2x.sparrow.auth.view.events.UserDetailAddEvent;
import io.e2x.sparrow.crud.mongod.repo.config.SparrowConfigurationRepository;
import io.e2x.sparrow.crud.mongod.repo.security.OAuthClientRepository;
import io.e2x.sparrow.crud.mongod.repo.security.OUserDetailRepository;
import io.e2x.sparrow.crud.mongod.repo.user.UserRepository;
import io.e2x.sparrow.crud.mongod.vo.security.OAuthClientDetail;
import io.e2x.sparrow.crud.mongod.vo.security.OUserDetail;
import io.e2x.sparrow.crud.mongod.vo.user.UserCurrency;
import io.e2x.sparrow.crud.mongod.vo.user.UserInformations;
import io.e2x.sparrow.crud.mongod.vo.user.UserSocialInformations;
import io.e2x.sparrow.crud.mongod.vo.user.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

//@Api(value = "/nest",tags = "admin Services")
@RestController
@RequestMapping(value = "/nest")
public class AdminRestController {

    @Autowired
    public SparrowConfigurationRepository s_config;

    @Value("${resource.userid:spring-boot-application}")
    private String resourceId;

    private OAuthClientRepository oAuthClientRepository;
    private OUserDetailRepository oUserDetailRepository;
    private UserRepository userInfoRepository;

    private final int LIST_PAGE_SIZE=20;

    public AdminRestController(OAuthClientRepository oAuthClientRepository, OUserDetailRepository oUserDetailRepository,UserRepository userInfoRepository) {
        this.userInfoRepository = userInfoRepository;
        this.oAuthClientRepository = oAuthClientRepository;
        this.oUserDetailRepository = oUserDetailRepository;
    }

    @GetMapping("adminallusers")
    public Flux<OUserDetail> adminHomeAllUsers(){
        Flux<OAuthClientDetail> oAuthClientDetailFlux = oAuthClientRepository.findAll();
        Flux<OUserDetail> oUserDetailFlux = oUserDetailRepository.findAll();
        return oUserDetailFlux;
    }
    @PreAuthorize("hasAuthority('GUARDER')")
    @GetMapping("clientsbypage/{page}")
    public Flux<OAuthClientDetail> getClients(@PathVariable("page") Integer page){
        if(page<=0) page=0;
        Pageable pageable = new PageRequest(page,LIST_PAGE_SIZE,Sort.Direction.DESC,"id");
        return oAuthClientRepository.findAll((Sort) pageable);
    }
    @PreAuthorize("hasAuthority('GUARDER')")
    @GetMapping("usersbypage/{page}")
    public Flux<OUserDetail> getUsers(@PathVariable("page") Integer page){
        if(page<=0) page=0;
        Pageable pageable = new PageRequest(page,LIST_PAGE_SIZE,Sort.Direction.DESC,"id");
        return oUserDetailRepository.findAll((Sort) pageable);
    }
    @PreAuthorize("hasAuthority('GUARDER')")
    @PostMapping(value = "user/add")
    public Mono<UserDetailAddEvent> useradd(@RequestBody UserDetailAddEvent user) throws UserRegistError{
        if(user.username!=null&&user.password!=null&user.email!=null) {
            String[] roles = {"USER"};
            Mono<Boolean> isexist = oUserDetailRepository.existsByUsername(user.username);
            return isexist.flatMap(res -> {
                if (res) {
                    return Mono.error(new UserRegistError(UserExceptionTypes.USER_EXIST));
                } else {
                    OUserDetail newUserDetails = new OUserDetail(user.username, user.password, user.enabled, true, true, true, roles);
                    return oUserDetailRepository.save(newUserDetails);
                }

            }).flatMap((OUserDetail user1) -> userRegist(user))
                    .onErrorReturn(new UserDetailAddEvent(1, "regist error"));

        }
        return Mono.just(new UserDetailAddEvent(1, "incorrect parameter"));
    }
    private Mono<UserDetailAddEvent> userRegist(UserDetailAddEvent user){
        Mono<UserDetailAddEvent> userDetailAddEventMono = null;
        try {
            userDetailAddEventMono= oUserDetailRepository.findByUsername(user.username)
                    //.flatMap(this::userRegistReportUserExist)
                    .flatMap(userDetail -> {
                        UserInformations userInformations = new UserInformations(userDetail.getId().toString(), new UserCurrency(0, 0), new UserSocialInformations(user.firstname, user.lastname, "", user.email, "", ""));
                        return userInfoRepository.save(new UserVO(userDetail.getId().toString(), userInformations, userDetail));
                    }).flatMap(userVO -> {return Mono.just(new UserDetailAddEvent(userVO.getInformations().getUserSocialInformations().getFirstname(),
                            userVO.getInformations().getUserSocialInformations().getLastname(),
                            userVO.getAuth().getUsername(),
                            userVO.getAuth().getPassword(),
                            userVO.getInformations().getUserSocialInformations().getEmail(),
                            userVO.getAuth().isEnabled(),
                            userVO.getId()
                    ));});
        }catch (Exception error){
            return Mono.error(error);
        }

        return userDetailAddEventMono;
    }
    private Mono<OUserDetail> userRegistReportUserExist(Mono<OUserDetail> user) throws UserRegistError{
        if(user==null) throw new UserRegistError(UserExceptionTypes.USER_EXIST);
        return user;
    }
//    @PreAuthorize("hasAuthority('GUARDER')")
//    @PostMapping(value = "client/{clientid}",consumes = "application/json")
//    public OAuthClientDetail postClientById(@PathVariable("clientid") String clientid,@JsonProperty("secret") String secret){
//        if (secret==null) secret = clientid;
//        Integer userid = UUID.randomUUID().hashCode();
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        String[] scope = {"read","write"};
//        OAuthClientDetail oAuthClientDetail = new OAuthClientDetail(userid,clientid,encoder.encode(secret),scope);
//        oAuthClientDetail.setResourceIds(resourceId);
//        oAuthClientDetail.setAuthorities("TRUSTED_CLIENT");
//        oAuthClientRepository.save(oAuthClientDetail);
//        return oAuthClientDetail;
//    }
    @PreAuthorize("hasAuthority('GUARDER')")
    @GetMapping("client/{clientid}")
    public Mono<OAuthClientDetail> getClientById(@PathVariable("clientid") String clientid){
        return oAuthClientRepository.findByClientId(clientid);
    }
    @PreAuthorize("hasAuthority('GUARDER')")
    @RequestMapping(method = RequestMethod.POST, value = "adminSetClientActive",consumes = "application/json")
    public Mono<StandardResponseEvent> adminSetClientActive(@RequestBody OAuthClientDetailSetEvent jdoc, @JsonProperty(value = "isenabled") String enabled, @JsonProperty(value = "clientid") String clientid){
        return oAuthClientRepository.findByClientId(jdoc.clientid)
                .flatMap(oAuthClientDetail -> {
                    return oAuthClientRepository.save(oAuthClientDetail.setEnabled(jdoc.enabled));
                })
                .flatMap(oAuthClientDetail -> {return Mono.just(new StandardResponseEvent(0,""+oAuthClientDetail.getClientId()+",is enabled"));
                });
    }
    @PreAuthorize("hasAuthority('GUARDER')")
    @RequestMapping(method = RequestMethod.POST, value = "adminSetUserAuth",consumes = "application/json")
    public Mono<ResultEventTypes> adminSetUserAuth(@RequestBody AdminSettingUserAuthEvent jdoc){

        return  oUserDetailRepository.findByUsername(jdoc.username)
                .flatMap(userDetail -> {
                    for(GrantedAuthority auth:userDetail.getAuthorities()){
                        if(jdoc.authority.equals(auth.getAuthority())){
                            if(!jdoc.enabled){ userDetail.addARoleWithName(jdoc.authority);}
                        }
                    }
                    userDetail.removeARoleWithName(jdoc.authority);
                    return oUserDetailRepository.save(userDetail);
                }).flatMap(userDetail -> Mono.just(ResultEventTypes.OK));
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("hello")
    public Mono<String> hello(){
        return Mono.just("hello");
    }
}
