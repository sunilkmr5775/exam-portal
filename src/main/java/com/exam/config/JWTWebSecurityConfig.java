package com.exam.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.exam.jwt.JwtAuthenticationEntryPoint;
import com.exam.jwt.JwtAuthenticationTokenFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class JWTWebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

	@Value("${jwt.get.token.uri}")
	private String authenticationPath;

	

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoderBean());
//		auth.userDetailsService(this.userDetailsService).passwordEncoder(passwordEncoder());
	}

	
	@Bean
	public PasswordEncoder passwordEncoderBean() {
		return new BCryptPasswordEncoder();
	}
//	@Bean
//	public PasswordEncoder passwordEncoder() {
//		return NoOpPasswordEncoder.getInstance();
//	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.csrf().disable().cors().disable().exceptionHandling()
				.authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()
				.antMatchers("authenticationPath", "/user/").permitAll().antMatchers(HttpMethod.OPTIONS).permitAll()
				.anyRequest().authenticated();

		httpSecurity.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

		httpSecurity.headers().frameOptions().sameOrigin() // H2 Console Needs this setting
				.cacheControl(); // disable caching
	}

	@Override
	public void configure(WebSecurity webSecurity) throws Exception {
		webSecurity.ignoring().antMatchers(HttpMethod.POST, authenticationPath).antMatchers(HttpMethod.OPTIONS, "/**")
				.and().ignoring().antMatchers(HttpMethod.GET, "/" // Other Stuff You want to Ignore
				).and().ignoring()
				// .antMatchers("/h2-console/**/**") // Should not be done in Production!
				.antMatchers("/swagger-ui.html**", "/webjars/**", "/v2/**").antMatchers("/swagger-resources/**");
	}

}
