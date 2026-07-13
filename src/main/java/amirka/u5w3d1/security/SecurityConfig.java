package amirka.u5w3d1.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // Marks this class as a Spring configuration class.
@EnableWebSecurity // Enables Spring Security and allows custom security configuration.
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {

        // Disables the default HTML login page since we're using a REST API.
        httpSecurity.formLogin(formLogin -> formLogin.disable());

        // Makes the application stateless so no HTTP session is created or used.
        httpSecurity.sessionManagement(sessions ->
                sessions.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Disables CSRF protection because it's unnecessary for stateless APIs using JWT.
        httpSecurity.csrf(csrf -> csrf.disable());

        // Allows every request to every endpoint without authentication.
        httpSecurity.authorizeHttpRequests(request ->
                request.requestMatchers("/**")
                        .permitAll());

        // Builds and returns the configured security filter chain.
        return httpSecurity.build();
    }
}
