package io.archilab.projektboerse.projectservice.config;

import java.util.Arrays;

import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakSecurityComponents;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticationProcessingFilter;
import org.keycloak.adapters.springsecurity.filter.KeycloakPreAuthActionsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebSecurity
class KeyCon extends KeycloakWebSecurityConfigurerAdapter
{
	/**
	 * Registers the KeycloakAuthenticationProvider with the authentication manager.
	 */
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		KeycloakAuthenticationProvider keycloakAuthenticationProvider = keycloakAuthenticationProvider();
		keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
		auth.authenticationProvider(keycloakAuthenticationProvider);
	}

	/**
	 * Defines the session authentication strategy.
	 */
	@Bean
	@Override
	protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
//		return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
		return new NullAuthenticatedSessionStrategy();
	}

	@Bean
	public KeycloakConfigResolver KeycloakConfigResolver() {return new KeycloakSpringBootConfigResolver();}

	@Override
	protected void configure(HttpSecurity http) throws Exception
	{
		super.configure(http);
		
	    http
	    .cors()
        .and()
	    .csrf()
        .disable()     
	    
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // STATELESS
        .sessionAuthenticationStrategy(sessionAuthenticationStrategy())
        .and()
        .authorizeRequests()
        .antMatchers(HttpMethod.GET,"/projects*").permitAll()
	    .antMatchers(HttpMethod.GET,"/projects/*").permitAll()
	    .antMatchers(HttpMethod.GET,"/projects/**").permitAll()
		.antMatchers("/projects*").hasRole("Dozent")
		.antMatchers("/projects/*").hasRole("Dozent")
		.antMatchers("/projects/**").hasRole("Dozent")
		.antMatchers(HttpMethod.GET,"/modules*").permitAll()
	    .antMatchers(HttpMethod.GET,"/modules/*").permitAll()
	    .antMatchers(HttpMethod.GET,"/modules/**").permitAll()
		.antMatchers("/modules*").hasRole("Dozent")
		.antMatchers("/modules/*").hasRole("Dozent")
		.antMatchers("/modules/**").hasRole("Dozent")
        .anyRequest().permitAll();
	}
	
	

	@Bean
	public FilterRegistrationBean keycloakAuthenticationProcessingFilterRegistrationBean(
	        KeycloakAuthenticationProcessingFilter filter) {
	    FilterRegistrationBean registrationBean = new FilterRegistrationBean(filter);
	    registrationBean.setEnabled(false);
	    return registrationBean;
	}

	@Bean
	public FilterRegistrationBean keycloakPreAuthActionsFilterRegistrationBean(
	        KeycloakPreAuthActionsFilter filter) {
	    FilterRegistrationBean registrationBean = new FilterRegistrationBean(filter);
	    registrationBean.setEnabled(false);
	    return registrationBean;
	}
	
}
	
	

