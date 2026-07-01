package com.tinnova.lojaveiculo.controller;

import com.tinnova.lojaveiculo.dto.request.LoginDTO;
import com.tinnova.lojaveiculo.dto.response.TokenDTO;
import com.tinnova.lojaveiculo.security.JwtService;
import com.tinnova.lojaveiculo.security.UserDetailsServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Endpoints responsáveis pela autenticação de usuários")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final UserDetailsServiceImpl userDetailsService;

    private final JwtService jwtService;

    @Operation(summary = "Realiza autenticação e gera um Token JWT")
    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(
            @Valid @RequestBody LoginDTO dto) {

        try {

            authenticationManager.authenticate(

                    new UsernamePasswordAuthenticationToken(
                            dto.login(),
                            dto.senha())
            );

            UserDetails userDetails =
                    userDetailsService.loadUserByUsername(dto.login());

            String token =
                    jwtService.generateToken(userDetails);

            return ResponseEntity.ok(
                    new TokenDTO(token, "Bearer"));

        } catch (BadCredentialsException ex) {

            throw new BadCredentialsException(
                    "Usuário ou senha inválidos.");
        }
    }

}
