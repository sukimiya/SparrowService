/*
 * Project:sparrow nest
 * LastModified:18-4-13 上午11:07 by lily
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

package io.e2x.sparrow.nest.security.controller;

import io.e2x.sparrow.nest.security.model.OUserDetailRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class OUserServices implements UserDetailsService {
    private OUserDetailRepository oUserDetailRepository;

    public OUserServices(OUserDetailRepository oUserDetailRepository) {
        this.oUserDetailRepository = oUserDetailRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return oUserDetailRepository.findByUsername(s);
    }
}
