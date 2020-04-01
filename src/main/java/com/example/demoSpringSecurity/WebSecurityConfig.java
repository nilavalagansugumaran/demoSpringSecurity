package com.example.demoSpringSecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http//HTTP Basic authentication
                .httpBasic()
                .and()
            .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/orders").hasRole("USER")
                .antMatchers(HttpMethod.PUT, "/orders/**").hasRole("USER")
                .antMatchers(HttpMethod.DELETE, "/orders/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/orders/**").hasRole("USER")
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers(HttpMethod.GET, "/orders").permitAll()
                .antMatchers(HttpMethod.GET, "/orders/**").permitAll()
                .and()
                .csrf().disable()
                .formLogin().disable();
        
        // To allow H2 console displayed properly
        http.headers().frameOptions().disable();

    }

    @Autowired
    protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .inMemoryAuthentication()
                .withUser("nila").password("{noop}password").roles("USER")
                .and()
                .withUser("admin").password("{noop}admin").roles("USER", "ADMIN");
    }

//    @Autowired
//    private DataSource dataSource;
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//
//        auth.jdbcAuthentication().dataSource(dataSource)
//                .usersByUsernameQuery("select username, password, enabled"
//                        + " from users where username=?")
//                .authoritiesByUsernameQuery("select username, authority "
//                        + "from authorities where username=?")
//                .passwordEncoder(new BCryptPasswordEncoder());
//    }
}