package com.adventureseekers.adventurewebapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.adventureseekers.adventurewebapi.security.AuthenticationFilter;
import com.adventureseekers.adventurewebapi.security.AuthorizationFilter;
import com.adventureseekers.adventurewebapi.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
    private UserService userService;
	
	@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManagerBean());
		authenticationFilter.setFilterProcessesUrl("/login");
		
		http.cors().and().csrf().disable().authorizeRequests()
	        .antMatchers(HttpMethod.POST, "/api/users/register").permitAll()
	        .antMatchers(HttpMethod.POST, "/api/users/confirmation/**").permitAll()
	        .antMatchers(HttpMethod.POST, "/api/users/resend/**").permitAll()
	        .anyRequest().authenticated()
	        .and()
	        .addFilter(authenticationFilter)
	        .addFilterBefore(new AuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
	}
	
	/**
	 * Configuration for CORS
	 */
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
		return source;
	}
	
	// beans
    /**
     * BCrypt bean definition
     */
    @Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
    
    /**
     * Authentication provider bean definition
     * @return The new authentication provider
     */
  	@Bean
  	public DaoAuthenticationProvider authenticationProvider() {
  		DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
  		auth.setUserDetailsService(userService); //set the custom user details service
  		auth.setPasswordEncoder(passwordEncoder()); //set the password encoder - bcrypt
  		return auth;
  	}
  	
  	@Bean
  	@Override
  	public AuthenticationManager authenticationManagerBean() throws Exception {
  		return super.authenticationManagerBean();
  	}
}












