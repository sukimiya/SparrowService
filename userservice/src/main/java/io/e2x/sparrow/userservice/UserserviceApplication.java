package io.e2x.sparrow.userservice;

import io.e2x.sparrow.sparrowwebconfiguration.SSOFilter;
import io.e2x.sparrow.sparrowwebconfiguration.SparrowWebSecurityConfigurerAdapter;
import io.e2x.sparrow.sparrowwebconfiguration.client.ClientResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.filter.CompositeFilter;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@RestController
@EnableOAuth2Client
@EnableAuthorizationServer
@Order(SecurityProperties.BASIC_AUTH_ORDER)
public class UserserviceApplication extends SparrowWebSecurityConfigurerAdapter{
	private final Boolean disableDefaults = false;
	public static final String root = "/user";
	@Autowired
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

	public static void main(String[] args) {
		SpringApplication.run(UserserviceApplication.class, args);
	}
}
