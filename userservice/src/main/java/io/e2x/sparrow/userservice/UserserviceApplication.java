package io.e2x.sparrow.userservice;

import io.e2x.sparrow.sparrowwebconfiguration.SparrowWebSecurityConfigurerAdapter;
import io.e2x.sparrow.sparrowwebconfiguration.client.ClientResources;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.CompositeFilter;

import javax.servlet.Filter;
import java.util.stream.Stream;

@SpringBootApplication
public class UserserviceApplication extends SparrowWebSecurityConfigurerAdapter{
	private final Boolean disableDefaults = false;
	public static final String root = "/user";

	public UserserviceApplication(Boolean b) {
		super(b);
	}
	public UserserviceApplication(){
		this(false);
		initWithValue(root,new CompositeFilter(),"/","/all","/byId/{id}");
	}
	private void initWithValue(String root, Filter filter, String... antPatterns){
		this.setRoot(root);
		setFilter(filter);
		setAntPatterns(antPatterns);
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

	public static void main(String[] args) {
		SpringApplication.run(UserserviceApplication.class, args);
	}
}
