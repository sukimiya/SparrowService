/*
 * Project:sparrow auth
 * LastModified:18-4-23 上午1:29 by sukimiya
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


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Date;

@ControllerAdvice
public class ErrorController {

    private static Logger logger = LoggerFactory.getLogger(ErrorController.class);

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String exception500(final Throwable throwable, final Model model) {
        return getPages("/error/500.html",500,throwable,model);
    }


    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String AccessDeniedException(final Throwable throwable,final Model model){
        return getPages("/error/403.html",403,throwable,model);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String notFoundException(final Throwable throwable,final Model model){
        return getPages("/error/404.html",404,throwable,model);
    }
    private String getPages(String pages,int status, Throwable throwable,Model model){
        String errorMessage = (throwable != null ? throwable.getMessage() : "Not Found error");
        model.addAttribute("status",status);
        model.addAttribute("dates",new Date());
        model.addAttribute("messages",errorMessage);
        model.addAttribute("error",throwable.getStackTrace().toString());
        return pages;
    }
}
