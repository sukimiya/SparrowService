/*
 * Project:sparrow userservice
 * LastModified:18-4-10 下午8:33 by lily
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

package io.e2x.sparrow.userservice;

import io.e2x.sparrow.sparrowwebconfiguration.SSOFilter;
import io.e2x.sparrow.sparrowwebconfiguration.SparrowWebSecurityConfigurerAdapter;
import io.e2x.sparrow.sparrowwebconfiguration.client.ClientResources;
import io.e2x.sparrow.userservice.vo.UserRepository;
import io.e2x.sparrow.userservice.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.filter.CompositeFilter;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@SpringBootApplication
@RestController
@EnableOAuth2Client
@EnableAuthorizationServer
@Order(SecurityProperties.BASIC_AUTH_ORDER)
public class UserserviceApplication extends SparrowWebSecurityConfigurerAdapter{
	private final Boolean disableDefaults = false;
	public static final String root = "/user";

	OAuth2ClientContext oAuth2ClientContext;

	protected UserserviceApplication() {
		super(false, "/user", new CompositeFilter(), "/","/all","/byId/{id}");
	}

	@Bean
	@ConfigurationProperties("github")
	public ClientResources github() {
		return new ClientResources();
	}

	@Bean
	@ConfigurationProperties("facebook")
	public ClientResources facebook() {
		return new ClientResources();
	}
	@Override
	protected void initWithValue(){
		CompositeFilter filter = (CompositeFilter) this.getFilter();
		OAuth2ClientContext context = oAuth2ClientContext;
		List<Filter> filters = new ArrayList<>();
		filters.add(new SSOFilter(context,github(),"/github"));
		filters.add(new SSOFilter(context,facebook(),"/facebook"));
		filter.setFilters(filters);
		setFilter(filter);
	}
	@Override
	protected void configure(HttpSecurity http) throws Exception {

	}
	@Bean
	CommandLineRunner initiateRun(UserRepository userRepository){
		return args -> Stream.of("sukimiya,m19547047","jonh,Mc97Tem34U","user25,sM09873575.")
				.map(tpl -> tpl.split(","))
				.forEach(tpl -> userRepository.save(new UserVO(tpl[0],tpl[1],true)));
	}
	public static void main(String[] args) {
		SpringApplication.run(UserserviceApplication.class, args);
	}
}
