package com.sinnguyen;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.savedrequest.NullRequestCache;

@SpringBootApplication
@EnableScheduling
public class MrsApiApplication extends WebSecurityConfigurerAdapter {

	@Autowired
	DataSource dataSource;
	
	public static void main(String[] args) {
		SpringApplication.run(MrsApiApplication.class, args);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().httpBasic().and().requestCache().requestCache(new NullRequestCache()).and()
				.authorizeRequests().antMatchers("/admin/**").hasAnyRole("ADMIN").antMatchers("/user/**")
				.hasAnyRole("ADMIN", "USER").anyRequest().permitAll();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource)
				.usersByUsernameQuery("SELECT username, password, activated FROM user WHERE username = ?")
				.authoritiesByUsernameQuery("SELECT username, role FROM user WHERE username = ?")
				.passwordEncoder(new BCryptPasswordEncoder(12));
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
	}
}
