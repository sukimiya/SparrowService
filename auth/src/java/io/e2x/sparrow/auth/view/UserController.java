/*
 * Project:sparrow auth
 * LastModified:18-4-22 下午10:28 by sukimiya
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

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

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
