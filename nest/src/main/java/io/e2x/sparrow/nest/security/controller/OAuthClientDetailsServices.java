/*
 * Project:sparrow nest
 * LastModified:18-4-12 下午11:15 by lily
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

import io.e2x.sparrow.nest.security.model.OAuthClientDetail;
import io.e2x.sparrow.nest.security.model.OAuthClientRepository;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;

import java.util.Date;

public class OAuthClientDetailsServices implements ClientDetailsService {

    private OAuthClientRepository clientRepository;

    public OAuthClientDetailsServices(OAuthClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public ClientDetails loadClientByClientId(String s) throws ClientRegistrationException {
        OAuthClientDetail clientDetails = clientRepository.findByClientId(s);
        return clientDetails.of(System.currentTimeMillis());


    }
}
