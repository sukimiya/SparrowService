/*
 * Project:sparrow crud
 * LastModified:18-5-22 上午10:49 by sukimiya
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

import io.e2x.sparrow.crud.mongod.vo.security.OUserDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Data
public class UserVO {


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;

    public UserInformations getInformations() {
        return informations;
    }

    public void setInformations(UserInformations informations) {
        this.informations = informations;
    }

    public OUserDetail getAuth() {
        return auth;
    }

    public void setAuth(OUserDetail auth) {
        this.auth = auth;
    }

    private UserInformations informations;
    private OUserDetail auth;

    public UserVO(String id, UserInformations informations, OUserDetail auth) {
        this.id = id;
        this.informations = informations;
        this.auth = auth;
    }

    public UserVO() {
        id = ObjectId.get().toHexString();
    }
}
