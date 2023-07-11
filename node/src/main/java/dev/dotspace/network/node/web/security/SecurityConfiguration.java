package dev.dotspace.network.node.web.security;

import dev.dotspace.network.library.config.ConfigField;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collections;

@EnableWebSecurity
@Configuration
@EnableMethodSecurity
@Log4j2
public class SecurityConfiguration {
  /**
   * Get environment (config file)
   */
  @Autowired
  private Environment environment;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    log.info("Setting up security.");

    if (this.environment.getProperty(ConfigField.DEV_MODE, Boolean.class, false)) {
      http
        .authorizeHttpRequests(auth -> {
          auth.requestMatchers("/**").permitAll();
        })
        .formLogin(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .csrf(AbstractHttpConfigurer::disable);
      log.warn("Running in dev mode, security is disabled!");
    } else {
      // @formatter:off
      http
        .authorizeHttpRequests((authorize) -> authorize
          .requestMatchers("/test").permitAll()
          .anyRequest().authenticated()
        ).x509(authorize -> authorize
          .subjectPrincipalRegex("CN=(.*?)(?:,|$)")
        )
        .userDetailsService(userDetailsService())
        .formLogin(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable);
      // @formatter:on
      log.info("Security enabled.");
    }

    return http.build();
  }

  @Bean
  public UserDetailsService userDetailsService() {
    return username -> {
      if (username.isBlank()) throw new UsernameNotFoundException("Certificate provides invalid CN");

      return new User(username, Strings.EMPTY, Collections.emptyList());
      //  AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER"));
    };
  }

}
