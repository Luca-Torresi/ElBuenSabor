package com.example.demo.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Acá deben ir todas las rutas públicas, a las que se puede acceder sin necesidad de estar loggeado
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admininstrador/**").hasRole("ADMINISTRADOR")
                        .requestMatchers("/cajero/**").hasAnyRole("CAJERO", "ADMINISTRADOR")
                        .requestMatchers("/cocinero/**").hasRole("COCINERO")
                        .requestMatchers("/repartidor/**").hasRole("REPARTIDOR")
                        .requestMatchers(
                                "/",
                                "/public/**",
                                "/webjars/**",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-resources/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                // Valida tokens y extrae roles
                .oauth2ResourceServer(oauth -> oauth
                        .jwt(jwt -> jwt
                                .decoder(jwtDecoder())
                        )
                )
                // Ruta a la cual el usuario es redirigido en caso que el LogIn sea exitoso
                .oauth2Login(oauth -> oauth
                        .defaultSuccessUrl("/home", true)
                )
                // Ruta a la cual el usuario es redirigido una vez cerrada sesión
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                );
        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri("https://tu-dominio.auth0.com/.well-known/jwks.json")
                .build();
    }
}
