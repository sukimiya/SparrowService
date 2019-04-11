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

import io.e2x.sparrow.crud.mongod.vo.security.OUserDetail;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;
import org.springframework.data.couchbase.core.mapping.id.GenerationStrategy;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class UserInformations {
    public UserInformations(OUserDetail userDetail, UserCurrency userCurrency, UserSocialInformations userSocialInformations) {
        this.userDetail = userDetail;
        this.userCurrency = userCurrency;
        this.userSocialInformations = userSocialInformations;
    }

    public UserInformations(OUserDetail userDetail) {
        this.userDetail = userDetail;
        this.userCurrency = new UserCurrency(0,0);
        this.userSocialInformations = new UserSocialInformations();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy= GenerationStrategy.UNIQUE)
    private String id;

    public OUserDetail getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(OUserDetail userDetail) {
        this.userDetail = userDetail;
    }

    @DBRef
    private OUserDetail userDetail;

    private UserCurrency userCurrency;

    public UserCurrency getUserCurrency() {
        return userCurrency;
    }

    public void setUserCurrency(UserCurrency userCurrency) {
        this.userCurrency = userCurrency;
    }

    public UserSocialInformations getUserSocialInformations() {
        return userSocialInformations;
    }

    public void setUserSocialInformations(UserSocialInformations userSocialInformations) {
        this.userSocialInformations = userSocialInformations;
    }

    private UserSocialInformations userSocialInformations;
}
