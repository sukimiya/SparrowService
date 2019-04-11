/*
 * Project:sparrow nest
 * LastModified:18-4-17 下午5:14 by sukimiya
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

import io.e2x.sparrow.nest.users.vo.UserInformations;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserInfoRepository extends MongoRepository<UserInformations,String>{
    UserInformations findByUserDetail_Username(String s);
    UserInformations findUserInformationsByUserDetailId(String s);
    UserInformations findByUserSocialInformations_MobilePhone(String mobilephone);
}
