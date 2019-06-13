package io.archilab.prox.projectservice.config;

import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticationProcessingFilter;
import org.keycloak.adapters.springsecurity.filter.KeycloakPreAuthActionsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

@Configuration
@EnableWebSecurity
class KeycloakConfiguration extends KeycloakWebSecurityConfigurerAdapter {

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    KeycloakAuthenticationProvider keycloakAuthenticationProvider = this
        .keycloakAuthenticationProvider();
    keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
    auth.authenticationProvider(keycloakAuthenticationProvider);
  }

  @Bean
  @Override
  protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
//		return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    return new NullAuthenticatedSessionStrategy();
  }

  @Bean
  public KeycloakConfigResolver KeycloakConfigResolver() {
    return new KeycloakSpringBootConfigResolver();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    super.configure(http);

    http
//	    .cors()
//        .and()
        // TODO vlt. in Zukunft csrf protection aktiveren, dann müsste im Client ein solches Token immer mitgeschickt werden
        .csrf()
        .disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // STATELESS
        .sessionAuthenticationStrategy(this.sessionAuthenticationStrategy())
        .and()
        .authorizeRequests()
        .antMatchers(HttpMethod.GET, "/projects*").permitAll()
        .antMatchers(HttpMethod.GET, "/projects/*").permitAll()
        .antMatchers(HttpMethod.GET, "/projects/**").permitAll()
        .antMatchers("/projects*").hasRole("Dozent")
        .antMatchers("/projects/*").hasRole("Dozent")
        .antMatchers("/projects/**").hasRole("Dozent")
        .antMatchers(HttpMethod.GET, "/projectStudyCourses*").permitAll()
        .antMatchers(HttpMethod.GET, "/projectStudyCourses/*").permitAll()
        .antMatchers(HttpMethod.GET, "/projectStudyCourses/**").permitAll()
        .antMatchers("/projectStudyCourses*").denyAll()
        .antMatchers("/projectStudyCourses/*").denyAll()
        .antMatchers("/projectStudyCourses/**").denyAll()
        .antMatchers(HttpMethod.GET, "/projectModules*").permitAll()
        .antMatchers(HttpMethod.GET, "/projectModules/*").permitAll()
        .antMatchers(HttpMethod.GET, "/projectModules/**").permitAll()
        .antMatchers("/projectModules*").denyAll()
        .antMatchers("/projectModules/*").denyAll()
        .antMatchers("/projectModules/**").denyAll()
        .antMatchers("/").permitAll()
        .anyRequest().denyAll();
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