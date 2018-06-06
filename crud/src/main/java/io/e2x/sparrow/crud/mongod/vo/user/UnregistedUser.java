/*
 * Project:sparrow crud
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

package io.e2x.sparrow.crud.mongod.vo.user;

import lombok.Data;

@Data
public class UnregistedUser extends UserSocialInformations{
    private long reqtimestamp;
    public UnregistedUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UnregistedUser(String username, String password, String firstname, String lastname, String wechatID, String email, String mobilePhone, String qQNumber,long time) {
        super(firstname,lastname, wechatID, email, mobilePhone, qQNumber);
        this.username = username;
        this.password = password;
        this.reqtimestamp = time;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String username;
    private String password;

}
