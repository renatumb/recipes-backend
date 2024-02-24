package org.example.recipesbackend.config.websecurity;

import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.example.recipesbackend.config.jwt.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.HttpMethod.*;

@Slf4j
@EnableWebSecurity
@Configuration
public class AppConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        log.debug(">>>>>    AppConfig.securityFilterChain() ");
        httpSecurity
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable())
                .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(getCorsConfiguration()))
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                        authorizationManagerRequestMatcherRegistry
                                .requestMatchers("/swagger-ui/**", "/v3/**", "/auth/**" ).permitAll()
                                .requestMatchers("/recipe/**", "/users/**").authenticated()
                                .anyRequest().denyAll()
                )
                .addFilterBefore( jwtFilter, UsernamePasswordAuthenticationFilter.class)
                //.formLogin(withDefaults())
                .httpBasic(withDefaults())
        ;
        return httpSecurity.build();
    }

    private CorsConfigurationSource getCorsConfiguration() {
        return new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                //UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
                CorsConfiguration corsConfiguration = new CorsConfiguration();

                corsConfiguration.setAllowCredentials(true);
                corsConfiguration.setAllowedOrigins(List.of("http://localhost:4200"));
                corsConfiguration.setAllowedMethods(List.of(GET.name(), POST.name(), PUT.name(), PATCH.name(), DELETE.name(), OPTIONS.name()));
                corsConfiguration.setAllowedHeaders(List.of("X-Requested-With", ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN, CONTENT_TYPE, ACCEPT, AUTHORIZATION, ACCESS_CONTROL_REQUEST_METHOD, ACCESS_CONTROL_REQUEST_HEADERS, ACCESS_CONTROL_ALLOW_CREDENTIALS));
                corsConfiguration.setExposedHeaders(List.of("X-Requested-With", ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN, CONTENT_TYPE, ACCEPT, AUTHORIZATION, ACCESS_CONTROL_REQUEST_METHOD, ACCESS_CONTROL_REQUEST_HEADERS, ACCESS_CONTROL_ALLOW_CREDENTIALS));

                //urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);

                return corsConfiguration;
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        log.debug(">>>>>    AppConfig.passwordEncoder() ");
        return new BCryptPasswordEncoder();
    }
}
