package com.tinnova.lojaveiculo.config;

import com.tinnova.lojaveiculo.security.JwtAuthenticationEntryPoint;
import com.tinnova.lojaveiculo.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final JwtAuthenticationEntryPoint authenticationEntryPoint;

    /**
     * Configuração principal do Spring Security.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http

                // API REST não utiliza CSRF
                .csrf(csrf -> csrf.disable())

                // Não cria sessão HTTP
                .sessionManagement(session ->

                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS))

                // Tratamento de erro de autenticação
                .exceptionHandling(exception ->

                        exception.authenticationEntryPoint(
                                authenticationEntryPoint))

                // Permissões
                .authorizeHttpRequests(auth -> auth

                        /*
                         * Login
                         */
                        .requestMatchers(
                                "/auth/login"
                        ).permitAll()

                        /*
                         * Swagger
                         */
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/webjars/**"
                        ).permitAll()

                        /*
                         * H2 Console
                         */
                        .requestMatchers(
                                "/h2-console/**"
                        ).permitAll()

                        /*
                         * GET
                         */
                        .requestMatchers(HttpMethod.GET,
                                "/veiculos/**")
                        .hasAnyRole("USER", "ADMIN")

                        /*
                         * POST
                         */
                        .requestMatchers(HttpMethod.POST,
                                "/veiculos/**")
                        .hasRole("ADMIN")

                        /*
                         * PUT
                         */
                        .requestMatchers(HttpMethod.PUT,
                                "/veiculos/**")
                        .hasRole("ADMIN")

                        /*
                         * PATCH
                         */
                        .requestMatchers(HttpMethod.PATCH,
                                "/veiculos/**")
                        .hasRole("ADMIN")

                        /*
                         * DELETE
                         */
                        .requestMatchers(HttpMethod.DELETE,
                                "/veiculos/**")
                        .hasRole("ADMIN")

                        .requestMatchers("/error")
                        .permitAll()
                        /*
                         * Qualquer outra requisição
                         */
                        .anyRequest()

                        .authenticated())

                /*
                 * Necessário para o H2 Console
                 */
                .headers(headers ->

                        headers.frameOptions(
                                frame -> frame.disable()));

        /*
         * Adiciona o filtro JWT
         */
        http.addFilterBefore(

                jwtAuthenticationFilter,

                UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Password Encoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    /**
     * Authentication Manager.
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)
            throws Exception {

        return configuration.getAuthenticationManager();
    }

}