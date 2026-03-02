package cafe.shop.configuration;

import cafe.shop.model.AuthUser;
import cafe.shop.model.entities.User;
import cafe.shop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig  {

    @Autowired private JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/v1/admin").permitAll()
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/terminals/*/recipes").hasAnyRole("ADMIN", "OPERATOR", "CUSTOMER")
                        .requestMatchers("/api/v1/terminals/nearby").hasRole("CUSTOMER")
                        .requestMatchers("/api/v1/terminals", "/api/v1/terminals/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/queues", "/api/v1/queues/**").hasAnyRole("ADMIN", "OPERATOR")
                        .requestMatchers("/api/v1/users", "/api/v1/users/**").hasAnyRole("ADMIN", "OPERATOR", "CUSTOMER")
                        .requestMatchers("/api/v1/orders/**").hasAnyRole("ADMIN", "OPERATOR", "CUSTOMER")
                        .requestMatchers("/api/v1/product-authorizations/**").hasAnyRole("ADMIN", "OPERATOR")
                        .requestMatchers("/api/v1/customers/register").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
