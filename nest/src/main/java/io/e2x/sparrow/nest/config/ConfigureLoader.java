/*
 * Project:sparrow nest
 * LastModified:18-4-19 下午9:40 by sukimiya
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

package io.e2x.sparrow.nest.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

public class ConfigureLoader {
    public SparrowConfigurationRepository getS_config() {
        return s_config;
    }

    private SparrowConfigurationRepository s_config;
    public ConfigureLoader(SparrowConfigurationRepository s_config) {
        this.s_config = s_config;
    }

    public String getAdmin_name() {
        return admin_name;
    }

    public void setAdmin_name(String admin_name) {
        this.admin_name = admin_name;
    }

    public String getAdmin_password() {
        return admin_password;
    }

    public void setAdmin_password(String admin_password) {
        this.admin_password = admin_password;
    }

    private String admin_name="admin";
    private String admin_password="{YWRtaW46VnJh.}";
}
