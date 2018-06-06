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

import io.e2x.sparrow.crud.mongod.repo.security.OAuthClientRepository;
import io.e2x.sparrow.crud.mongod.vo.security.OAuthClientDetail;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import reactor.core.publisher.Mono;

public class OAuthClientDetailsServices implements ClientDetailsService {

    private OAuthClientRepository clientRepository;

    public OAuthClientDetailsServices(OAuthClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public ClientDetails loadClientByClientId(String s) throws ClientRegistrationException {
        Mono<OAuthClientDetail> clientDetails = clientRepository.findByClientId(s);
        return clientDetails.block().of(System.currentTimeMillis());
    }
}
