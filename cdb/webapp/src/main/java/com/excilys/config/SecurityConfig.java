





package com.excilys.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.excilys.adapters.UserDtoAdapter;
import com.excilys.restcontrollers.security.JwtAuthenticationEntryPoint;
import com.excilys.restcontrollers.security.JwtRequestFilter;


@Configuration
@EnableWebSecurity
@PropertySource(value = "classpath:private.properties")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

     @Autowired
     private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

     @Autowired
     UserDtoAdapter userService;

     @Autowired
     private JwtRequestFilter jwtRequestFilter;


     @Bean
     @Override
     public AuthenticationManager authenticationManagerBean() throws Exception {
          return super.authenticationManagerBean();
     }

     @Override
     protected void configure(final HttpSecurity http) throws Exception {
          http.authorizeRequests().antMatchers(HttpMethod.GET, "api//computers/**")
                    .hasAnyAuthority("USER", "ADMIN")
                    .antMatchers(HttpMethod.POST, "/api/computers/**").hasAuthority("ADMIN")
                    .antMatchers(HttpMethod.DELETE, "/api/computers/**").hasAuthority("ADMIN")
                    .antMatchers(HttpMethod.PUT, "/api/computers/**").hasAuthority("ADMIN")
                    .antMatchers(HttpMethod.POST, "/register/admin").hasAuthority("ADMIN")
                    .and().csrf().disable()
                    .exceptionHandling().authenticationEntryPoint(this.jwtAuthenticationEntryPoint).and()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
          http.addFilterBefore(this.jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
     }

     @Autowired
     public void configureGlobal(final AuthenticationManagerBuilder auth) throws Exception {
          auth.userDetailsService(this.userService).passwordEncoder(passwordEncoder());
     }

     @Bean
     public PasswordEncoder passwordEncoder() {
          return new BCryptPasswordEncoder();
     }

}
