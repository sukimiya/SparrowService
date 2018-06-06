/*
 * Project:sparrow auth
 * LastModified:18-4-22 下午2:17 by sukimiya
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

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MVCExceptionHandler implements HandlerExceptionResolver {

    public MVCExceptionHandler(){
        int a = 1 ;
    }

    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
                                         Exception ex) {
//        Map<String, Object> model = new HashMap<String, Object>();
//        model.put("ex", ex);
        ex.printStackTrace();
        ModelAndView view = new ModelAndView("/common/error.btl");
        view.addObject("errorMessage", ex);
        view.addObject("timestamp",System.currentTimeMillis());
        view.addObject("status",response.getStatus());
        return view;
    }
}