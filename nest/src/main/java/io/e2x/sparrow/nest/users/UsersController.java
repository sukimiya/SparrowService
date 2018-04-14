/*
 * Project:sparrow nest
 * LastModified:18-4-14 上午1:19 by lily
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

package io.e2x.sparrow.nest.users;


import io.e2x.sparrow.nest.security.model.OUserDetailRepository;
import io.e2x.sparrow.nest.security.model.OUserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UsersController {

    public final OUserDetailRepository oUserDetailRepository;

    public UsersController(OUserDetailRepository oUserDetailRepository) {
        this.oUserDetailRepository = oUserDetailRepository;
    }

    @GetMapping("all")
    public List<OUserDetails> allUsers(){
        List<OUserDetails> allUsers = oUserDetailRepository.findAll();
        return allUsers;
    }
}
