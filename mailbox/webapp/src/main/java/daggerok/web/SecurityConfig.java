package daggerok.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

  @Bean MapReactiveUserDetailsService userDetailsService() {
    return new MapReactiveUserDetailsService(
        User.withUsername("max")
            .password("max")
            .roles("USER")
            .build()
    );
  }

  @Bean PasswordEncoder passwordEncoder() {
    return NoOpPasswordEncoder.getInstance();
  }
}
