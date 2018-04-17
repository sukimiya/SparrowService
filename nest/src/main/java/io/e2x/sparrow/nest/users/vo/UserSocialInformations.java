/*
 * Project:sparrow nest
 * LastModified:18-4-17 下午5:08 by sukimiya
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

package io.e2x.sparrow.nest.users.vo;

public class UserSocialInformations {
    public UserSocialInformations(){

    }
    public UserSocialInformations(String nikename, String wechatID, String email, String mobilePhone, String qQNumber) {
        this.nikename = nikename;
        this.wechatID = wechatID;
        this.email = email;
        this.mobilePhone = mobilePhone;
        this.qQNumber = qQNumber;
    }

    public String getNikename() {
        return nikename;
    }

    public void setNikename(String nikename) {
        this.nikename = nikename;
    }

    public String getWechatID() {
        return wechatID;
    }

    public void setWechatID(String wechatID) {
        this.wechatID = wechatID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getqQNumber() {
        return qQNumber;
    }

    public void setqQNumber(String qQNumber) {
        this.qQNumber = qQNumber;
    }

    private String nikename;
    private String wechatID;
    private String email;
    private String mobilePhone;
    private String qQNumber;

}
