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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.e2x.sparrow.nest.security.model.OAuthClientDetail;
import io.e2x.sparrow.nest.security.model.OAuthClientRepository;
import io.e2x.sparrow.nest.security.model.OUserDetailRepository;
import io.e2x.sparrow.nest.security.model.OUserDetails;
import io.e2x.sparrow.nest.web.controller.event.AdministratorHomeEvent;
import io.e2x.sparrow.nest.web.events.OAuthClientDetailSetEvent;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Api(value = "/nest",tags = "admin Services")
@RestController
@RequestMapping(value = "/nest")
public class AdminRestController {

    @Value("${resource.id:spring-boot-application}")
    private String resourceId;

    private OAuthClientRepository oAuthClientRepository;
    private OUserDetailRepository oUserDetailRepository;

    private final int LIST_PAGE_SIZE=20;

    public AdminRestController(OAuthClientRepository oAuthClientRepository, OUserDetailRepository oUserDetailRepository) {
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
    @GetMapping("usersbypage/{page}")
    public Page<OUserDetails> getUsers(@PathVariable("page") Integer page){
        if(page<=0) page=0;
        Pageable pageable = new PageRequest(page,LIST_PAGE_SIZE,Sort.Direction.DESC,"id");
        return oUserDetailRepository.findAll(pageable);
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
    public Map<String,String> adminSetClientActive(@RequestBody OAuthClientDetailSetEvent jdoc, @JsonProperty(value = "enabled") String enabled, @JsonProperty(value = "clientid") String clientid){
        OAuthClientDetail oclient= oAuthClientRepository.findByClientId(jdoc.clientid);
        oclient.setEnabled(jdoc.enabled);
        oAuthClientRepository.save(oclient);
        Map<String,String> returnmap = new HashMap<String,String>();
        returnmap.put("clientId",oclient.getClientId());
        returnmap.put("enabled",oclient.getEnabled().toString());
        return returnmap;
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("hello")
    public String hello(){
        return "hello";
    }
}
