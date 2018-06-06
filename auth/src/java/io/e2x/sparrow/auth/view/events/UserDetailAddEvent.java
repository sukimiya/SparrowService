/*
 * Project:sparrow auth
 * LastModified:18-4-29 下午8:20 by sukimiya
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

package io.e2x.sparrow.auth.view.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import io.e2x.sparrow.auth.view.event.StandardResponseEvent;
import org.springframework.data.mongodb.core.mapping.Document;

@JsonView
@Document
public class UserDetailAddEvent extends StandardResponseEvent {
    @JsonProperty("firstname")
    public String firstname;
    @JsonProperty("lastname")
    public String lastname;
    @JsonProperty("username")
    public String username;
    @JsonProperty("password")
    public String password;
    @JsonProperty("email")
    public String email;
    @JsonProperty("isenabled")
    public Boolean enabled = true;

    @JsonProperty(value = "id",required = false,access = JsonProperty.Access.READ_ONLY)
    public String userid;
    public UserDetailAddEvent(){}

    public UserDetailAddEvent(String firstname, String lastname, String username, String password, String email, Boolean enabled, String userid) {
        super(0,"OK");
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.password = password;
        this.email = email;
        this.enabled = enabled;
        this.userid = userid;

    }

    public UserDetailAddEvent(int resultId, String messages) {
        super(resultId, messages);
    }
}
