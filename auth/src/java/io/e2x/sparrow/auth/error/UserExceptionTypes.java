/*
 * Project:sparrow auth
 * LastModified:18-5-29 上午10:31 by sukimiya
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

package io.e2x.sparrow.auth.error;

public enum  UserExceptionTypes {
    NOT_USER(0x301,"User not exist"),
    USER_EXIST(0x302,"User already exist");


    public int id;
    public String message;
    UserExceptionTypes(int errorid, String errormsg){
        this.id = errorid;
        this.message = errormsg;
    }
}
