package project.codegeneration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import project.codegeneration.security.JwtFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfiguration {

    private JwtFilter jwtFilter;

    public WebSecurityConfiguration(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable());
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));


        http.headers((headers) ->
                headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
        );

        http.authorizeHttpRequests(requests -> requests.requestMatchers("/login").permitAll());
        http.authorizeHttpRequests(requests -> requests.requestMatchers("/atm/login").permitAll());
        http.authorizeHttpRequests(requests -> requests.requestMatchers("/users").permitAll());
        http.authorizeHttpRequests(requests -> requests.requestMatchers("/demo").permitAll());
        http.authorizeHttpRequests(requests -> requests.requestMatchers("/h2-console").permitAll());
        http.authorizeHttpRequests(requests -> requests.requestMatchers("/h2-console/**").permitAll());
        http.authorizeHttpRequests(requests -> requests.requestMatchers("/api/**").permitAll());
        http.authorizeHttpRequests(requests -> requests.anyRequest().permitAll());
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:5173",
                "https://hicham.github.io/project-code-generation/",
                "https://hicham.github.io/"
        ));
        configuration.setAllowedMethods(Arrays.asList("GET","POST", "PUT", "DELETE", "HEAD", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }



}
