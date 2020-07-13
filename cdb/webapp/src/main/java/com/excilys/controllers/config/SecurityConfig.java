package com.excilys.controllers.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.DigestAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.DigestAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    DigestAuthenticationFilter digestFilter;

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/", "/page", "/addUser")
                .permitAll()
                .antMatchers(HttpMethod.POST, "/addUser")
                .permitAll();

        http.authorizeRequests().antMatchers("/editComputer*", "/addComputer")
                .authenticated();
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/page")
                .hasRole("ADMIN");
        http.addFilter(this.digestFilter);
        http.exceptionHandling(e -> e.authenticationEntryPoint(entryPoint()));
    }

    @Bean
    DigestAuthenticationEntryPoint entryPoint() {
        DigestAuthenticationEntryPoint authEntryPoint = new DigestAuthenticationEntryPoint();
        authEntryPoint.setRealmName("t450");
        authEntryPoint.setKey("plopiplop");
        return authEntryPoint;
    }

    @Bean
    AuthenticationManager authManager() throws Exception {
        return authenticationManager();
    }

    @Bean
    PasswordEncoder enc() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    DigestAuthenticationFilter digestFilter(final UserDetailsService service) {
        DigestAuthenticationFilter filter = new DigestAuthenticationFilter();
        filter.setUserDetailsService(service);
        filter.setAuthenticationEntryPoint(entryPoint());
        filter.setPasswordAlreadyEncoded(false);

        return filter;
    }
}
