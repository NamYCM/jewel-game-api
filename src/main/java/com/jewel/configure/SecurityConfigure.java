package com.jewel.configure;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfigure extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf().ignoringAntMatchers("/api/**");
        http.cors().and().csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests().antMatchers("/login/**").permitAll().and()
                .authorizeRequests().antMatchers("/admin/**").hasAuthority("admin").and()
                .authorizeRequests().antMatchers("/user/**").hasAuthority("user").and()
                .authorizeRequests().antMatchers(HttpMethod.GET,"/levelMap/**").hasAnyAuthority("user","admin").and()
                .authorizeRequests().antMatchers(HttpMethod.POST,"/levelMap/**").hasAuthority("admin").and()
                .authorizeRequests().antMatchers(HttpMethod.PUT,"/levelMap/**").hasAuthority("admin").and()
                .authorizeRequests().antMatchers(HttpMethod.DELETE,"/levelMap/**").hasAuthority("admin").and()
                .authorizeRequests().antMatchers("/shop/**").hasAnyAuthority("user","admin").and()
                .addFilterBefore(new JwtRequestFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
