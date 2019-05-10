/*
 * Project:sparrow nest
 * LastModified:18-4-17 下午4:36 by sukimiya
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

import io.e2x.sparrow.nest.security.model.OUserDetail;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {

    @GetMapping("/user")
    public String user(@AuthenticationPrincipal Principal principal, Model model){
        if(principal!=null){
            model.addAttribute("username", principal.getName());
        }else {
            return "redirect:/login";
        }

        return getPages("user/user.html",model);
    }

    public static boolean checkLogin(Model model) {
        Object principal= SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal!=null){
            if(principal instanceof OUserDetail){
                model.addAttribute("isAuthenticated",true);
                OUserDetail userDetail = (OUserDetail) principal;
                List<GrantedAuthority> authorities = (List<GrantedAuthority>)userDetail.getAuthorities();
                ArrayList<String> array = new ArrayList<String>();
                for(int i=0;i<authorities.size();i++){
                    array.add(authorities.get(i).getAuthority());
                }
                model.addAttribute("m78_auth",array);
                if(array.indexOf("DISPATCHER")!=-1) model.addAttribute("isHasDispatcher",true);
                else model.addAttribute("isHasDispatcher",false);
                if(array.indexOf("ADMIN")!=-1) model.addAttribute("isAdmin",true);
                else model.addAttribute("isAdmin",false);

                return true;
            }
            model.addAttribute("isAdmin",false);
            model.addAttribute("isHasDispatcher",false);
            model.addAttribute("isAuthenticated",false);
            return false;
        }
        model.addAttribute("isAdmin",false);
        model.addAttribute("isHasDispatcher",false);
        model.addAttribute("isAuthenticated",false);
        return false;
    }

    private String getPages(String page, Model model){
        checkLogin(model);
        return page;
    }

}
