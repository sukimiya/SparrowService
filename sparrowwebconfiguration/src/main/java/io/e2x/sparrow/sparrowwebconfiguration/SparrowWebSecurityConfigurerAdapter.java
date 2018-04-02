package io.e2x.sparrow.sparrowwebconfiguration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import javax.servlet.Filter;

public class SparrowWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

    private String [] antPatterns;

    public void setAntPatterns(String[] antPatterns) {
        this.antPatterns = antPatterns;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    private String root;
    private Filter filter;

    public String[] getAntPatterns() {
        return antPatterns;
    }

    public String getRoot() {
        return root;
    }

    public Filter getFilter() {
        return filter;
    }
    protected SparrowWebSecurityConfigurerAdapter(Boolean b) {
        super(b);
    }

    public SparrowWebSecurityConfigurerAdapter(boolean disableDefaults, String root, Filter filter, String... antPatterns){
        this(disableDefaults);
        this.root = root;
        this.antPatterns = antPatterns;
        this.filter = filter;
    }
    protected void initWithValue(){

    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        initWithValue();
        http.antMatcher(getRoot()).authorizeRequests().antMatchers(getAntPatterns()).permitAll().anyRequest()
                .authenticated().and().exceptionHandling()
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint(getRoot())).and().logout()
                .logoutSuccessUrl(getRoot()).permitAll().and().csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and()
                .addFilterBefore(getFilter(), BasicAuthenticationFilter.class);
    }
}
