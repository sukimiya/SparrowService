package io.e2x.sparrow.sparrowwebconfiguration;


import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    private String path;

    public ResourceServerConfiguration() {
        super();
    }

    public ResourceServerConfiguration(String path){
        this();
        this.path = path;
    }
    @Override
    public void configure(HttpSecurity http) throws Exception{
        // @formatter:off
        http.antMatcher(path).authorizeRequests().anyRequest().authenticated();
        // @formatter:on
    }
}
