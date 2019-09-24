package io.archilab.prox.projectservice.config;

import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.keycloak.adapters.springsecurity.management.HttpSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

@KeycloakConfiguration
class SecurityConfig extends KeycloakWebSecurityConfigurerAdapter {

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    KeycloakAuthenticationProvider keycloakAuthenticationProvider =
        this.keycloakAuthenticationProvider();
    keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
    auth.authenticationProvider(keycloakAuthenticationProvider);
  }

  @Bean
  @Override
  @ConditionalOnMissingBean(HttpSessionManager.class)
  protected HttpSessionManager httpSessionManager() {
    return new HttpSessionManager();
  }

  @Bean
  @Override
  protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
    return new NullAuthenticatedSessionStrategy();
  }

  @Bean
  public KeycloakConfigResolver KeycloakConfigResolver() {
    return new KeycloakSpringBootConfigResolver();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    super.configure(http);
    http.csrf().disable().authorizeRequests()

        .antMatchers(HttpMethod.GET, "/projects/**").permitAll()
        .antMatchers(HttpMethod.HEAD, "/projects/**").permitAll()
        .antMatchers(HttpMethod.OPTIONS, "/projects/**").permitAll().antMatchers("/projects/**")
        .hasRole("professor")

        .antMatchers(HttpMethod.GET, "/projectStudyCourses/**").permitAll()
        .antMatchers(HttpMethod.HEAD, "/projectStudyCourses/**").permitAll()
        .antMatchers(HttpMethod.OPTIONS, "/projprojectStudyCoursesects/**").permitAll()
        .antMatchers("/projectStudyCourses/**").denyAll()

        .antMatchers(HttpMethod.GET, "/projectModules/**").permitAll()
        .antMatchers(HttpMethod.HEAD, "/projectModules/**").permitAll()
        .antMatchers(HttpMethod.OPTIONS, "/projectModules/**").permitAll()
        .antMatchers("/projectModules/**").denyAll()

        .antMatchers("/profile/**").permitAll().anyRequest().denyAll();
  }

  // @Bean
  // public FilterRegistrationBean keycloakAuthenticationProcessingFilterRegistrationBean(
  // KeycloakAuthenticationProcessingFilter filter) {
  // FilterRegistrationBean registrationBean = new FilterRegistrationBean(filter);
  // registrationBean.setEnabled(false);
  // return registrationBean;
  // }
  //
  // @Bean
  // public FilterRegistrationBean keycloakPreAuthActionsFilterRegistrationBean(
  // KeycloakPreAuthActionsFilter filter) {
  // FilterRegistrationBean registrationBean = new FilterRegistrationBean(filter);
  // registrationBean.setEnabled(false);
  // return registrationBean;
  // }
}
