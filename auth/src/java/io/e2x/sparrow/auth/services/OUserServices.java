/*
 * Project:sparrow auth
 * LastModified:18-5-9 下午4:33 by sukimiya
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

package io.e2x.sparrow.auth.services;

import io.e2x.sparrow.crud.mongod.repo.security.OUserDetailRepository;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import reactor.core.publisher.Mono;

public class OUserServices implements ReactiveUserDetailsService {
    private OUserDetailRepository oUserDetailRepository;

    public OUserServices(OUserDetailRepository oUserDetailRepository) {
        this.oUserDetailRepository = oUserDetailRepository;
    }

    public Mono<UserDetails> findByUsername(String s) throws UsernameNotFoundException {
        return oUserDetailRepository.findByUsername(s).cast(UserDetails.class);
    }
}
