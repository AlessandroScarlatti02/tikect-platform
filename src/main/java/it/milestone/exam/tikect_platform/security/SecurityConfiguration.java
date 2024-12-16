package it.milestone.exam.tikect_platform.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests()
                .requestMatchers("/tickets/edit/**", "/tickets/delete/**").hasAuthority("ADMIN")
                .requestMatchers("/categories").hasAuthority("ADMIN")
                .requestMatchers("/tickets/create").hasAnyAuthority("ADMIN", "USER")
                .requestMatchers("/notes").hasAnyAuthority("ADMIN", "OPERATOR")
                .requestMatchers("/tickets", "/tickets/**").hasAnyAuthority("ADMIN", "OPERATOR", "USER")
                .requestMatchers("/**").permitAll()
                .and().formLogin().and().logout()
                .and().exceptionHandling()
                .and().csrf().disable();
        ;

        return http.build();

    }

    @Bean
    public DatabaseUserDetailsService userDetailsService() {
        return new DatabaseUserDetailsService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());

        return provider;
    }
}
