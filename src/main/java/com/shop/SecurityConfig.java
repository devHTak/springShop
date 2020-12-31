package com.shop;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// TODO Auto-generated method stub
		http.csrf().disable()
			.authorizeRequests()
			.antMatchers("/", "/login", "/accounts", "/vendors").permitAll()
			.anyRequest().authenticated();
		
		http.formLogin().loginPage("/login").defaultSuccessUrl("/").permitAll();
		http.logout().logoutUrl("/logout").logoutSuccessUrl("/");
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		// TODO Auto-generated method stub
		web.ignoring()
		.antMatchers("/node_modules/**")
		.requestMatchers(PathRequest.toStaticResources().atCommonLocations());
	}
	
	
	
	

}
