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


import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.unbescape.html.HtmlEscape;

/**
 * Application home page and login.
 */
@RestController
@RequestMapping("/")
public class MainController {

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping("/")
    public String root(Locale locale) {
        return "redirect:/index.html";
    }


    /** Home page. */
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping("/index.html")
    public String index() {
        return "index";
    }

    /** User zone index. */
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping("/user/index.html")
    public String userIndex() {
        return "user/index";
    }

    /** Administration zone index. */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping("/admin/index.html")
    public String adminIndex() {
        return "admin/index";
    }

    /** Shared zone index. */
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping("/shared/index.html")
    public String sharedIndex() {
        return "shared/index";
    }

    /** Login form. */
    @RequestMapping("/login.html")
    public String login() {
        return "login";
    }

    /** Login form with error. */
    @RequestMapping("/login-error.html")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login";
    }

    /** Simulation of an exception. */
    @RequestMapping("/simulateError.html")
    public void simulateError() {
        throw new RuntimeException("This is a simulated error message");
    }

    /** Error page. */
    @RequestMapping("/error.html")
    public String error(HttpServletRequest request, Model model) {
        model.addAttribute("errorCode", "Error " + request.getAttribute("javax.servlet.error.status_code"));
        Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append("<ul>");
        while (throwable != null) {
            errorMessage.append("<li>").append(HtmlEscape.escapeHtml5(throwable.getMessage())).append("</li>");
            throwable = throwable.getCause();
        }
        errorMessage.append("</ul>");
        model.addAttribute("errorMessage", errorMessage.toString());
        return "error";
    }

    /** Error page. */
    @RequestMapping("/403.html")
    public String forbidden() {
        return "403";
    }


}
