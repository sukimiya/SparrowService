/*
 * Project:sparrow nest
 * LastModified:18-4-17 下午7:49 by sukimiya
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

package io.e2x.sparrow.nest.web.controller.event;

import io.e2x.sparrow.nest.security.model.OAuthClientDetail;
import io.e2x.sparrow.nest.security.model.OUserDetails;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

public class AdministratorHomeEvent {
    private long timestamp;

    public AdministratorHomeEvent() {
        timestamp = System.currentTimeMillis();
    }

    public List<OAuthClientDetail> getClients() {
        return clients;
    }

    public void setClients(List<OAuthClientDetail> clients) {
        this.clients = clients;
    }

    public List<OUserDetails> getUsers() {
        return users;
    }

    public void setUsers(List<OUserDetails> users) {
        this.users = users;
    }

    private List<OAuthClientDetail> clients;
    private List<OUserDetails> users;
}
